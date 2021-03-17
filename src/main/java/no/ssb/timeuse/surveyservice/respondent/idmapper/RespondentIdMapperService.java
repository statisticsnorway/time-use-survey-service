package no.ssb.timeuse.surveyservice.respondent.idmapper;

import lombok.AllArgsConstructor;
import no.ssb.timeuse.surveyservice.utils.sample.SampleImport;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RespondentIdMapperService {

    public RespondentIdMapper mapFromSample(SampleImport request) {
        return RespondentIdMapper.builder()
                .respondentId(request.getNewUUID())
                .personIdentificator(request.getPersonIdentificator())
                .build();
    }
}
