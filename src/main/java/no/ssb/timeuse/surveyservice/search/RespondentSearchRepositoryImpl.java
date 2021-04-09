package no.ssb.timeuse.surveyservice.search;

import lombok.extern.slf4j.Slf4j;
import no.ssb.timeuse.surveyservice.appointment.Appointment;
import no.ssb.timeuse.surveyservice.respondent.Respondent;
import no.ssb.timeuse.surveyservice.respondent.Respondent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RespondentSearchRepositoryImpl implements RespondentSearchRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Respondent> searchForRespondents(SearchRequestGroup groupRequest) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Respondent> cq = cb.createQuery(Respondent.class);
        Root<Respondent> root = cq.from(Respondent.class);
        Predicate andPredicate = buildAndPredicates(groupRequest.getPredicates(), cb, root);
        Predicate withInterval = applyTimeInterval(andPredicate,
                groupRequest.getDiaryStartFrom(), groupRequest.getDiaryStartTo(), cb, root);
        Predicate withAppointmentFilter = filterAwayAppointments(cb, cq, root);
        return manager.createQuery(cq.where(withInterval, withAppointmentFilter)).getResultList();
    }

    private Predicate buildAndPredicates(Map<String, List<String>> predicateMap, CriteriaBuilder cb, Root<Respondent> root) {
        List<Predicate> andPredicates = predicateMap.entrySet().stream().map(entry ->  buildOrPredicate(entry.getKey(), entry.getValue(), cb, root)).collect(Collectors.toList());
        if (andPredicates.size() > 1) return cb.and(andPredicates.toArray(Predicate[]::new));
        else if(andPredicates.size() == 1) return andPredicates.get(0);
        else return null;
    }

    private Predicate buildOrPredicate(String fieldName, List<String> orList, CriteriaBuilder cb, Root<Respondent> root) {
        List<Predicate> orPredicates = orList.stream().map(orValue -> cb.equal(root.get(fieldName), orValue)).collect(Collectors.toList());
        if (orPredicates.size() > 1) return cb.or(orPredicates.toArray(Predicate[]::new));
        else if (orPredicates.size() == 1) return orPredicates.get(0);
        else return null;
    }

    private Predicate applyTimeInterval(Predicate fieldPredicate, LocalDate from, LocalDate to, CriteriaBuilder cb, Root<Respondent> root) {
        if(from != null) {
            if(to != null) {
                return cb.and(fieldPredicate, cb.between(root.get("diaryStart"), from, to));
            }
        }
        return fieldPredicate;
    }

    private Predicate filterAwayAppointments(CriteriaBuilder cb, CriteriaQuery<Respondent> cq, Root<Respondent> root) {
        Subquery<Long> subApp = cq.subquery(Long.class);
        Root<Respondent> subRoot = subApp.from(Respondent.class);
        Join<Respondent, Appointment> appJoin = subRoot.join("appointments");
        subApp.select(cb.count(appJoin.get("id")));
        subApp.where(cb.and(cb.equal(root.get("respondentId"), subRoot.get("respondentId")), cb.greaterThan(appJoin.get("appointmentTime"), cb.currentTimestamp())));
        return cb.lessThan(subApp, 1L);
    }

}
