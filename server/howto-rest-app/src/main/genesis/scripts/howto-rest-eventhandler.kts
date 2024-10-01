import global.genesis.api.accounts.api.AccountControllerApi
import global.genesis.api.accounts.api.AccountControllerApi.FindAllUsingGETRequest
import global.genesis.pipeline.PipelineManager

/**
 * This file defines the event handler APIs. These APIs (modeled after CQRS
 * commands) allow callers to manipulate the data in the database. By default,
 * insert, update and delete event handlers (or commands) have been created.
 * These result in the data being written to the database and messages to be
 * published for the rest of the platform to by notified.
 *
 * Custom event handlers may be added to extend the functionality of the
 * application as well as custom logic added to existing event handlers.
 *
 * The following objects are visible in each eventhandler
 * `event.details` which holds the entity that this event handler is acting upon
 * `entityDb` which is database object used to perform INSERT, MODIFY and UPDATE the records
 * Full documentation on event handler may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/

 */

val accountsApi = AccountControllerApi("http://localhost:8080")
accountsApi.registerApiToken("Authorization", "Basic YWRtaW46YWRtaW4=")

eventHandler {

    // PIPELINES:
    // Firstly we must inject the PipelineManager
    val pipelineManager: PipelineManager = inject()

    // This is a normal eventhandler for inserting a simple trade
    // We will hook our pipeline into this
    eventHandler<Trade>("TRADE_INSERT", transactional = true) {
        onCommit { event ->
            // Our trade table has a relation to the accounts table from our pipeline in the form of the accountNumber field
            val tradeInsert = event.details

            val pipelineName = "OPEN_API_INTEGRATION"
            // To retrieve our pipeline from the manager we must pass the request type and the name
            val pipeline = pipelineManager
                .getTypedBatchPipeline<FindAllUsingGETRequest>(pipelineName)!!

            // Here we simply insert the trade from the event as normal
            val insertedTrade = entityDb.insert(tradeInsert).record

            // Once the trade is inserted we retrieve the account linked to it by executing the pipeline
            // We construct the request from the generated code, passing the accountNumber from the inserted trade
            pipeline.execute(
                FindAllUsingGETRequest(
                    accountNumber = insertedTrade.accountNumber
                )
            ).await() // Execution can be handled in many ways, here we simply await the result

            // At this point our trade has inserted and the linked account has been queried from the external API and pulled into our table
            ack(listOf(mapOf("TRADE_ID" to insertedTrade.tradeId)))
        }
    }

    // OPEN_API: You can also use the Genesis Evaluator to schedule eventhandler calls to your API
    // See https://docs.genesis.global/docs/develop/server-capabilities/real-time-triggers-evaluator/ for details.
    eventHandler<Unit>("CRON_REST") {
        onCommit {
            val accounts = accountsApi.findAllUsingGET(
                FindAllUsingGETRequest(
                    _limit = 100,
                    _pageIndex = 0
                )
            ).accounts

            LOG.info("Retrieved accounts {}", accounts.joinToString(","))
            ack()
        }
    }
}
