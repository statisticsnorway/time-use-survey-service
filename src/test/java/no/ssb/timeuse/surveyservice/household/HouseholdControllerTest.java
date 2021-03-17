package no.ssb.timeuse.surveyservice.household;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.ssb.timeuse.surveyservice.codelist.CodeList;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HouseholdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HouseholdRepository repository;
    @Mock
    private HouseholdService service;
    @Mock
    private CodeList codeList;

    @InjectMocks
    private HouseholdController controller;

    private JacksonTester<HouseholdRequest> householdJson;

    private final Household household = Household.builder()
            .id(1L)
            .referenceWeek(2)
            .mainRespondentId(UUID.randomUUID())
            .ioNumber(4L)
            .statusSurvey(CodeList.status.get("STARTED").getValue())
            .statusRecruitment(CodeList.status.get("01").getValue())
            .statusDiary(CodeList.status.get("03").getValue())
            .statusQuestionnaire(CodeList.status.get("01").getValue())
            .diaryStart(LocalDate.now())
            .diaryEnd(LocalDate.now())
            .address("X")
            .postcode("Y")
            .city("Z")
            .recruitmentStart(LocalDateTime.now())
            .recruitmentEnd(LocalDateTime.now())
            .recruitmentMinutesSpent(50)
            .acceptedInitialDiaryStart(TRUE)
            .householdSize(3)
            .respondents(Collections.emptyList())
            .build();

    private final HouseholdRequest householdRequest = HouseholdRequest.builder()
            .referenceWeek(2)
            .mainRespondentId(UUID.randomUUID())
            .ioNumber(4L)
            .statusSurvey("STARTED")
            .statusRecruitment("STARTED")
            .statusDiary("STARTED")
            .statusQuestionnaire("NOT_STARTED")
            .diaryStart(LocalDate.of(2000, 1, 1))
            .diaryEnd(LocalDate.of(2001, 1, 1))
            .address("X")
            .postcode("Y")
            .city("Z")
            .recruitmentStart(LocalDateTime.now())
            .recruitmentEnd(null)
            .recruitmentMinutesSpent(50)
            .acceptedInitialDiaryStart(TRUE)
            .householdSize(3)
            .build();

    private static final String URL = "/v1/households";

    @BeforeEach
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonTester.initFields(this, mapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void get_Entries_ListOfHouseholds_ReturnOk() throws Exception {
        //given
        given(repository.findAll())
                .willReturn(List.of(household));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void put_UpdateHousehold_IfRequestAndIdIsValid_ReturnOk() throws Exception {
        //given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(household));

        given(repository.save(any()))
                .willReturn(household);

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(householdJson.write(householdRequest).getJson()))
                .andReturn()
                .getResponse();


        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void put_UpdateHousehold_IfHouseholdIdIsNotFound_ReturnNotFound() throws Exception {
        //given

        System.out.println(householdJson.write(householdRequest).getJson());
        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(householdJson.write(householdRequest).getJson()))
                .andReturn()
                .getResponse();


        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
