package no.ssb.timeuse.surveyservice.search;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        log.info("diaryStartFrom: {}", rootNode.get("diaryStartFrom").asText());
        SearchRequestGroup searchRequestGroup = SearchRequestGroup.builder()
                 .diaryStartFrom(dateOf(rootNode, "diaryStartFrom"))
                 .diaryStartTo(dateOf(rootNode, "diaryStartTo"))
                 .maxResults(rootNode.get("maxResults") != null ? (rootNode.get("maxResults")).asInt() : 10000)
                .build();
        log.info("før searchRequestGroup: {}", searchRequestGroup);
        Map<String, List<String>> predicates = buildPredicateMap(rootNode);
        log.info("predicates: {}", predicates);
        searchRequestGroup.setPredicates(predicates);
        log.info("return searchRequestGroup: {}", searchRequestGroup);
        return searchRequestGroup;
    }

    private LocalDate dateOf(JsonNode rootNode, String dateField) {
        JsonNode dateNode = rootNode.get(dateField);
        log.info("dateField {} = {}, of type {}", dateField, dateNode, rootNode.get(dateField) != null ? dateNode.getNodeType().name() : " tom");
//
//        if (dateNode.getNodeType().name().equals("OBJECT")) {
//            log.info("dateNode is OBJECT");
//            dateNode.fields().forEachRemaining(field -> log.info("{} = {}", field.getKey(), field.getValue()));
//            log.info("year: {}, month: {}, date: {}", dateNode.get("year"), dateNode.get("monthValue"), dateNode.get("dayOfMonth"));
//            log.info("new Date: {}", LocalDate.of(dateNode.get("year").asInt(), dateNode.get("monthValue").asInt(), dateNode.get("dayOfMonth").asInt()));
//        }

        LocalDate date = ((dateNode == null || dateNode.getNodeType().name().equals("NULL")) ? null :
                (dateNode.getNodeType().name().equals("STRING") ?
                        LocalDate.parse(dateNode.asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : LocalDate.of(dateNode.get("year").asInt(), dateNode.get("monthValue").asInt(), dateNode.get("dayOfMonth").asInt())
                ));
        log.info("date to be returned: {}", date);
        return date;
    }

    private Map<String, List<String>> buildPredicateMap(JsonNode node) {
        log.info("build predicate map from node {}", node);
        Map<String, List<String>> predicateMap = new HashMap<>();

        node.fieldNames().forEachRemaining( (field) -> {
            log.info("field: {} = {}", field, node.get(field));
            if (field.equals("predicates")) {
                JsonNode predicateNode = node.get(field);
                log.info("predicateNodes: {}", predicateNode);
                predicateNode.fieldNames().forEachRemaining( (predField) -> {
                    log.info("predField: {}", predField);
                    List<String> values = predicateMapValue(predicateNode, predField);
                    log.info("values: {}", values);
                    predicateMap.put(predField, values);
                });
            } else
            if (!field.equals("diaryStartFrom") && !field.equals("diaryStartTo") && !field.equals("maxResults")) {
                List<String> values = predicateMapValue(node, field);
                predicateMap.put(field, values);
            }
        });

        log.info("returner predicateMap:");
        predicateMap.forEach((k,v) -> log.info("key: {}, value: {}", k,v));

        return predicateMap;
    }

    private List<String> predicateMapValue(JsonNode node, String field) {
        ArrayNode orNode = node.withArray(field);
        List<String> values = new ArrayList<>();

        orNode.elements().forEachRemaining(element -> {
            values.add(element.asText());
        });
        return values;
    }


}
