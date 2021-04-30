package no.ssb.timeuse.surveyservice.searchTerm;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.exception.ResourceExistsException;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.searchterm.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SearchTermControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchTermRepository repository;

    @Mock
    private ActivityCategoryRepository activityCategoryRepository;

    @Mock
    private SearchTermService service;

    @InjectMocks
    private SearchTermController controller;

    private JacksonTester<SearchTermRequest> searchTermRequestJson;
    private JacksonTester<List<SearchTermRequest>> searchTermRequestListJson;

    private final static String URL = "/v1/search-terms";

    private final ActivityCategory activity = ActivityCategory.builder()
            .code("1.1.1")
            .id(3L)
            .description("test data")
            .level(3)
            .build();

    private final SearchTerm searchTerm = SearchTerm.builder()
            .id(1L)
            .text("testdata")
            .activity(activity)
            .build();

    private final SearchTermRequest searchTermRequest = SearchTermRequest.builder()
            .activityCode("1.1.1")
            .text("testdata searchTermRequest")
            .build();

    private final List<SearchTerm> listOfSearchTerms = List.of(searchTerm);

    private final List<SearchTermRequest> listOfSearchTermRequest = List.of(searchTermRequest);

    @Captor
    ArgumentCaptor<List<SearchTerm>> searchTermsToSave;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }



    @Test
    public void get_Entries_IfSearchTermsByActivityCodeAreRequested_ReturnOK() throws Exception {
        //given
        given(repository.findByActivityCode(anyString()))
                .willReturn(listOfSearchTerms);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void get_Entries_IfSearchTermsByTextAreRequested_ReturnOK() throws Exception {
        //given
        given(repository.findByTextStartsWithIgnoreCase(anyString()))
                .willReturn(listOfSearchTerms);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void get_SearchById_IfSearchTermDoesExist_ReturnOk() throws Exception {
        //given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(searchTerm));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void get_SearchById_IfSearchTermDoesNotExist_ReturnNotFound() throws Exception {
        //given
        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void get_SearchById_IfSearchTermDoesNotExistException_ReturnNotFound() throws Exception {
        //given
        doThrow(ResourceNotFoundException.class)
                .when(repository).findById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(get(URL + "/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }



    @Test
    public void update_UpdateSearchTerm_IfSearchTermDoesNotExist_ReturnNotFound() throws Exception {
        //given
        doThrow(ResourceNotFoundException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();


        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void update_UpdateSearchTerm_IfDuplicateExists_ReturnConflict() throws Exception {
        //given
        doThrow(ResourceExistsException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void update_UpdateSearchTerm_IfInvalidActivityCode_ReturnBadRequest() throws Exception {
        //given
        doThrow(ResourceValidationException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void update_UpdateSearchTerm_ifRequestIsValid_ReturnOK() throws Exception {
        //given
        given(service.save(any(), any()))
                .willReturn(searchTerm);

        //when
        MockHttpServletResponse response = mockMvc.perform(put(URL+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void delete_DeleteSearchTerm_IfRequestIsValid_ReturnOK() throws Exception {
        //given
        given(repository.findById(anyLong()))
                .willReturn(Optional.of(searchTerm));

        doNothing().when(repository)
                .deleteById(anyLong());


        //when
        MockHttpServletResponse response = mockMvc.perform(delete(URL+"/4"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void delete_DeleteSearchTerm_IfIdIsDoesNotExist_ReturnNotFound() throws Exception {
        //given
        doThrow(EmptyResultDataAccessException.class)
                .when(repository).deleteById(anyLong());


        //when
        MockHttpServletResponse response = mockMvc.perform(delete(URL+"/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void post_CreateNewSearchTerm_IfRequestIsValid_ReturnCreated() throws Exception {
        //given
        given(service.save(any(), any()))
                .willReturn(searchTerm);

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void post_CreateNewSearchTerm_IfSearchTermDoesNotExist_ReturnNotFound() throws Exception {
        //given
        doThrow(ResourceNotFoundException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL+"/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();


        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void post_CreateNewSearchTerm_IfDuplicateExists_ReturnConflict() throws Exception {
        //given
        doThrow(ResourceExistsException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL+"/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void post_CreateNewSearchTerm_IfInvalidActivityCode_ReturnBadRequest() throws Exception {
        //given
        doThrow(ResourceValidationException.class)
                .when(service).save(any(), any());

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL+"/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestJson.write(searchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void post_CreateNewSearchTermBatch_IfListOfRequestIsValid_ReturnCreated() throws Exception {
        //given
        given(repository.saveAll(any()))
                .willReturn(listOfSearchTerms);

        //when
        MockHttpServletResponse response = mockMvc.perform(post(URL + "/create-batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(searchTermRequestListJson.write(listOfSearchTermRequest).getJson()))
                .andReturn()
                .getResponse();

        //then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());
    }

}
