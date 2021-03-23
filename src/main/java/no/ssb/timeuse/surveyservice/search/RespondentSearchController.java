package no.ssb.timeuse.surveyservice.search;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.respondent.RespondentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/respondents/search")
@AllArgsConstructor
public class RespondentSearchController {

    private final RespondentSearchService service;

    @CrossOrigin
    @PostMapping("/search-specific")
    public ResponseEntity<?> search(@RequestBody HashMap<String, String> request) {
        return new ResponseEntity<>(service.search(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> searchSpecific(@RequestBody SearchRequest searchRequest) {
        return new ResponseEntity<>(service.searchRespondentSpecific(searchRequest), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/group")
    public ResponseEntity<?> searchGroup(@RequestBody SearchRequestGroup searchRequestGroup) {
        log.info("gruppesøk: {}", searchRequestGroup);
        List<RespondentResponse> list = service.findRespondentsByGroupFilter(searchRequestGroup);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
