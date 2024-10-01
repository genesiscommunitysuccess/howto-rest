package global.genesis.cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import rest_integration.utilities.api_utitilites.events.accounts.Accounts;
import rest_integration.utilities.api_utitilites.events.authentication.EventLoginAuth;
import rest_integration.utilities.api_utitilites.reqrep.ReqRep;

import java.nio.file.Path;
import java.util.Map;

import static rest_integration.utilities.api_utitilites.constants.Authentication.SESSION_AUTH_TOKEN;
import static rest_integration.utilities.api_utitilites.constants.Authentication.USERNAME;
import static rest_integration.utilities.file_utilities.CompareJSON.compareJSONFiles;
import static rest_integration.utilities.file_utilities.FileConstants.generateJSONFilePath;
import static rest_integration.utilities.file_utilities.WriteReadFile.writeJSON;

public class RestIntegrationAPITest {
    private String endpoint;
    private String queryParam;
    private Response response;

    @Given("User connect with username {string} and password {string}")
    public void user_connect_with_username_and_password(String username, String password) {
        EventLoginAuth eventLoginAuth = new EventLoginAuth(username, password);
        Response response = eventLoginAuth.post();
        SESSION_AUTH_TOKEN = response.jsonPath().get("SESSION_AUTH_TOKEN");
        USERNAME = username;
    }

    @When("User send the event {string}")
    public void userSendsTheEvent(String endpoint) {
        Accounts accounts = new Accounts("accounts?pageIndex=0&limit=100");
        Accounts insertAllAccounts = new Accounts(endpoint);
        JsonPath accountsJSON =  accounts.get().jsonPath();
        if (accountsJSON.getList("accounts").isEmpty()){
            insertAllAccounts.post();
        }
    }

    @When("User sends request to {string} with {string}")
    public void userSendsRequestToWith(String endpoint, String queryParam) {
        this.queryParam = queryParam;
        this.endpoint = endpoint;
        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();
    }

    @Then("User compares it to expected {string}, {string} and, {string}")
    public void user_compares_it_to_expected(String result, String primary_key, String keys_to_ignore) {
        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();
        Map<String, Object> expected = response.jsonPath().get("");
        Path filePath = generateJSONFilePath("actual", endpoint + "_" + queryParam);
        writeJSON(filePath, expected);
        compareJSONFiles(result, String.valueOf(filePath), primary_key, keys_to_ignore);
    }
}