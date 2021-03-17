package no.ssb.timeuse.surveyservice.appointment;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AppointmentMetrics {

    private static final String METRICS_PREFIX = "tus.ss.appointment.";
    private final AtomicInteger gaugeTotal;

    private final AppointmentRepository appointmentRepository;

    public AppointmentMetrics(MeterRegistry meterRegistry, AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.gaugeTotal = meterRegistry.gauge(METRICS_PREFIX + "total", new AtomicInteger(0));    }

    public void generateMetrics() {
        countTotals();
    }

    private void countTotals() {
        val totalNumber = appointmentRepository.count();
        gaugeTotal.set((int) totalNumber);
    }

}
