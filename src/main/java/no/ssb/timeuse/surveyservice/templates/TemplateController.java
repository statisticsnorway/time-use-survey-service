package no.ssb.timeuse.surveyservice.templates;

import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import no.ssb.timeuse.surveyservice.exception.ResourceNotFoundException;
import no.ssb.timeuse.surveyservice.exception.ResourceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@AllArgsConstructor
@Timed
@RequestMapping("/v1/templates")
public class TemplateController {

    private final TemplateRepository repository;


    @GetMapping
    public List<TemplateResponse> entries(@RequestParam(required = false) Optional<String> name,
                                             @RequestParam(required = false) Optional<String> type) {
        log.info("name: {}, type: {}", name.isPresent(), type.isPresent());
       if (name.isPresent()) {
            return repository.findAllByNameContainingIgnoreCase(name.get())
                    .stream()
                    .map(TemplateResponse::map)
                    .collect(Collectors.toList());
        }
        if (type.isPresent()) {
            return repository.findAllByType(type.get())
                    .stream()
                    .map(TemplateResponse::map)
                    .collect(Collectors.toList());
        }
        return repository.findAll()
                .stream()
                .map(TemplateResponse::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TemplateResponse templateById(@PathVariable Long id) {
        return repository.findById(id)
                .map(TemplateResponse::map)
                .orElseThrow(() -> new ResourceNotFoundException("Template with id " + id + " does not exist"));
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Template with id " + id + " does not exist");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateResponse> updateTemplate(@PathVariable(value = "id") Long id,
                                                           @RequestBody TemplateRequest request) {
        if (repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Template with id " + id + " does not exist");
        }
        return new ResponseEntity<>(TemplateResponse.map(repository.save(convertToTemplate(Optional.of(id), request))), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createNewTemplate(@RequestBody TemplateRequest request) {
        log.info("request: {}", request.toString());
        Template p = convertToTemplate(Optional.empty(), request);
        return new ResponseEntity<>(TemplateResponse.map(repository.save(p)), HttpStatus.CREATED);
    }

    final Template convertToTemplate(Optional<Long> id, TemplateRequest request) {

        Template template = Template.builder()
                .name(request.getName())
                .type(request.getType())
                .text(request.getText())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .createdTime(request.getCreatedTime() != null ? request.getCreatedTime() : LocalDateTime.now())
                .build();

        if (id.isPresent()) {
            template.setId(id.get());
        }

        log.info("template converted: {}", template.toString());
        return template;
    }

}



