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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedGaugeTest {

    @Test
    public void taggedGaugeTest(){
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        TaggedGauge taggedGauge = new TaggedGauge("tide", "city", registry);
        taggedGauge.set("Halifax", 1.75);
        taggedGauge.set("Portland", 1.3);
        taggedGauge.set("Portland", 2);
        taggedGauge.set("Venice", 0.81);
        taggedGauge.set("Halifax", 1.8);
        taggedGauge.set("Halifax", 1.98);
        taggedGauge.set("Venice", 0.43);
        taggedGauge.set("Halifax", 1.86);
        taggedGauge.set("Portland", 2.4);
        taggedGauge.set("Venice", -0.09);

        assertEquals(3, registry.getMeters().size());
    }

}
