package by.dvorkin.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

public interface ISSEService {

    void register(String login, FluxSink<ServerSentEvent<String>> fluxSink);

    void send(String login, String message);

}
