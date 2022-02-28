package es.grupocmc.api;

import es.grupocmc.api.command.rq.EnvCreateUserRq;
import es.grupocmc.api.command.rs.EnvCreateUserRs;
import es.grupocmc.api.command.rs.UserRs;
import es.grupocmc.api.util.UserCreator;
import es.grupocmc.model.user.User;
import es.grupocmc.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
public class ApiRestTest {

    @InjectMocks
    private ApiRest apiRest;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private UserUseCase userUseCase;

    private EnvCreateUserRq envCreateUserRq;

    private User userNoDB;

    private EnvCreateUserRs envCreateUserRs;

    private User userDB;

    @BeforeEach
    void setup(){
        apiRest = new ApiRest(mapper, userUseCase);
        UserCreator userCreator = new UserCreator();
        envCreateUserRq = EnvCreateUserRq.builder()
                .usuario(userCreator.getCreateUserRq())
                .build();
        envCreateUserRs = EnvCreateUserRs.builder()
                .usuario(userCreator.getUserRs())
                .build();
        userNoDB = userCreator.getUserRq();
        userDB = userCreator.getUserDB();
        BDDMockito.when(userUseCase.createUser(userNoDB))
                .thenReturn(Mono.just(userDB));
        BDDMockito.when(userUseCase.setPsswrdEncoder(userNoDB))
                        .thenReturn(Mono.just(userNoDB));
        BDDMockito.when(mapper.map(envCreateUserRq.getUsuario(), User.class))
                .thenReturn(userNoDB);
        BDDMockito.when(mapper.map(userDB, UserRs.class))
                .thenReturn(userCreator.getUserRs());
    }

    @Test
    void crearUsuarioExitoso() {
        WebTestClient.bindToController(apiRest)
                .build()
                .post()
                .uri("/api/admin/users")
                .body(Mono.just(envCreateUserRs), EnvCreateUserRs.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.ACCEPTED);
    }





}
