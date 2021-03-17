package no.ssb.timeuse.surveyservice.search;

import lombok.Value;

//TODO skal også kunne søke basert på føringsperiode, epost og telefonnummer, id-nummer
@Value
public class Person {
    String firstName;
    String lastName;
    String fdat;
}
