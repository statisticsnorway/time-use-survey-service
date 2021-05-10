package no.ssb.timeuse.surveyservice.templates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateResponse {
    Long id;
    String name;
    String text;
    String type;
    String description;
    String createdBy;
    LocalDateTime createdTime;

    public static TemplateResponse map(Template from) {
        log.info("template.name: {}", from.getName());
        return new TemplateResponse(
                from.getId(),
                from.getName(),
                from.getText(),
                from.getType(),
                from.getDescription(),
                from.getCreatedBy(),
                from.getCreatedTime());
    }


}

