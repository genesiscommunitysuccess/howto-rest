# Data Pipelines with REST Integration

As with other Genesis components, you can integrate your external API with data pipelines.
These pipelines can be triggered by other components and reused across your application, reducing duplication.

Relevant code is annotated with `// PIPELINES`

### Adding the necessary dependencies

In the [`build.gradle.kts`](server/howto-rest-app/build.gradle.kts) file, add the `genesis-pal-datapipeline` dependency

```kotlin
    implementation(genesis("pal-datapipeline"))
```

### Defining your pipeline

We can use the generated code from the Open API spec in our data pipelines as well.

See [`howto-rest-pipelines.kts`](server/howto-rest-app/src/main/genesis/scripts/howto-rest-pipelines.kts)

This pipeline will, when executed, use the generated controller to fetch data from the external API, based on the
request parameters passed to the pipeline. Once retrieved, it will map the data to the generated entity object and sink with the DB.
```kotlin
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
```

### Using your pipeline

Now that we have a pipeline defined we need to execute it. For this example we will trigger
the pipeline in an eventhandler, using the event details to populate the request.

The trade insert will have an accountNumber field, which we will use to query the accounts API.
You can see the pre-requisite code for this setup in the following files:

- [`howto-rest-tables-dictionary.kts`](server/howto-rest-app/src/main/genesis/cfg/howto-rest-tables-dictionary.kts) - table definitions for `TRADE` and `ACCOUNTS`
- [`howto-rest-view-dictionary.kts`](server/howto-rest-app/src/main/genesis/cfg/howto-rest-view-dictionary.kts) - defines the view joining the `ACCOUNTS` and `TRADE` tables together
- [`howto-rest-dataserver.kts`](server/howto-rest-app/src/main/genesis/scripts/howto-rest-dataserver.kts) - defines the queries the UI uses to populate the grids

The above setup means that when we insert a trade, if there is an account in the table with a matching accountNumber,
the view will join the two records together and the dataserver query will send this update to any subscribers.

Currently there are no accounts in the table, so we will set the pipeline up to execute and pull in relevant accounts on a trade insert.

See [`howto-rest-eventhandler.kts`](server/howto-rest-app/src/main/genesis/scripts/howto-rest-eventhandler.kts)

```kotlin
// PIPELINES:
// Firstly we must inject the PipelineManager
val pipelineManager: PipelineManager = inject()

// This is a normal eventhandler for inserting a simple trade
// We will hook our pipeline into this
eventHandler<TradeInsert>("TRADE_INSERT", transactional = true) {
    onCommit { event ->
        // Our trade table has a relation to the accounts table from our pipeline in the form of the accountNumber field
        val tradeInsert = event.details

        val pipelineName = "OPEN_API_INTEGRATION"
        // To retrieve our pipeline from the manager we must pass the request type and the name
        val pipeline = pipelineManager
            .getTypedBatchPipeline<FindAllUsingGETRequest>(pipelineName)!!

        // Here we simply insert the trade from the event as normal
        val insertedTrade = entityDb.insert(
            Trade {
                quantity = tradeInsert.quantity
                price = tradeInsert.price
                side = tradeInsert.side
                instrument = tradeInsert.instrument
                accountNumber = tradeInsert.accountNumber
            }
        ).record

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
```

### UI for pipelines

*Note: the dependencies required for the grids are listed in the UI section of [this](open-api-rest.md) readme.*

*See [components.ts](client/src/components/components.ts) for details*

That's the server ready, but in order to see any of this data we'll need a UI to display the grids.
Unlike with the request reply integration, we use dataserver queries here to keep things real-time and take advantage of the database.

See the linked dataserver file above for the queries, but here we'll show how to use them:

We have a `pipelines` page in the `client/src/routes` directory to separate our grids from the Open API REST grids.

See [`pipelines.template.ts`](client/src/routes/pipelines/pipelines.template.ts)

Our trades grid:
```tsx
<rapid-grid-pro
    persist-column-state-key="grid-pro-ssrm-column-state"
    enable-row-flashing
    enable-cell-flashing
>
    <grid-pro-server-side-datasource
        resource-name="ALL_TRADES"
        row-id="TRADE_ID"
        max-rows="30"
    ></grid-pro-server-side-datasource>
</rapid-grid-pro>
```

And the trade accounts view:
```typescript jsx
<rapid-grid-pro
    persist-column-state-key="grid-pro-ssrm-column-state"
    enable-row-flashing
    enable-cell-flashing
>
    <grid-pro-server-side-datasource
      resource-name="TRADE_ACCOUNTS_VIEW"
      row-id="TRADE_ID"
      max-rows="30"
    ></grid-pro-server-side-datasource>
</rapid-grid-pro>
```

That's it, go back to the [README](README.md) to see how to run your application.