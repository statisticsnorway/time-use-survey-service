package no.ssb.timeuse.surveyservice.diary;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.exception.ValidationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1")
public class DiaryController {
    private final DiaryRepository diaryRepository;
    private final DiaryService diaryService;

    @GetMapping ("/diaries")
    public List<DiaryResponse> entries() {
        return diaryRepository.findAll().stream()
                .map(DiaryResponse::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/respondents/{respondent-id}/diaries")
    public List<DiaryResponse> entriesByRespondentId(@PathVariable(value = "respondent-id") Optional<UUID> respondentId) {
        try {
            return diaryRepository.findByRespondentId(respondentId.get()).stream()
                    .map(DiaryResponse::map)
                    .collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/respondents/{respondent-id}/diaries")
    public ResponseEntity<DiaryRespondentResponse> createNewDiary(@PathVariable(value = "respondent-id") Optional<UUID> respondentId,
                                                              @RequestBody DiaryRequest diaryRequest) {
        try {
            //TODO: sjekke om det er greit å slette alle før en lagrer alle aktiviteter for en respondent
            diaryRepository.deleteByRespondentId(respondentId.orElse(null));

            List<Diary> respondentDiaries = diaryService.map(diaryRequest, respondentId.orElse(null));
            return new ResponseEntity<>(DiaryRespondentResponse.map(diaryRepository.saveAll(respondentDiaries)), HttpStatus.CREATED);
        } catch (ValidationException e) {
            throw new ResourceValidationException(e.getMessage());
        }
    }

    //TODO: Vil vi noen gang ha behov for å lagre én aktivitet?
//    @PutMapping("/respondents/{respondent-id}/diarys/{id}")
//    public DiaryResponse updateDiary(@PathVariable(value = "respondent-id") Optional<UUID> respondentId,
//                                           @PathVariable(value = "id") Long id,
//                                           @RequestBody DiaryRequest diaryRequest) {
//        try {
//            val diary = repository.findById(id)
//                    .orElseThrow(() -> new ResourceNotFoundException("Diary with id " + id + " does not exist"));
//
//            Diary diaryToSave = diaryService.map(diaryRequest, respondentId.orElse(null));
//            diaryToSave.setId(id);
//            diaryToSave.setModifiedDate(LocalDateTime.now());
//            diaryToSave.setCreatedDate(diary.getCreatedDate());
//
//            return DiaryResponse.map(repository.save(diaryToSave));
//        } catch (ValidationException e) {
//            throw new ResourceValidationException(e.getMessage());
//        }
//    }

    @DeleteMapping("/respondents/{respondent-id}/diaries")
    public void deleteDiary(@PathVariable(value = "respondent-id") Optional<UUID> respondentId) {
        try {
            diaryRepository.deleteByRespondentId(respondentId.orElse(null));
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/respondents/{respondent-id}/diaries/{id}")
    public void deleteDiary(@PathVariable(value = "respondent-id") Optional<UUID> respondentId,
                               @PathVariable(value = "id") Long id) {
        try {
            diaryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
