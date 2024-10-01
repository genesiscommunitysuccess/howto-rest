package rest_integration.utilities.file_utilities;

import java.nio.file.Path;

public class FileConstants {
    private static final String feature = "RestIntegration";

    public static Path generateJSONFilePath(String expectedOrActual, String filename) {
        return Path.of("src/test/resources/result/" + expectedOrActual + "/" + feature + "/" + filename + ".json");
    }
}