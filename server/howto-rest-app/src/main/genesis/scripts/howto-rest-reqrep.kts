import global.genesis.api.accounts.api.AccountControllerApi
import global.genesis.api.accounts.api.AccountControllerApi.FindAllUsingGETRequest
import global.genesis.api.accounts.schema.AccountDTO
import global.genesis.httpclient.GenesisHttpClient

/**
 * This file defines request-replies of the application.
 * Request-Replies provide snapshot data from a table or view in response to a request from the front end.
 * Once the response is received, the transaction is over (unlike a Data Server, which stays connected to the client and pushes updates)

 * Full documentation on request server may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/snapshot-queries-request-server/
 */

// OPEN_API
// To use the generated code we must instantiate the API class
val accountsApi = AccountControllerApi("http://localhost:8080")
// Configure authorisation - the authorisation method you will use will depend on your external API.
// Remember, you can externalise your secrets using Genesis system definitions. Follow the link below for more details
// and checkout the HashiCorp Vault section to understand how to integrate with your secrets manager
// https://docs.genesis.global/docs/develop/server-capabilities/runtime-configuration/system-definition/
accountsApi.registerApiToken("Authorization", "Basic YWRtaW46YWRtaW4=")

requestReplies {
  // OPEN_API
  // Define a custom request reply with the generated classes as the request and reply types
  requestReply<FindAllUsingGETRequest, AccountDTO>("ACCOUNTS_API") {
    maxRetries = 5 // If your API has a rate limit, you can limit the number of retries here
    replyList { request ->
      try {
        // OPEN_API
        // Thanks to the generated types we can call our API directly with the request parameter
        accountsApi
          .findAllUsingGET(request)
          .accounts
      } catch (e: Exception) {
        LOG.warn("Error sending request for accounts to external API", e)
        emptyList()
      }
    }
  }

  // REST
  // When integrating with an API without an OpenAPI specification,
  // we can achieve type safety by defining our own data classes
  data class AccountsRequest(
    val customerId: Int,
    val brokerId: Int
  )

  data class Account(
    val accountNumber: Int,
    val balance: Double,
    val brokerId: Int,
    val customerId: Int,
    val owner: String,
  )

  data class AccountsResponse(
    val accounts: List<Account>,
    val limit: Int,
    val pageIndex: Int,
    val totalElements: Int,
    val totalPages: Int,
  )

  // REST
  // In this example we will be using the GenesisHttpClient
  // You may use any client you wish
  val client = GenesisHttpClient()

  // We define a custom req rep with the data classes from above
  requestReply<AccountsRequest, Account>("RAW_ACCOUNTS") {
    replyList { request ->
      // Build your request as needed
      client.get<AccountsResponse> {
        url = "http://localhost:8080/accounts"
        queries(
          "brokerId" to request.brokerId.toString(),
          "customerId" to request.customerId.toString(),
        )
      }.data.accounts
    }
  }
}

