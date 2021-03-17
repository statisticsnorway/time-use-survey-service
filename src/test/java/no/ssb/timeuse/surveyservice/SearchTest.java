package no.ssb.timeuse.surveyservice;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchTest {

    /* SEARCH SERVICE
    * API:
    * The API is a simple HashMap where keys refer to searchable fields (defined in queryOrder), and values are query strings.
    * We chose to use a HashMap instead of a DTO to simplify the code and make search more flexible.
    *
    * SERVICE:
    * First, undefined fields (not in queryOrder) and empty query strings are removed. The map is sorted by the priority set in queryOrder
    * before the top key is queried in the database using a Native Query (SELECT * FROM RESPONDENT WHERE <key> = <query string>).
    * Subsequently, if the database returns a result set, each row will be converted to a HashMap and added to a List<HashMap<String, String>>.
    * Every remaining query will be used to reduce the list before it is converted to response objects and returned to the API caller.
    * */

    @Test
    @DisplayName("Ett eller annet")
    public void nameIsTor() {
        val criteria = generateCriteria("Tor", "12");
        criteria.put("ignored", "value");

        val queryOrder = List.of(
                "fdat",
                "name");

        val orderedQueries = criteria.entrySet().stream()
                .filter((k) -> queryOrder.contains(k.getKey())) // Remove keys that are not defined
                .filter((k) -> k.getValue() != null && !k.getValue().isEmpty()) // Remove empty queries
                .sorted(Comparator.comparingInt(e -> queryOrder.indexOf(e.getKey()))) // Sort by priority
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, // Collect in an ordered LinkedHashMap
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        orderedQueries.forEach((k, v) -> System.out.println(k + " Value: " + v));


    }

    private static HashMap<String, String> generateCriteria(String name, String fdat) {
        return new HashMap<>(Map.of("name", name, "fdat", fdat));
    }


}
