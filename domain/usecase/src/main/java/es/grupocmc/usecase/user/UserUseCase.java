package es.grupocmc.usecase.user;

import es.grupocmc.commons.exception.ExistsException;
import es.grupocmc.commons.exception.NotFoundException;
import es.grupocmc.model.user.DataPassword;
import es.grupocmc.model.user.ExistsUsername;
import es.grupocmc.model.user.User;
import es.grupocmc.model.user.gateways.UserRepository;
import es.grupocmc.util.service.BCryptService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final BCryptService bCryptService;

    public Mono<User>  createUser(User user) {
        return getExistsUsername(user.getUsername())
                .flatMap(existsUsername -> {
                    if (!existsUsername.getExists()) return userRepository.createOrUpdateUser(user);
                    else return Mono.error(new ExistsException("El username ya existe"));
                });
    }
    public Flux<User> getUsers() { return userRepository.getUsers();}
    public Mono<User> getUserById(Long userId) {return  userRepository.getUserById(userId);}
    public Mono<User> updateUser(Long id, User user) {
        return getUserById(id)
                .flatMap(userOld -> {
                    user.setId(userOld.getId());
                    user.setPassword(userOld.getPassword());
                   return userRepository.createOrUpdateUser(user);
                });
    }

    public Mono<ExistsUsername> getExistsUsername( String username ) {
        return userRepository.getExistsUsername(username);
    }

    public Mono<Void> deleteUser( Long id ) {
        return getUserById(id)
                .flatMap(userRepository::deleteUser);
    }

    public Mono<String> updatePassword(Long id, DataPassword dataPassword) {
        return getUserById(id)
                .flatMap(user -> bCryptService.validatePassword(dataPassword.getCurrentPassword(), user))
                .flatMap(tupla -> {
                        if (tupla.getT1()) {
                            tupla.getT2().setPassword(dataPassword.getNewPassword());
                            userRepository.createOrUpdateUser(bCryptService.setPsswrdEncoder(tupla.getT2()));
                            return Mono.just("Contraseña actualizada");
                        }
                        else return Mono.error(new NotFoundException("La contraseña es incorrecta"));
                        });
    }

    public Mono<User> setPsswrdEncoder(User user) {
        return Mono.just(bCryptService.setPsswrdEncoder(user));
    }

}
