package es.grupocmc.model.user.gateways;

import es.grupocmc.model.user.ExistsUsername;
import es.grupocmc.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> createOrUpdateUser(User user);
    Flux<User> getUsers();
    Mono<User> getUserById(Long id);
    Mono<ExistsUsername> getExistsUsername(String username);
    Mono<Void> deleteUser(User user);
}
