package no.ssb.timeuse.surveyservice.communicationlog.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import net.minidev.json.JSONObject;
import no.ssb.timeuse.surveyservice.search.SearchRequestGroup;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduledCommunicationService {
    private final ScheduledCommunicationRepository repository;

    public ScheduledCommunication save(ScheduledCommunicationRequest request) {

        String criteria = criteriaToString(request.getRespondentCriteria());
        log.info("criteria: {}", criteria);

        val scheduledCommunication = ScheduledCommunication.builder()
                .respondentCriteria(criteria)
                .direction(request.getDirection())
                .type(request.getType())
                .category(request.getCategory())
                .message(request.getMessage())
                .createdBy(request.getCreatedBy())
                .createdTime(request.getCreatedTime())
                .scheduled(request.getScheduled())
                .communicationTriggered(LocalDateTime.now())
                .build();
        log.info("scheduledCommunication to be saved: {}", scheduledCommunication );
        ScheduledCommunication saved = repository.save(scheduledCommunication);
        log.info("saved schedComm: {}", saved);
        return repository.save(scheduledCommunication);
    }

    private String criteriaToString(SearchRequestGroup respondentCriteria) {
        try {
            log.info("convert {}", respondentCriteria);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            log.info("respondentCriteria as string: {}", objectMapper.writeValueAsString(respondentCriteria));
            return objectMapper.writeValueAsString(respondentCriteria);
        } catch (JsonProcessingException e) {
            log.error("Error json-stringify map {}", respondentCriteria);
        }
        return "";
    }

}
