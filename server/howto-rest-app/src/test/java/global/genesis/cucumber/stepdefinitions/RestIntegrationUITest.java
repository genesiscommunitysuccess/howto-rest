package global.genesis.cucumber.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rest_integration.pages.homepage.HomePage;
import rest_integration.pages.loginpage.LoginPage;

import java.nio.file.Path;

import static rest_integration.utilities.file_utilities.CompareJSON.compareJSONFiles;
import static rest_integration.utilities.file_utilities.FileConstants.generateJSONFilePath;
import static rest_integration.utilities.file_utilities.WriteReadFile.writeJsonArrayFile;
import static rest_integration.utilities.grid_utilities.GridUtil.getGridListMap;
import static rest_integration.utilities.playwright_driver.PlaywrightDriver.getPage;

public class RestIntegrationUITest {
    private Integer pageNumber = 1;
    private static final String primary_key = "ACCOUNT_NUMBER";

    @When("User enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        final LoginPage loginPage = new LoginPage(getPage());
        loginPage.validateURL("login");
        loginPage.login(username, password);
    }

    @Then("User should see the grid")
    public void userShouldSeeTheTrades(String expectedFile) {
        Path actualFile = generateJSONFilePath("actual", "page_number_" + pageNumber);
        writeJsonArrayFile(actualFile, getGridListMap(null));
        compareJSONFiles(expectedFile, String.valueOf(actualFile), primary_key, null);
    }

    @And("User click on the {string}")
    public void userClickOnTheButton(String button) {
        HomePage homePage = new HomePage(getPage());
        homePage.clickButton(button);
        pageNumber++;
    }
}
