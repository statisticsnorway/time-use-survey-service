package no.ssb.timeuse.surveyservice.codelist;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/v1/codelist")
public class CodeListController {

    @CrossOrigin
    @GetMapping
    public Map<String, CodeInfo> entries(@RequestParam (required = false) Optional<List<Status>> statusName) {
            return statusName.map(statusesFromQueryParams -> {
                return CodeList.status.entrySet().stream()
                        .filter(codeIntoEntry -> {
                            return codeIntoEntry.getValue()
                                    .getTypes()
                                    .stream()
                                    .anyMatch(statusesFromQueryParams::contains);
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }

            ).orElse(CodeList.status);
    }
}
