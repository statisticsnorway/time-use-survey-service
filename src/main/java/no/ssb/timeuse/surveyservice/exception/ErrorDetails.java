package no.ssb.timeuse.surveyservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;
    private final HttpStatus status;
}
