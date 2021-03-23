package no.ssb.timeuse.surveyservice.search;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRequestDeserializer extends StdDeserializer<no.ssb.timeuse.surveyservice.search.SearchRequestGroup> {

    public SearchRequestDeserializer() {
        super(no.ssb.timeuse.surveyservice.search.SearchRequestGroup.class);
    }

    protected SearchRequestDeserializer(Class<?> vc) {
        super(vc);
    }

    protected SearchRequestDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected SearchRequestDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public no.ssb.timeuse.surveyservice.search.SearchRequestGroup deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
         return no.ssb.timeuse.surveyservice.search.SearchRequestGroup.builder().predicates(buildPredicateMap(rootNode))
                .from(rootNode.get("from") != null ? LocalDate.parse(rootNode.get("from").asText()) : null)
                .to(rootNode.get("to") != null ? LocalDate.parse(rootNode.get("to").asText()) : null)
                .build();
    }

    private Map<String, List<String>> buildPredicateMap(JsonNode node) {
        Map<String, List<String>> predicateMap = new HashMap<>();

        node.fieldNames().forEachRemaining( (field) -> {

            if(!field.equals("from") && !field.equals("to")) {
                ArrayNode orNode = node.withArray(field);
                List<String> values = new ArrayList<>();

                orNode.elements().forEachRemaining(element -> {
                    values.add(element.asText());
                });
                predicateMap.put(field, values);
            }
        });

        return predicateMap;
    }
}
