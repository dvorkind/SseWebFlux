package by.dvorkin.controller;

import by.dvorkin.service.ISSEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private ISSEService sseService;

    @GetMapping("/")
    public String init() {
        System.out.println("INDEX");
        return "index";
    }

    @GetMapping(path = "/sse/verification/result", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Async
    public Flux<?> subscribe(HttpServletRequest request) {
        return Flux.<ServerSentEvent<String>>create(
                fluxSink -> sseService.register(request.getSession().getId(), fluxSink));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/send")
    public void send() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                sseService.send("123", "Hello World");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

}
