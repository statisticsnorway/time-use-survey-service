package no.ssb.timeuse.surveyservice.search;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public SearchRequestGroup deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
        log.info("parse json: {}", rootNode);
         return SearchRequestGroup.builder().predicates(buildPredicateMap(rootNode))
                 .diaryStartFrom(rootNode.get("diaryStartFrom") != null ? LocalDate.parse(rootNode.get("diaryStartFrom").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                 .diaryStartTo(rootNode.get("diaryStartTo") != null ? LocalDate.parse(rootNode.get("diaryStartTo").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                 .maxResults(rootNode.get("maxResults") != null ? (rootNode.get("maxResults")).asInt() : 10000)

                .build();
    }

    private Map<String, List<String>> buildPredicateMap(JsonNode node) {
        log.info("build predicate map from node {}", node);
        Map<String, List<String>> predicateMap = new HashMap<>();

        node.fieldNames().forEachRemaining( (field) -> {
            if(!field.equals("diaryStartFrom") && !field.equals("diaryStartTo") && !field.equals("maxResults")) {
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
