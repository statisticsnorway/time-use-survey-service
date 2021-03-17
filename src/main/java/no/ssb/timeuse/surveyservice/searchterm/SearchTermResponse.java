package no.ssb.timeuse.surveyservice.searchterm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;


@Data
@AllArgsConstructor
public class SearchTermResponse {
        final Long id;
        final String text;
        final String code;
        @JsonSerialize(as = ActivityCategory.class)
        final ActivityCategory activity;

    public static SearchTermResponse map(SearchTerm from) {
            return new SearchTermResponse(from.getId(), from.getText(), from.getActivity().getCode(), from.getActivity());
    }
}
