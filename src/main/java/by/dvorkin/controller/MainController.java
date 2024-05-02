package by.dvorkin.controller;

import by.dvorkin.service.ISSEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;

@Controller
public class MainController {

    @Autowired
    private ISSEService sseService;

    @GetMapping("/")
    public String init() {
        return "index";
    }

    @GetMapping(path = "/sse/verification/result", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> subscribe() {
        return Flux.<ServerSentEvent<String>>create(
                fluxSink -> sseService.register("123", fluxSink));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/send")
    public void send() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                sseService.send("123", "You've got a message :D");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

}
