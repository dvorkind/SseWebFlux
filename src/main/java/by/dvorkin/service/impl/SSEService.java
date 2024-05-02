package by.dvorkin.service.impl;

import by.dvorkin.service.ISSEService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SSEService implements ISSEService {

    private final ConcurrentMap<String, FluxSink<ServerSentEvent<String>>> subscriptions = new ConcurrentHashMap<>();

    @Override
    public void register(String login, FluxSink<ServerSentEvent<String>> fluxSink) {
        subscriptions.put(login, fluxSink);
        fluxSink.onCancel(() -> subscriptions.remove(login));
    }

    @Override
    public void send(String login, String message) {
        FluxSink<ServerSentEvent<String>> subscriber = subscriptions.get(login);
        if (subscriber != null) {
            ServerSentEvent<String> event = ServerSentEvent
                    .builder(message)
                    .build();
            subscriber.next(event);
        }

    }

}
