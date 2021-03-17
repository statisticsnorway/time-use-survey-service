package no.ssb.timeuse.surveyservice;

import no.ssb.timeuse.surveyservice.communicationlog.CommunicationLogEntry;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Direction;
import no.ssb.timeuse.surveyservice.communicationlog.enums.Type;
import no.ssb.timeuse.surveyservice.household.Household;
//import no.ssb.timeuse.surveyservice.purchase.Purchase;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommonMocks {

    public static final Respondent respondent = Respondent.builder()
            .respondentId(UUID.randomUUID())
            .email("mail")
            .name("Lars Larsen")
            .id(1L)
            .phone("982")
//            .purchases(List.of(Purchase.builder()
//                    .id(1L)
//                    .totalSum(461.30F)
//                    .items(Collections.emptyList())
//                    .respondent(Respondent.builder()
//                            .respondentId(UUID.randomUUID())
//                            .build())
//                    .timeOfPurchase(LocalDate.now())
//                    .build()))
            .build();

    public static final Household household = Household.builder()
            .id(1L)
            .ioNumber(1L)
            .address("jernbanegata")
            .city("kongsvinger")
            .diaryEnd(LocalDate.now())
            .diaryStart(LocalDate.now())
            .mainRespondentId(UUID.randomUUID())
            .referenceWeek(1)
            .statusSurvey("STARTED")
            .respondents(List.of(respondent))
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


}
