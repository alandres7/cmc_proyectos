package es.grupocmc.jpa.dto.user;

import es.grupocmc.commons.exception.NotContentException;
import es.grupocmc.commons.exception.NotFoundException;
import es.grupocmc.jpa.helper.AdapterOperations;
import es.grupocmc.model.user.ExistsUsername;
import es.grupocmc.model.user.User;
import es.grupocmc.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserDTO, Long, JPARepository>
implements UserRepository
{

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.mapBuilder(d,User.UserBuilder.class).build());
    }


    @Override
    public Mono<User> createOrUpdateUser(User user) {
        return Mono.just(mapper.map(repository.save(mapper.map(user, UserDTO.class)), User.class));
    }

    @Override
    public Flux<User> getUsers() {
        List<User> users = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(userDTO -> mapper.map(userDTO, User.class))
                .collect(Collectors.toList());
        return Flux.fromIterable(users);
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return Mono.just(mapper.map(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un usuario con el " +
                        "ID "+ id)), User.class));
    }

    @Override
    public Mono<ExistsUsername> getExistsUsername(String username) {
        if(repository.findByUsername(username) == null)
            return Mono.just(ExistsUsername.builder().exists(Boolean.FALSE).build());
        else
            return Mono.just(ExistsUsername.builder().exists(Boolean.TRUE).build());
    }

    @Override
    public Mono<Void> deleteUser( User user ) {
        repository.delete(mapper.map(user, UserDTO.class));
        return Mono.error(new NotContentException("Usuario "+user.getId()+" borrado con exito"));
    }


}
