package no.ssb.timeuse.surveyservice;

import no.ssb.timeuse.surveyservice.activitiy.ActivityCategory;
import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
//import no.ssb.timeuse.surveyservice.purchase.Purchase;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.RespondentRequest;
import no.ssb.timeuse.surveyservice.searchterm.SearchTerm;
import no.ssb.timeuse.surveyservice.searchterm.SearchTermRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommonMocks {

    public static final Respondent respondent = Respondent.builder()
            .respondentId(UUID.randomUUID())
            .id(1L)
            .ioNumber(1L)
            .name("Lars Larsen")
            .phone("982")
            .email("mail")
            .address("jernbanegata")
            .city("kongsvinger")
            .diaryEnd(LocalDate.now())
            .diaryStart(LocalDate.now())
            .statusSurvey("STARTED")//            .purchases(List.of(Purchase.builder()
//                    .id(1L)
//                    .totalSum(461.30F)
//                    .items(Collections.emptyList())
//                    .respondent(Respondent.builder()
//                            .respondentId(UUID.randomUUID())
//                            .build())
//                    .timeOfPurchase(LocalDate.now())
//                    .build()))
            .build();

    public static final RespondentRequest respondentRequest = RespondentRequest.builder()
            .name("Lars Larsen")
            .phone("982")
            .email("mail")
            .age(45)
            .build();

    public static final List<CommunicationLogEntry> listOfCommunicationLogEntries = List.of(
            CommunicationLogEntry.builder()
                    .id(1L)
                    .direction(Direction.OUTGOING)
                    .type(Type.SMS)
                    .respondent(Respondent.builder()
                            .phone("22334455")
                            .build())
                    .message("You've got SMS!")
                    .build(),

            CommunicationLogEntry.builder()
                    .id(2L)
                    .direction(Direction.OUTGOING)
                    .type(Type.EMAIL)
                    .respondent(Respondent.builder()
                            .email("e-mail@address.com")
                            .build())
                    .message("This email is for you!")
                    .build(),

            CommunicationLogEntry.builder()
                    .id(3L)
                    .direction(Direction.INCOMING)
                    .type(Type.EMAIL)
                    .respondent(Respondent.builder()
                            .email("e-mail@address.com")
                            .build())
                    .message("This email is for SSB!")
                    .build(),

            CommunicationLogEntry.builder()
                    .id(4L)
                    .direction(Direction.OUTGOING)
                    .type(Type.PHONE)
                    .respondent(Respondent.builder()
                            .email("e-mail@address.com")
                            .build())
                    .message("Call to SSB!")
                    .build()
    );

    public static final ActivityCategory activity = ActivityCategory.builder()
            .code("1.1.1")
            .id(1L)
            .description("test data")
            .level(3)
            .helpText("help text")
            .build();

    public static final SearchTermRequest searchTermRequest = SearchTermRequest.builder()
            .text("Test searchTermRequest")
            .activityCode("1.1.1")
            .build();

    public static final SearchTerm searchTerm = SearchTerm.builder()
            .id(1L)
            .text("Test searhTerm")
            .activity(activity)
            .build();




}
