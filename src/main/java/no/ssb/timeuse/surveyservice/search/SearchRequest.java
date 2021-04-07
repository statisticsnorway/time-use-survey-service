package no.ssb.timeuse.surveyservice.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    String telephone;
    String email;
    String name;
    Long ioNumber;
}
