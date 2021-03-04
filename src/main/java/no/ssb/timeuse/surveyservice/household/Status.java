package no.ssb.timeuse.surveyservice.household;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
        /**
         * recruitment codes
         */
        RECRUITED("01"),
        STARTED("02"),
        NOT_STARTED("03"),
        FINISHED("FINISHED"),

        /**
         * drop-out codes
         */
        NO_TIME("11"),
        NO_WISH_TO_PARTICIPATE("12"),
        HARD_REFUSAL("13"),
        REFUSAL_BY_PROXY("14"),
        NO_CONSENT("15"),
        SHORT_TERM_ILLNESS("21"),
        LONG_TERM_ILLNESS("22"),
        PROBLEMS_IN_CLOSE_FAMILY("23"),
        LANGUAGE_PROBLEM("24"),
        TEMPORARILY_UNAVAILABLE_WORK("25"),
        TEMPORARILY_UNAVAILABLE_VACATION("26"),
        LACK_COMPUTER_INTERNET("27"),
        LACK_PHONE_NUMBER("44"),
        NO_CONTACT("45"),
        LACK_PHONE_NUMBER_AND_EMAIL("46"),
        LACK_EMAIL("47"),
        OTHER_DROPOUT_REASON("71"),

        /**
         * tranfer codes
         */
        IMPARTIAL_INTERVIEWER("81"),
        CAPACITY_PROBLEMS("82"),

        /**
         * avgang (departure) codes
         */
        DEATH("91"),
        RESIDENCE_ABROAD("92"),
        RESIDENCE_INSTITUTION("93"),
        NO_PERMANENT_RESIDENCE("94"),
        OTHER_DEPARTURE_REASON("99");

        String value;

        String getValue() {
            return value;
        }
}
