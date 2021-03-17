package no.ssb.timeuse.surveyservice.activity;

import lombok.val;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRequest;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryService;
import no.ssb.timeuse.surveyservice.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ActivityCategoryServiceTest {

    @Mock
    private ActivityCategoryRepository repository;

    @InjectMocks
    private ActivityCategoryService service;

    private final ActivityCategory activityCategory = ActivityCategory.builder()
            .id(1L)
            .code("01.1.1.1")
            .description("Test data")
            .helpText("Test data")
            .level(4)
            .build();

    private final ActivityCategoryRequest activityCategoryRequest = ActivityCategoryRequest.builder()
            .description("test data")
            .helpText("test data")
            .build();


    @Test
    public void saveWhenActivityIsValid() {
        repository.save(activityCategory);

        verify(repository).save(any());
    }

    @Test
    public void throwExceptionWhenActivityCodeIsInvalid() {
        val activity = activityCategory;
        activity.setCode("1.1.1.1");

        Throwable exception = catchThrowable(() -> service.validateActivity(activity));

        assertThat(exception).isInstanceOf(ResourceValidationException.class);
    }

    @Test
    public void throwExceptionWhenActivityDescriptionExists() {
        given(repository.findByDescription(anyString()))
                .willReturn(Optional.of(activityCategory));

        Throwable exception = catchThrowable(() -> service.checkForActivityDuplicate(activityCategory));

        assertThat(exception).isInstanceOf(ResourceExistsException.class);

    }
}
