package no.ssb.timeuse.surveyservice.appointment;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import no.ssb.timeuse.surveyservice.metrics.CustomMetrics;
import no.ssb.timeuse.surveyservice.metrics.TaggedGauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AppointmentMetrics {

    private static final String METRICS_PREFIX = "tus.ss.appointment.";
    private TaggedGauge tgDbCount;

    private final AppointmentRepository appointmentRepository;

    public AppointmentMetrics(MeterRegistry meterRegistry, AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
        tgDbCount = new TaggedGauge(CustomMetrics.DB_COUNT, "table", meterRegistry);
    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = appointmentRepository.count();
        tgDbCount.set("Appointment", totalNumber);
    }

}
