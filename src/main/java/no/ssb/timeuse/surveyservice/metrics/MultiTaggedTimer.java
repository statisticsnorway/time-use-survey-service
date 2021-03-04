package no.ssb.timeuse.surveyservice.metrics;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

public class MultiTaggedTimer {
    private String name;
    private String[] tagNames;
    private MeterRegistry registry;
    private Map<String, Timer> timers = new HashMap<>();

    public MultiTaggedTimer(String name, MeterRegistry registry, String ... tags) {
        this.name = name;
        this.tagNames = tags;
        this.registry = registry;
    }

    public Timer getTimer(String ... tagValues){
        String valuesString = Arrays.toString(tagValues);
        if(tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are "+Arrays.toString(tagNames)+", provided tags are "+valuesString);
        }
        Timer timer = timers.get(valuesString);
        if(timer == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for(int i = 0; i<tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            timer = Timer.builder(name).tags(tags).register(registry);
            timers.put(valuesString, timer);
        }
        return timer;
    }

}
