package es.grupocmc.utils;

import es.grupocmc.model.user.User;
import es.grupocmc.util.service.BCryptService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
@AllArgsConstructor
public class BCryptUtil implements BCryptService {

    private final BCryptPasswordEncoder encoder;

    @Override
    public User setPsswrdEncoder(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public Mono<Tuple2<Boolean, User>> validatePassword(String interfacePassword, User user) {
        return  Mono.zip(Mono.just(encoder.matches(interfacePassword, user.getPassword())), Mono.just(user));
    }

}
