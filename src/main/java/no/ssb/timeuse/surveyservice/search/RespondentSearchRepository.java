package no.ssb.timeuse.surveyservice.search;

import no.ssb.timeuse.surveyservice.respondent.Respondent;

import java.util.List;

public interface RespondentSearchRepository {
    List<Respondent> searchForRespondents(SearchRequestGroup groupRequest);
}
