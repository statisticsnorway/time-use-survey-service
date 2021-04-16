package no.ssb.timeuse.surveyservice.diary;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import no.ssb.timeuse.surveyservice.activitiy.ActivityCategoryRepository;
import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import no.ssb.timeuse.surveyservice.respondent.RespondentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Component
@Slf4j
public class DiaryService {

    final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    final long diaryStartHoursPastMidnight = 4;
    private final ActivityCategoryRepository activityCategoryRepository;
    private final RespondentRepository respondentRepository;
    private final DiaryRepository diaryRepository;

    public DiaryService(ActivityCategoryRepository activityCategoryRepository
            , RespondentRepository respondentRepository
            , DiaryRepository diaryRepository) {
        this.activityCategoryRepository = activityCategoryRepository;
        this.respondentRepository = respondentRepository;
        this.diaryRepository = diaryRepository;
    }

    public List<Diary> map(DiaryRequest from, UUID respondentId) {
        List<Diary> diaries = new ArrayList<>();

        val respondent = respondentRepository.findByRespondentId(respondentId)
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with ID " + respondentId + " does not exist"));

        from.diaryActivities.stream().forEach(da -> {
            Diary diary = Diary.builder()
                    .respondent(respondent)
                    .startTime(LocalDateTime.parse(da.getStartTime(), dateFormat))
                    .endTime(LocalDateTime.parse(da.getEndTime(), dateFormat))
                    .activity(da.getActivity())
                    .biActivity(da.getBiActivity())
                    .travelType(da.getTravelType())
                    .company(da.getCompany())
                    .createdDate(da.getCreatedTime() != null ? LocalDateTime.parse(da.getCreatedTime(), dateFormat) : LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

            if (da.getActivityCode() != null) {
                val activityCategory = activityCategoryRepository.findByCode(da.getActivityCode())
                        .orElseThrow(() -> new ResourceValidationException("Activitycode " + da.getActivityCode() + " is invalid"));
                diary.setActivityCategory(activityCategory);
            }
            if (da.getActivityCode() != null) {
                val activityCategory = activityCategoryRepository.findByCode(da.getBiActivityCode())
                        .orElseThrow(() -> new ResourceValidationException("Activitycode " + da.getBiActivityCode() + " is invalid"));
                diary.setBiActivityCategory(activityCategory);
            }

            diaries.add(diary);
        });
        return diaries;

    }

//    public void tags(List<Diary> diaryList) {
//        ArrayList<String> activityCategoryList = new ArrayList<>();
//        diaryList.forEach(i -> {
//            activityCategoryList.add(i.getActivityCategory().getCode().substring(0, 2));
//        });
//
//        String mostCommonTag = activityCategoryList.stream()
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
//                .entrySet().stream().max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey).orElse(null);
//
//        System.out.println("TEST :::: MOST COMMON ACTIVITY = " + activityCategoryRepository.findByCode(mostCommonTag));
//    }

    public String diaryStatus(UUID respondentId) {
        val respondent = respondentRepository.findByRespondentId(respondentId)
                .orElseThrow(() -> new ResourceNotFoundException("Respondent with ID " + respondentId + " does not exist"));

        List<Diary> diaryActivities = diaryRepository.findByRespondentId(respondentId);
        Collections.sort(diaryActivities, Comparator.comparing(Diary::getStartTime));

        LocalDateTime diaryActivitiesStart = diaryActivities.get(0).getStartTime();
        LocalDateTime prevEnd = diaryActivities.get(0).getEndTime();

        if (diaryActivities.size() <= 1) return "NOT_STARTED";
        if (diaryActivitiesStart.isAfter(respondent.getDiaryStart().atStartOfDay().plusHours(diaryStartHoursPastMidnight))) {
            return "STARTED";
        }
        if (diaryActivities.get(diaryActivities.size() - 1).getEndTime().plusMinutes(1).isAfter(
                respondent.getDiaryEnd().atStartOfDay().plusHours(diaryStartHoursPastMidnight))) {
            return "STARTED";
        }

        String loopStatus = "FINISHED";
        for (int i = 1; i < diaryActivities.size(); i++) {
            if (diaryActivities.get(i).getStartTime().isAfter(prevEnd.plusMinutes(1))) {
                loopStatus = "STARTED";
                break;
            }
            prevEnd = diaryActivities.get(i).getEndTime();
        }
        return loopStatus;
    }

}
