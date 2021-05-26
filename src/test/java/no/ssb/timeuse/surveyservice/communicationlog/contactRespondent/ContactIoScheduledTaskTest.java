package no.ssb.timeuse.surveyservice.communicationlog.contactRespondent;

import no.ssb.timeuse.surveyservice.CommonMocks;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogRepository;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContactIoScheduledTaskTest {

    @Mock
    private CommunicationLogRepository repository;

    @Mock
    private ContactRespondentConsumer contactIo;

    @InjectMocks
    private ContactRespondentScheduledTask task;

    @Captor
    private ArgumentCaptor<List<CommunicationLogEntry>> capturedEntry;

    private final List<CommunicationLogEntry> mockedEntries = CommonMocks.listOfCommunicationLogEntries;

    @Test
    public void run_WhenUnsentEntriesArePresent_sendAndMarkAsSent() {
        // given four unsent entries
        given(repository.findAllByConfirmedSentIsNull())
                .willReturn(mockedEntries);

        given(contactIo.call(any()))
                .willReturn(List.of(1L, 2L));

        given(repository.findAllByIdIn(any()))
                .willReturn(mockedEntries.subList(0, 2));

        // when
        task.run();

        // then filter keeps SMS/EMAIL that are OUTGOING (2 entries) and excludes the rest (2 entries)
        verify(contactIo).call(capturedEntry.capture());

        assertThat(capturedEntry.getValue().stream()
                .filter(entry -> (!(entry.getDirection() == Direction.OUTGOING) && (entry.getType() == Type.EMAIL || entry.getType() == Type.SMS)))
                .collect(Collectors.toList()))
                .isEmpty();

        // and 2 entries are returned from service
        verify(repository).saveAll(capturedEntry.capture());

        assertThat(capturedEntry.getValue().stream()
                .filter(entry -> entry.getConfirmedSent() != null)
                .collect(Collectors.toList()))
                .size().isEqualTo(2);
    }
}
