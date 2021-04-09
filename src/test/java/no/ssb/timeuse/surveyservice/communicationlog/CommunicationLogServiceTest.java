package no.ssb.timeuse.surveyservice.communicationlog;

import lombok.val;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Category;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommunicationLogServiceTest {
    @Mock
    private RespondentRepository respondentRepository;

    @Mock
    private CommunicationLogRepository repository;

    @InjectMocks
    private CommunicationLogService service;

    @Captor
    private ArgumentCaptor<List<CommunicationLogEntry>> capturedCommunicationLogEntries;

    @Test
    public void save_IfRequestedRespondentIdsAreFound_ReturnCommunicationLogEntriesForAll() {
        // given
        val requestedRespondentId = Set.of(UUID.randomUUID(), UUID.randomUUID());

        given(respondentRepository.findAllByRespondentIdIn(any()))
                .willReturn(generatedRespondents(requestedRespondentId));

        given(repository.saveAll(any()))
                .willReturn(any());

        // when
        service.save(generatedRequest(requestedRespondentId));
        verify(repository).saveAll(capturedCommunicationLogEntries.capture());

        // then
        assertThat(capturedCommunicationLogEntries.getValue().size())
                .isEqualTo(2);
    }

    @Test
    public void save_IfRequestedRespondentIdsAreNotFound_ThrowExceptionContainingMissingRespondentIds() {
        // given
        val requestedRespondentId = new HashSet<>(Set.of(UUID.randomUUID(), UUID.randomUUID()));

        given(respondentRepository.findAllByRespondentIdIn(any()))
                .willReturn(generatedRespondents(requestedRespondentId));

        val missingRespondentId = UUID.randomUUID();
        requestedRespondentId.add(missingRespondentId);

        // when
        Throwable exception = catchThrowable(() -> service.save(generatedRequest(requestedRespondentId)));

        // then
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception).hasMessage("One or several respondentIDs was not found: " + List.of(missingRespondentId).toString());
    }

    private static List<Respondent> generatedRespondents(Set<UUID> respondentIds) {
        return respondentIds.stream()
                .map(requested -> Respondent.builder()
                        .respondentId(requested)
                        .name("testname")
                        .ioNumber(1L)
                        .build())
                .collect(Collectors.toList());
    }

    private static CommunicationLogEntryRequest generatedRequest(Set<UUID> respondentIds) {
        return CommunicationLogEntryRequest.builder()
                .respondentId(respondentIds)
                .category(Category.APPOINTMENT)
                .createdBy("B")
                .direction(Direction.INCOMING)
                .message("D")
                .scheduled(LocalDateTime.now())
                .type(Type.PHONE)
                .build();
    }

}
