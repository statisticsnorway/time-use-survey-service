package no.ssb.timeuse.surveyservice.respondent;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ssb.timeuse.surveyservice.appointment.Appointment;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.household.Household;
//import no.ssb.timeuse.surveyservice.purchase.Purchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RespondentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RespondentRepository repository;

    @Mock
    private RespondentService service;

    @InjectMocks
    private RespondentController controller;

    JacksonTester<RespondentRequest> respondentRequestJacksonTester;

    private static final String URL = "/v1/respondents";
    private static final UUID randomUUID = UUID.randomUUID();


    private final Respondent respondent = Respondent.builder()
            .respondentId(UUID.randomUUID())
            .email("mail")
            .name("Lars Larsen")
            .id(1L)
            .phone("982")
            .age(45)
            .relationToRecruitmentRefPerson("03")
            .household(Household.builder()
                    .ioNumber(1L)
                    .build())
            .appointments(Arrays.asList(Appointment.builder()
                    .description("avtale")
                    .build()))
//            .purchases(List.of(Purchase.builder()
//                    .id(1L)
//                    .totalSum(461.30F)
//                    .items(Collections.emptyList())
//                    .respondent(Respondent.builder()
//                            .respondentId(randomUUID)
//                            .build())
//                    .timeOfPurchase(LocalDate.now())
//                    .build()))
            .build();

    private final RespondentRequest respondentRequest = RespondentRequest.builder()
            .name("Lars Larsen")
            .phone("982")
            .email("mail")
            .age(45)
            .relationToRecruitmentRefPerson("03")
            .build();

    private RespondentResponse respondentResponse;


    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        respondent.getAppointments().get(0).setRespondent(respondent);

        respondentResponse = RespondentResponse.map(respondent);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void get_Entries_ListOfAllRespondents_ReturnOk() throws Exception {
        //given
        given(repository.findAll())
                .willReturn(List.of(respondent));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void get_FindRespondentId_IfRespondentByRespondentIdAreRequested_ReturnOk() throws Exception {
        //given
        given(repository.findByRespondentId(randomUUID))
                .willReturn(Optional.of(respondent));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/" + randomUUID))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void get_FindRespondentId_IfRespondentByRespondentIdDoesNotExist_ReturnNotFound() throws Exception {
        //given
        given(repository.findByRespondentId(randomUUID))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/" + randomUUID))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void put_UpdateRespondent_ifRespondentRequestIsValid_ReturnOk() throws Exception {
        //given
        given(service.mapToDao(any(), any()))
                .willReturn(respondent);

        given(repository.save(any()))
                .willReturn(respondent);

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(respondentRequestJacksonTester.write(respondentRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void put_UpdateRespondent_IfRespondentRequestIsInvalid_ReturnBadRequest() throws Exception {
        //given
        doThrow(ResourceValidationException.class)
                .when(service).mapToDao(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(respondentRequestJacksonTester.write(respondentRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void put_UpdateRespondent_IfRespondentIdDoesNotExist_ReturnNotFound() throws Exception {
        //given
        doThrow(ResourceNotFoundException.class)
                .when(service).mapToDao(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(respondentRequestJacksonTester.write(respondentRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void put_UpdateRespondent_IfRespondentIdDoesNotExist_ReturnNotFoun2d() throws Exception {
        //given
        doThrow(ResourceNotFoundException.class)
                .when(service).mapToDao(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/" + randomUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(respondentRequestJacksonTester.write(respondentRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
