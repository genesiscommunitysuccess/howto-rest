package global.genesis

class RestIntegrationBDD {

    fun `retrieves data from external API`() {
        // Given I define a request reply server calling my API controller
        // When I sent a request to the router for the req rep
        // Then the external API is called and retrieved data is sent as a response
    }

    fun `paginated requests send NEXT_VIEW`() {
        // Given I have a paginated API
        // When I request a page from the API
        // Then the server responds indicating the next logical page to request
    }

    fun `server responds with NEXT_VIEW = -1 when there is no more data`() {
        // Given I have a paginated API
        // When I request the last page
        // Then the server responds with NEXT_VIEW = -1, indicating there is no more data
    }

    fun `responds with max_rows results even when server filtering is applied`() {
        // Given I request 50 rows from the API
        // When I have criteria filtering the results
        // Then:
        // the server will request the remaining rows from the API
        // the server will cache the read rows from extra pages
        // and the server will respond with 50 rows and indicate the next logical page to request

        // Example:
        // Client requests 50 rows from page 1 where the balance is greater than 10.00
        // Server retrieves 50 rows from page 1 but filters it to 30 rows (20 rows have balances less than 10.00)
        // The server will then request page 2 and try make up the missing (20) rows
        // Page 2 has 20 valid rows, so the server adds them to the response
        // The server will cache page 2 as having the first 20 rows read
        // The response will indicate the next page to request is page 2
        // When the client then requests page 2, the server will only read 30 rows as the first 20 have already been sent
        // So the server makes up the remaining 20 rows by requesting page 3 and indicating the next logical page to request is 3
    }

    fun `handles max rows being set greater than total rows`() {
        // Given I set max rows above the number of rows in the API
        // When I request max rows
        // Then the server returns all available rows and indicates there is no more data (NEXT_VIEW = -1)
    }
}