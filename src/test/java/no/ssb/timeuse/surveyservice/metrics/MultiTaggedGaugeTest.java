package no.ssb.timeuse.surveyservice.metrics;
/*
MIT License

Copyright (c) 2020 Firedome

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiTaggedGaugeTest {
    @Test
    public void multiTaggedGaugeTest() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MultiTaggedGauge multiTaggedGauge = new MultiTaggedGauge("weekly-high-tide", registry, "city", "day");
        multiTaggedGauge.set(1.75, "Halifax", "Sunday");
        multiTaggedGauge.set(1.3, "Portland", "Sunday");
        multiTaggedGauge.set(2, "Portland", "Monday");
        multiTaggedGauge.set(0.81, "Venice", "Monday");
        multiTaggedGauge.set(1.8, "Halifax", "Monday");
        multiTaggedGauge.set(1.98, "Halifax", "Monday");
        multiTaggedGauge.set(0.43, "Venice", "Tuesday");
        multiTaggedGauge.set(1.86, "Halifax", "Tuesday");
        multiTaggedGauge.set(2.4, "Portland", "Monday");
        multiTaggedGauge.set(0.56, "Venice", "Tuesday");

        assertEquals(7, registry.getMeters().size());

    }

    @Test
    public void multiTaggedGaugeIllegalArgsTest() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MultiTaggedGauge multiTaggedGauge = new MultiTaggedGauge("weekly-high-tide", registry, "city", "day");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedGauge.set(1.75, "Halifax");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            multiTaggedGauge.set(1.75, "Halifax", "Sunday", "boo");
        });
    }
}
