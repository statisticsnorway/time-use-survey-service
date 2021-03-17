package no.ssb.timeuse.surveyservice.communicationlog.contactio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static no.ssb.timeuse.surveyservice.communicationlog.enums.Type.SMS;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactIoConsumer {

    @Value("${contact-io.url}")
    private String url;

    private final WebClient client;

    public List<Long> call(List<CommunicationLogEntry> entries) {
        try {
            return client.post()
                    .uri(url)
                    .body(BodyInserters.fromValue(entries.stream()
                            .map(ContactIoConsumer::toDto)
                            .collect(Collectors.toList())))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Long.class)
                    .collectList()
                    .block();
        } catch(WebClientResponseException e) {
            log.error("Caught error from ContactIO API: " + e.getStatusCode());
            return Collections.emptyList();
        }
    }

    private static ContactIoDto toDto(CommunicationLogEntry entry) {
        if(entry.getType() == SMS) {
            return ContactIoDto.builder()
                    .requestId(entry.getId().toString())
                    .type("sms")
                    .to(entry.getRespondent().getPhone())
                    .message(entry.getMessage())
                    .build();
        } else  {
            return ContactIoDto.builder()
                    .requestId(entry.getId().toString())
                    .type("email")
                    .from("service-contact-io@ssb.no")
                    .to(entry.getRespondent().getEmail())
                    .message(entry.getMessage())
                    .build();
        }
    }
}
