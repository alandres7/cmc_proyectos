package es.grupocmc.util.service;

import es.grupocmc.model.user.User;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface BCryptService {
    User setPsswrdEncoder(User user);
    Mono<Tuple2<Boolean, User>> validatePassword(String interfacePassword, User user);
}
