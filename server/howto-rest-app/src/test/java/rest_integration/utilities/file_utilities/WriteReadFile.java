package rest_integration.utilities.file_utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class WriteReadFile {
    private static final Logger LOG = LoggerFactory.getLogger(WriteReadFile.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJSON(Path filePath, @NotNull Map<String, Object> trades) {
        try {
            Files.createDirectories(filePath.getParent());
            try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
                fileWriter.write(objectMapper.writeValueAsString(trades));
                LOG.info("JSON written successfully to file: {}", filePath);
            }
        } catch (IOException e) {
            LOG.error("Error writing JSON to file: {}", filePath, e);
            throw new RuntimeException(e);
        }
    }

    public static void writeJsonArrayFile(Path filePath, List<Map<String, String>> data) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (Map<String, String> row : data) {
            ObjectNode objectNode = mapper.createObjectNode();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                objectNode.put(entry.getKey(), entry.getValue());
            }
            arrayNode.add(objectNode);
        }

        try {
            Path path = filePath.getParent();
            if (path != null) {
                Files.createDirectories(path);
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), arrayNode);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file", e);
        }
    }

    protected static void removeDirectory(String filePath) {
        try {
            Path path = Paths.get(filePath).getParent();
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}