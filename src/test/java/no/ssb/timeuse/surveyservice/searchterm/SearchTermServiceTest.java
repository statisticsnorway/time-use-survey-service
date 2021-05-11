package no.ssb.timeuse.surveyservice.searchterm;

import lombok.val;
import no.ssb.timeuse.surveyservice.CommonMocks;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.exception.ResourceExistsException;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SearchTermServiceTest {

    @Mock
    private SearchTermRepository repository;

    @Mock
    private ActivityCategoryRepository activityCategoryRepository;

    @InjectMocks
    private SearchTermService service;

    @Captor
    private ArgumentCaptor<SearchTerm> capturedSearchTerm;

    private final ActivityCategory activityCategory = CommonMocks.activity;

    private final SearchTermRequest searchTermRequest = CommonMocks.searchTermRequest;

    private final SearchTerm searchTerm = CommonMocks.searchTerm;

    @Test
    public void mapToDao_searchTermRequestIsValidAndSearchTermIdIsNull() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        //when
        repository.save(service.mapToDao(searchTermRequest, Optional.empty()));

        //then
        assertThat(repository.findByTextIgnoreCase(searchTermRequest.getText())).isNotNull();
    }

    @Test
    public void mapToDao_searchTermRequestIsValidAndSearchTermIdIsPresent() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        given(repository.findById(anyLong()))
                .willReturn(Optional.of(searchTerm));

        //when
        repository.save(service.mapToDao(searchTermRequest, Optional.of(anyLong())));

        //then
        verify(repository).save(capturedSearchTerm.capture());

        assertThat(capturedSearchTerm.getValue().getActivity()).isNotNull();
    }

    @Test
    public void mapToDao_searchTermRequestIsValidAndSearchTermIdIsNotValid() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        given(repository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        Throwable exception = catchThrowable(() -> repository.save(service.mapToDao(searchTermRequest, Optional.of(1L))));

        //then
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void findDuplicates_whenDuplicateExists() {
        //given
        given(repository.findByTextIgnoreCase(anyString()).stream()
                .map(SearchTerm::getActivity)
                .anyMatch(d -> d.getCode().equals(searchTermRequest.getActivityCode())))
        .willThrow(ResourceExistsException.class);

        //when
        Throwable exception = catchThrowable(() -> repository.findByTextIgnoreCase(anyString()).stream().map(SearchTerm::getActivity)
                .anyMatch(d -> d.getCode().equals(searchTermRequest.getActivityCode())));

        //then
        assertThat(exception).isInstanceOf(ResourceExistsException.class);
    }

    @Test
    public void validateActivityCode_whenActivityCodeIsNotValid() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.empty());

        //when
        Throwable exception = catchThrowable(() -> service.validateActivityCode(""));

        //then
        assertThat(exception).isInstanceOf(ResourceValidationException.class);
    }

    @Test
    public void validateActivityCode_whenActivityCodeIsValid() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        String activityCode = "01.1.1.1";

        //when
        boolean result = service.validateActivityCode(activityCode);

        //then
        assertTrue(result);
    }


    @Test
    public void save_whenSearchTermRequestIsValid() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        val specificRequest = searchTermRequest;
        searchTermRequest.setActivityCode("01.1.1.1");

        //when
        service.save(specificRequest, Optional.empty());

        //then
        verify(repository).save(capturedSearchTerm.capture());

        assertThat(capturedSearchTerm.getValue().getActivity()).isNotNull();
    }

    @Test
    public void save_whenSearchTermRequestIsNotValid_notValidActivity() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.empty());

        val specificRequest = searchTermRequest;
        specificRequest.setActivityCode("01.1.1.1");

        //when
        Throwable exception = catchThrowable(() -> service.save(specificRequest, Optional.empty()));

        //then
        assertThat(exception).isInstanceOf(ResourceValidationException.class);
    }

    @Test
    public void save_whenSearchTermRequestIsNotValid_duplicate() {
        //given
        given(activityCategoryRepository.findByCode(anyString()))
                .willReturn(Optional.of(activityCategory));

        given(repository.findByTextIgnoreCase(anyString()).stream()
                .map(SearchTerm::getActivity)
                .anyMatch(d -> d.getCode().equals(searchTermRequest.getActivityCode())))
                .willThrow(ResourceExistsException.class);

        val specificRequest = searchTermRequest;
        specificRequest.setActivityCode("01.1.1.1");

        //when
        Throwable exception = catchThrowable(() -> service.save(specificRequest, Optional.empty()));

        //then
        assertThat(exception).isInstanceOf(ResourceExistsException.class);
    }
}
