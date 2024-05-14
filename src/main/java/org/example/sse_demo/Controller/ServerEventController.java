package org.example.sse_demo.Controller;


import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.time.Instant;

@RestController
@RequestMapping("/serverevents")
public class ServerEventController {

    @GetMapping(produces= MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<ServerSentEvent<String>> getEvents() throws IOException {

        Stream<String>lines=Files.lines(Path.of("C:\\karthik\\DummyResposne.txt"));


        AtomicInteger counter= new AtomicInteger();
        return Flux.fromStream(lines)
                .filter(line->!line.isBlank())
                .map(line-> ServerSentEvent.<String> builder()
                            .id(String.valueOf(counter.getAndIncrement()))
                            //.data(line)
                            .data("timestamp="+String.valueOf(Instant.now()))
                            .event("lineEvent")
                            .retry(Duration.ofMillis(1000))
                            .build())
        .delayElements(Duration.ofMillis(5000));




    }
}
