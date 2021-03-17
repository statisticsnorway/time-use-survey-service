package no.ssb.timeuse.surveyservice.search;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/v1/households/search")
@AllArgsConstructor
public class HouseholdSearchController {

    private final HouseholdSearchService service;

    @CrossOrigin
    @PostMapping("/search-specific")
    public ResponseEntity<?> search(@RequestBody HashMap<String, String> request) {
        return new ResponseEntity<>(service.search(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> searchSpecific(@RequestBody SearchRequest searchRequest) {
        return new ResponseEntity<>(service.searchHouseholdSpecific(searchRequest), HttpStatus.OK);
    }
}
