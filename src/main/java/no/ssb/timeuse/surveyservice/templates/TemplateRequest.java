package no.ssb.timeuse.surveyservice.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TemplateRequest {
    private String name;
    private String type;
    private String text;
    private String description;
    private String createdBy;
    private LocalDateTime createdTime;
}
