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

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaggedTimerTest {

    @Test
    public void taggedTimerTest(){
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        TaggedTimer taggedTimer = new TaggedTimer("some-timer", "action", registry);
        taggedTimer.getTimer("walk-the-dog").record(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        taggedTimer.getTimer("make-dinner").record(30, TimeUnit.MINUTES);
        taggedTimer.getTimer("homework").record(2, TimeUnit.HOURS);
        taggedTimer.getTimer("walk-the-dog").record(10, TimeUnit.MINUTES);
        taggedTimer.getTimer("walk-the-dog").record(15, TimeUnit.MINUTES);
        List<Meter> meters = registry.getMeters();
        assertEquals(3, meters.size());
    }

}
