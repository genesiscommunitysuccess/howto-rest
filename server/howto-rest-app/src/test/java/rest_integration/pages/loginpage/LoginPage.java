package rest_integration.pages.loginpage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static rest_integration.utilities.api_utitilites.constants.Authentication.USERNAME;
import static rest_integration.utilities.config_utilities.ConfigReader.readProperty;

public class LoginPage {
    private final Page page;
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameField = page.locator("[data-test-id=username] input");
        this.passwordField =  page.locator("[data-test-id=password] input");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    }

    public void validateURL(String url) {
        assertThat(page).hasURL(readProperty("defaultHost") + url);
    }

    public void login(String username, String password) {
        USERNAME = username;
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }
}
