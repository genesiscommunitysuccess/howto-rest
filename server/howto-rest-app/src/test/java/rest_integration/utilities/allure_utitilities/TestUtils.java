package rest_integration.utilities.allure_utitilities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import static rest_integration.utilities.api_utitilites.constants.Authentication.USERNAME;
import static rest_integration.utilities.playwright_driver.PlaywrightDriver.getPage;


public class TestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);


    public static void captureFullPage(String filePath) {
        getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        LOG.info("Finished captureFullPage with filePath: {}", filePath);
    }

    public static void captureElement(String locator, String filePath) {
        Locator element = getPage().locator(locator);
        element.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(filePath)));
        LOG.info("Finished captureElement with locator: {} and filePath: {}", locator, filePath);
    }

    public static void addAttachment() {
        if (Files.exists(Paths.get("src/test/resources/result/" + USERNAME + "/actual"))) {
            try {
                Optional<Path> latestFilePath = Files.list(Paths.get("src/test/resources/result/" + USERNAME + "/actual"))
                        .filter(Files::isRegularFile)
                        .max(Comparator.comparingLong(p -> p.toFile().lastModified()));

                if (latestFilePath.isPresent()) {
                    Path filePath = latestFilePath.get();
                    Allure.attachment(filePath.getFileName().toString(), new String(Files.readAllBytes(filePath)));
                }
            } catch (IOException e) {
                LOG.error("Error adding attachment to allure report", e);
            }
        }
    }

    // This method is used to add a screenshot to the allure report
    public static void addScreenshot(@NotNull Scenario scenario) {
        String screenshotPath = "build/screenshots/" + scenario.getName() + ".png";
        captureFullPage(screenshotPath);
        try (InputStream is = Files.newInputStream(Paths.get(screenshotPath))) {
            Allure.attachment(scenario.getName() + ".png", is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
