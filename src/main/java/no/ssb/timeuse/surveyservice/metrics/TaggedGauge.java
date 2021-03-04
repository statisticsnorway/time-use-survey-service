package no.ssb.timeuse.surveyservice.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.HashMap;
import java.util.Map;

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

public class TaggedGauge {
    private String name;
    private String tagName;
    private MeterRegistry registry;
    private Map<String, DoubleWrapper> gaugeValues = new HashMap<>();

    public TaggedGauge(String name, String tagName, MeterRegistry registry) {
        this.name = name;
        this.tagName = tagName;
        this.registry = registry;
    }

    public void set(String tagValue, double value){
        DoubleWrapper number = gaugeValues.get(tagValue);
        if(number == null) {
            DoubleWrapper valueHolder = new DoubleWrapper(value);
            Gauge.builder(name, valueHolder, DoubleWrapper::getValue).tags(tagName, tagValue).register(registry);
            gaugeValues.put(tagValue, valueHolder);

        } else {
            number.setValue(value);
        }
    }
}
