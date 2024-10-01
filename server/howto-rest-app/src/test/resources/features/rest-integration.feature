Feature: Rest Integration Features
#
#  Background: Loads the data
#    Given User send the event "insert-all-accounts"

  @API
  Scenario Outline: Rest API Pagination Tests
    Given User connect with username "admin" and password "genesis"
    When User sends request to "req-accounts-api" with "<queryParam>"
    Then User compares it to expected "<expected>", "ACCOUNT_NUMBER" and, "SOURCE_REF"
    Examples:
      | expected                                                                                                                       | queryParam                                            |
      | src/test/resources/result/expected/RestIntegration/req-accounts-api_BROKERID=1&DETAILS.MAX_ROWS=200&DETAILS.VIEW_NUMBER=1.json | BROKERID=1&DETAILS.MAX_ROWS=200&DETAILS.VIEW_NUMBER=1 |
      | src/test/resources/result/expected/RestIntegration/req-accounts-api_BROKERID=1&DETAILS.MAX_ROWS=100&DETAILS.VIEW_NUMBER=1.json | BROKERID=1&DETAILS.MAX_ROWS=100&DETAILS.VIEW_NUMBER=1 |
      | src/test/resources/result/expected/RestIntegration/req-accounts-api_BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=1.json  | BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=1  |
      | src/test/resources/result/expected/RestIntegration/req-accounts-api_BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=2.json  | BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=2  |
      | src/test/resources/result/expected/RestIntegration/req-accounts-api_BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=3.json  | BROKERID=1&DETAILS.MAX_ROWS=50&DETAILS.VIEW_NUMBER=3  |

  @UI
  Scenario: GUI Pagination Tests
    Given User enters username "admin" and password "genesis"
    Then User should see the grid
      | src/test/resources/result/expected/RestIntegration/page_number_1.json |
    When User click on the "Next Page"
    Then User should see the grid
      | src/test/resources/result/expected/RestIntegration/page_number_2.json |
    When User click on the "Next Page"
    Then User should see the grid
      | src/test/resources/result/expected/RestIntegration/page_number_3.json |
    When User click on the "Next Page"
    Then User should see the grid
      | src/test/resources/result/expected/RestIntegration/page_number_4.json |