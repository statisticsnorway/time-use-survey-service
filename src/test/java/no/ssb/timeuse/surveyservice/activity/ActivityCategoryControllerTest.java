package no.ssb.timeuse.surveyservice.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryController;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryResponse;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryService;
import no.ssb.timeuse.surveyservice.exception.ResourceExistsException;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//TODO fix tests
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActivityCategoryControllerTest {

    private final static String URL = "/v1/activity-categories/";

    private MockMvc mockMvc;

    @Mock
    private ActivityCategoryRepository repository;

    @Mock
    private ActivityCategoryService service;

    @InjectMocks
    private ActivityCategoryController controller;

    private JacksonTester<ActivityCategory> activityJson;

    private final ActivityCategory activity = ActivityCategory.builder()
            .code("01.1.1.1")
            .id(1L)
            .description("test data")
            .level(4)
            .helpText("help text")
            .build();

    private final ActivityCategory activityEmptyDescription = ActivityCategory.builder()
            .code("01.1.1.1")
            .id(1L)
            .description("")
            .level(4)
            .build();

    private final List<ActivityCategory> listOfActivitys = List.of(activity);


    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void get_Entries_findAllActivity_ReturnOk() throws Exception {
        given(repository.findAll())
                .willReturn(List.of(activity));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void post_CreateNewActivity_IfActivityIsValid_ReturnCreated() throws Exception {
        //given
        given(repository.save(activity))
                .willReturn(activity);

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    public void post_CreateNewActivity_IfActivityByDescriptionExists_ShouldReturnConflict() throws Exception {
        //given
        doThrow(ResourceExistsException.class)
                .when(service).checkForActivityDuplicate(any());

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void post_CreateNewActivity_IfActivityIsInvalid_ReturnBadRequest() throws Exception {
        doThrow(ResourceValidationException.class)
                .when(service).checkForActivityDuplicate(any());

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void put_UpdateActivity_IfActivityIsValid_ReturnOk() throws Exception {
        //given
        given(repository.findByCode(anyString()))
                .willReturn(Optional.of(activity));

        given(repository.findByDescription(anyString()))
                .willReturn(Optional.empty());

        given(repository.save(any()))
                .willReturn(activity);

        //when
        String result = mockMvc.perform(put(URL + "01.1.1.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper ob = new ObjectMapper();
        ActivityCategoryResponse response = ob.readValue(result, ActivityCategoryResponse.class);

        Assertions.assertTrue(response.getCode().equals(activity.getCode()));
        Assertions.assertTrue(response.getDescription().equals(activity.getDescription()));
        Assertions.assertTrue(response.getHelpText().equals(activity.getHelpText()));
    }

    @Test
    public void put_UpdateActivity_IfActivityCodeDoesNotExist_ReturnNotFound() throws Exception {
        //given
        given(repository.findByCode(anyString()))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "01.1.1.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void put_UpdateActivity_IfActivityByDescriptionAlreadyExists_ReturnConflict() throws Exception {
        //given
        given(repository.findByCode(anyString()))
                .willReturn(Optional.of(activity));

        given(repository.findByDescription(anyString()))
                .willReturn(Optional.of(activity));

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "01.1.1.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void put_UpdateActivity_IfActivityIsInvalid_ReturnBadRequest() throws Exception {
        given(repository.findByCode(anyString()))
                .willReturn(Optional.of(activity));

        given(repository.findByDescription(anyString()))
                .willReturn(Optional.empty());

        doThrow(ResourceValidationException.class)
                .when(service).validateActivity(any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL + "01.1.1.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(activityJson.write(activity).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    public void delete_DeleteActivity_IfIdExists_ReturnOk() throws Exception {
        //given

        //when
        MockHttpServletResponse response = mockMvc.perform(delete(URL + "1" ))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
    }
}
