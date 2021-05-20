package no.ssb.timeuse.surveyservice.respondent.diarystarthistory;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/diary-start-histories")
public class DiaryStartHistoryController {

    private final DiaryStartHistoryRepository repository;
    private final RespondentRepository respondentRepository;


    @GetMapping
    public List<DiaryStartHistoryResponse> entries(@RequestParam(required = false) Optional<UUID> respondentId) {
        log.info("respondentId: {}", respondentId.isPresent());
       if (respondentId.isPresent()) {
            return repository.findByRespondentRespondentId(respondentId.get())
                    .stream()
                    .map(DiaryStartHistoryResponse::map)
                    .collect(Collectors.toList());
        }
        return repository.findAll()
                .stream()
                .map(DiaryStartHistoryResponse::map)
                .collect(Collectors.toList());
    }


    @PostMapping
    public ResponseEntity<?> createNewDiaryStartHistory(@RequestBody DiaryStartHistoryRequest request) {
        log.info("request: {}", request.toString());
        DiaryStartHistory p = convertToDiaryStartHistory(request);
        return new ResponseEntity<>(DiaryStartHistoryResponse.map(repository.save(p)), HttpStatus.CREATED);
    }

    final DiaryStartHistory convertToDiaryStartHistory(DiaryStartHistoryRequest request) {
        val respondent = respondentRepository.findByRespondentId(request.getRespondentId());

        DiaryStartHistory diaryStartHistory = DiaryStartHistory.builder()
                .respondent(respondent.get())
                .diaryStart(request.getDiaryStart())
                .createdBy(request.getCreatedBy())
                .build();

        log.info("diaryStartHistory converted: {}", diaryStartHistory.toString());
        return diaryStartHistory;
    }

}



