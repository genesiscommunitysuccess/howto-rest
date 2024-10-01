import global.genesis.api.accounts.api.AccountControllerApi
import global.genesis.api.accounts.api.AccountControllerApi.FindAllUsingGETRequest
import global.genesis.api.accounts.schema.AccountDTO
import global.genesis.pipeline.api.db.DbOperation
import global.genesis.pipeline.util.ListSplitter
import kotlinx.coroutines.flow.flowOf

/**
 *
 *   System              : howto-rest
 *   Sub-System          : howto-rest Configuration
 *   Version             : 1.0
 *   Copyright           : (c) GENESIS
 *   Date                : 2021-09-07
 *
 *   Function : Provide Data Pipeline Configuration for howto-rest.
 *
 *   Modification History
 *
 */

// PIPELINES
// To use the generated code we must instantiate the generated API class
val accountsApi = AccountControllerApi("http://localhost:8080")
// Configure authorisation, this will depend on your external API
accountsApi.registerApiToken("Authorization", "Basic YWRtaW46YWRtaW4=")

pipelines {
    pipeline("OPEN_API_INTEGRATION") {
        // PIPELINES
        // Using typedBatchSource we can provide our generated classes, making the pipeline concise and type safe
        source(typedBatchSource<FindAllUsingGETRequest, List<AccountDTO>> {
            request {
                accountsApi.findAllUsingGET(request).accounts
            }
        }).split(
            // ListSplitter will take our List<AccountDTO> and pass each AccountDTO to the map function
            ListSplitter()
        ).map { accountDto ->
            // PIPELINES
            // In order to insert the resulting accounts into the DB
            // we need to map them to our generated table classes
            DbOperation.Upsert(
                Accounts {
                    accountNumber = accountDto.accountNumber
                    balance = accountDto.balance
                    brokerId = accountDto.brokerId
                    customerId = accountDto.customerId
                    owner = accountDto.owner
                }
            )
        }.sink(dbSink()) // Finally we sink to our database
    }
}
