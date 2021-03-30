package no.ssb.timeuse.surveyservice.codelist;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;

@AllArgsConstructor
public class CodeList {

    public static Map<String, CodeInfo> status = Map.ofEntries(
            /**
             * recruitment codes
             */
            entry("FINISHED", new CodeInfo("Ferdig/avsluttet", Arrays.asList(Status.SURVEY, Status.DIARY, Status.QUESTIONNAIRE))),
            entry("STARTED", new CodeInfo("Påbegynt", Arrays.asList(Status.SURVEY, Status.DIARY, Status.QUESTIONNAIRE))),
            entry("NOT_STARTED", new CodeInfo("Ikke påbegynt", Arrays.asList(Status.RECRUITMENT, Status.SURVEY, Status.DIARY, Status.QUESTIONNAIRE))),

            entry("INTERVIEWED", new CodeInfo("Intervjuet", Arrays.asList(Status.SURVEY))),
            entry("LOGININFO_SENT", new CodeInfo("Innloggingsinformasjon sendt", Arrays.asList(Status.SURVEY))),

            entry("01", new CodeInfo("Rekruttert", Arrays.asList(Status.RECRUITMENT))),
            entry("02", new CodeInfo("Påbegynt", Arrays.asList(Status.RECRUITMENT))),
            entry("03", new CodeInfo("Ikke påbegynt", Arrays.asList(Status.RECRUITMENT))),

            /**
             * drop-out codes
             */
            entry("11", new CodeInfo("Ikke tid", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("12", new CodeInfo("Ønsker ikke å delta", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("13", new CodeInfo("Hard nekt, trusler", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("14", new CodeInfo("Andre nekter for IO", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("15", new CodeInfo("Gir ikke samtykke", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("21", new CodeInfo("Kortvarig sykdom", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("22", new CodeInfo("Langvarig sykdom, svekkelse, psykisk utv hemmet ", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("23", new CodeInfo("Sykdom/dødsfall i familien, annen uforutsett hendelse", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("24", new CodeInfo("Språkproblemer", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("25", new CodeInfo("Midlertidig fraværende på grunn av skolegang/arbeid", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("26", new CodeInfo("Midlertidig fraværende på grunn av ferie ol.", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("27", new CodeInfo("Ikke internett, datamaskin el.", Arrays.asList(Status.SURVEY))),
            entry("44", new CodeInfo("Mangler kun  telefonnummer/feil telefonnummer", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("45", new CodeInfo("Ingen kontakt", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("46", new CodeInfo("Mangler både telefonnummer og epost", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("47", new CodeInfo("Mangler kun epost", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("71", new CodeInfo("Annen frafallsgrunn", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),

            /**
             * tranfer codes
             */
            entry("81", new CodeInfo("Intervjuer kjenner IO", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("82", new CodeInfo("Kapasitetsproblemer, sykdom ol hos intervjuer", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),

            /**
             * avgang (departure) codes
             */
            entry("91", new CodeInfo("Død", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("92", new CodeInfo("Bosatt i utlandet minst 6 mnd og i hele datafangsperioden", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("93", new CodeInfo("Bosatt i institusjon", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("94", new CodeInfo("Har ikke fast bopel", Arrays.asList(Status.SURVEY, Status.RECRUITMENT))),
            entry("99", new CodeInfo("Andre avgangsgrunner", Arrays.asList(Status.SURVEY, Status.RECRUITMENT)))

    );
}
