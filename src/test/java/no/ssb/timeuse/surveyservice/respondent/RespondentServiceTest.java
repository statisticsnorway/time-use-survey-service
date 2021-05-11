package no.ssb.timeuse.surveyservice.respondent;


import lombok.val;
import no.ssb.timeuse.surveyservice.CommonMocks;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RespondentServiceTest {

    @Mock
    private RespondentRepository repository;

    @InjectMocks
    private RespondentService service;

    @Captor
    private ArgumentCaptor<List<Respondent>> capturedListOfRespondents;

    private final RespondentRequest respondentRequest = CommonMocks.respondentRequest;
    private final Respondent respondent = CommonMocks.respondent;

    @Test
    public void validateRespondent_respondentRequestIsNotValid() {
        //given
        val invalidRequest = respondentRequest;
        invalidRequest.setName("");

        //when
        Throwable exception = catchThrowable(() -> service.validateRespondent(invalidRequest));

        //then
        assertThat(exception).isInstanceOf(ResourceValidationException.class);
    }


    @Test
    public void mapToDao_respondentIsValidButRespondentIdIsNotFound() {
        //given
        UUID randomUUID = UUID.randomUUID();

        given(repository.findByRespondentId(randomUUID))
                .willReturn(Optional.empty());

        //when
        Throwable exception = catchThrowable(() -> service.mapToDao(respondentRequest, Optional.of(randomUUID)));

        //then
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
        assertThat(exception.toString()).isEqualTo("no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException: Respondent with respondentId " + randomUUID + " does not exist");
    }



}
