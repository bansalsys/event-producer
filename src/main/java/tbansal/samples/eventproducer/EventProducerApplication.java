package tbansal.samples.eventproducer;

import lombok.Builder;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import tbansal.samples.eventproducer.EventProducerController.Events.Event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@SpringBootApplication
public class EventProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventProducerApplication.class, args);
    }


}

@Configuration
class EventProducerController {

    private static final String EVENTS = "/events";

    int eventNumber = 0;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(GET(EVENTS).and(accept(APPLICATION_JSON)),
                                     handleRequest());
    }

    private HandlerFunction<ServerResponse> handleRequest() {

        return request -> ServerResponse.ok().body(Mono.just(Events.builder().events(createEvents()).build()), Events.class);
    }


    private List<Event> createEvents() {
        return IntStream.range(0, 1000)
                        .mapToObj(integer -> Event.builder()
                                                  .id(eventNumber++)
                                                  .name("Event" + eventNumber)
                                                  .value("Value" + eventNumber)
                                                  .build())
                        .collect(Collectors.toList());
    }

    @Value
    @Builder
    static class Events {

        List<Event> events;

        @Value
        @Builder
        static class Event {

            String name;
            String value;
            Integer id;

        }
    }


}
