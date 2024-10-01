package rest_integration.utilities.grid_utilities;

import com.microsoft.playwright.ElementHandle;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static rest_integration.utilities.playwright_driver.PlaywrightDriver.getPage;

/**
 * Utility class for handling grid operations.
 */
public class GridUtil {

    /**
     * Extracts and returns data from a grid as a list of maps, with each map representing a row.
     *
     * @param last_column_id Column ID to ignore when extracting data.
     * @return List of maps containing the grid data with column IDs as keys and cell values as values.
     */
    public static List<Map<String, String>> getGridListMap(String last_column_id) {
        getPage().evaluate("document.body.style.zoom = '40%'");

        List<Map<String, String>> mapList = new LinkedList<>();
        final String rowSelector = "div.ag-center-cols-container >> div.ag-row";
        final String cellSelector = "div.ag-cell-value";

        getPage().waitForTimeout(5000);
        List<ElementHandle> rows = getPage().querySelectorAll(rowSelector);

        for (ElementHandle row : rows) {
            Map<String, String> map = new LinkedHashMap<>();
            List<ElementHandle> cells = row.querySelectorAll(cellSelector);

            for (ElementHandle cell : cells) {
                String outerHTML = cell.evaluate("element => element.outerHTML").toString();
                String textContent = cell.textContent();

                if (!textContent.isEmpty()) {
                    String columnId = columnIdExtractor(outerHTML);
                    map.put(columnId, textContent);

                    if (last_column_id != null && last_column_id.equals(columnId)) {
                        break;
                    }
                }
            }

            if (!map.isEmpty()) {
                mapList.add(new LinkedHashMap<>(map));
            }
        }

        return mapList;
    }


    /**
     * Extracts the column ID from the outer HTML of a grid cell.
     *
     * @param outerHTML the outer HTML of the grid cell
     * @return the column ID extracted from the outer HTML
     */
    private static String columnIdExtractor(String outerHTML) {
        String patternString = "col-id=\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(outerHTML);
        String colId = null;
        if (matcher.find()) {
            colId = matcher.group(1);
        }
        return colId;
    }
}
