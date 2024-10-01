package rest_integration.pages.homepage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    private final Locator nextPageButton;
    private final Locator previousPageButton;
    private final Locator firstPageButton;
    private final Locator lastPageButton;

    public HomePage(Page page) {
        this.nextPageButton = page.getByLabel("Next Page");
        this.previousPageButton = page.getByLabel("Previous Page");
        this.firstPageButton = page.getByLabel("First Page");
        this.lastPageButton = page.getByLabel("Last Page");
    }

    public void clickButton(String button) {
        switch (button) {
            case "Next Page":
                nextPageButton.click();
                break;
            case "Previous Page":
                previousPageButton.click();
                break;
            case "First Page":
                firstPageButton.click();
                break;
            case "Last Page":
                lastPageButton.click();
                break;
            default:
                throw new RuntimeException("Invalid button name");
        }
    }
}
