package no.ssb.timeuse.surveyservice.codelist;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeListTest {

    @Test
    void testOptionalOfNullableAgainstCodeListGetValue() {
        assertThat(Optional.ofNullable(CodeList.status.get("NOT_STARTED")).map(e -> e.getValue()).orElse("No match"))
                .isEqualTo("Ikke påbegynt");

        assertThat(Optional.ofNullable(CodeList.status.get("PARROT")).map(e -> e.getValue()).orElse("No match"))
                .isEqualTo("No match");
    }

}
