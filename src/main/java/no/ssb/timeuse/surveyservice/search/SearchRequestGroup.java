package no.ssb.timeuse.surveyservice.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = SearchRequestDeserializer.class)
public class SearchRequestGroup {
    Map<String, List<String>> predicates;
    LocalDate diaryStartFrom;
    LocalDate diaryStartTo;
}
