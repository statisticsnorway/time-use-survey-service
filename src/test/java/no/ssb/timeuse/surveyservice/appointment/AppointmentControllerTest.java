package no.ssb.timeuse.surveyservice.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ssb.timeuse.surveyservice.appointment.*;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentControllerTest {

    private static final String URL = "/v1/appointments";

    private MockMvc mockMvc;

    @Mock
    AppointmentRepository repository;

    @Mock
    AppointmentService service;

    @Mock
    RespondentRepository respondentRepository;

    @InjectMocks
    AppointmentController controller;

    private JacksonTester<AppointmentRequest> appointmentJson;

    public static final Appointment appointment = Appointment.builder()
            .id(1L)
            .appointmentTime(LocalDateTime.now())
            .createdBy("Obi-Wan Kenobi")
            .assignedTo("Darth Wader")
            .description("Final battle")
            .build();

    public static final AppointmentRequest appointmentRequest = AppointmentRequest.builder()
            .appointmentTime(LocalDateTime.now())
            .createdBy("Obi-Wan Kenobi")
            .assignedTo("Darth Wader")
            .description("Final battle")
            .build();

    public static final List<Appointment> appointmentList = List.of(appointment);


    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    /*
    @Test
    public void getEntries_ShoudReturnOk() throws Exception {
        //given
        given(repository.findAll())
                .willReturn(appointmentList);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    // TODO: Cant't get this to work. It returns 400 instead of 201
    //@Test
    public void createAppointment_IfResopondentIoIdNumberExists_ShoudReturnCreated() throws Exception {
        //given
        given(respondentRepository.findByIoNumber(anyLong()))
                .willReturn(Optional.of(CommonMocks.respondent));
        given(repository.save(appointment))
                .willReturn(appointment);

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(appointmentJson.write(appointmentRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());
    }

     */


}
