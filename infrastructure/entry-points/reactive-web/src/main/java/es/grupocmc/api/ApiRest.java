package es.grupocmc.api;

import es.grupocmc.api.command.rq.EnvCreateUserRq;
import es.grupocmc.api.command.rq.EnvUpdatePasswordRq;
import es.grupocmc.api.command.rq.EnvUpdateUserRq;
import es.grupocmc.api.command.rs.*;
import es.grupocmc.commons.error.ErrorResponse;
import es.grupocmc.model.user.DataPassword;
import es.grupocmc.model.user.User;
import es.grupocmc.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Tag(name = "Admin Usuarios - CRUD",
        description = "Permite a los consumidores realizar las operaciones basicas para la administracion " +
                "de los usuarios.")
@RestController
@RestControllerAdvice
@RequestMapping(value = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class ApiRest {

    private final ObjectMapper mapper;
    private final UserUseCase userUseCase;

    @Operation(summary = "Crear un usuario.",
            description = "Capacidad que permite crear un usuario con todas sus caracteristicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Creacion exitosa",
                    content = @Content(schema = @Schema(implementation = EnvCreateUserRs.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @ResponseBody
    @PostMapping
    public Mono<ResponseEntity<EnvCreateUserRs>> createUser(@RequestBody @Validated EnvCreateUserRq userRq) {
        return Mono.just(userRq)
                .map(commandUser -> mapper.map(commandUser.getUsuario(), User.class))
                .doOnNext(user -> log.info("Mapper User"))
                .flatMap(userUseCase::setPsswrdEncoder)
                .doOnNext(user -> log.info("Encoder pass"))
                .flatMap(userUseCase::createUser)
                .doOnNext(user -> log.info("crearlo"))
                .map(user -> mapper.map(user, UserRs.class))
                .doOnNext(user -> log.info("Mapper Response"))
                .map(commandUser -> EnvCreateUserRs.builder().usuario(commandUser).build())
                .map(userRs -> ResponseEntity.status(HttpStatus.CREATED)
                        .headers(httpHeaders -> httpHeaders.add("url",
                                        "/api/admin/users/"+userRs.getUsuario().getId()))
                        .body(userRs));
    }

    @Operation(summary = "Lista de Usuarios",
            description = "Obtener un listado de usuarios con todas sus caracteristicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios",
                    content = @Content(schema = @Schema(implementation = UsersRs.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @ResponseBody
    @GetMapping
    public Mono<ResponseEntity<UsersRs>> getUsers() {
        return userUseCase.getUsers()
                .map(user -> mapper.map(user, UserRs.class))
                .collect(Collectors.toList())
                .map(usersRs -> ResponseEntity.ok().body(UsersRs.builder().usuarios(usersRs).build()));
    }

    @Operation(summary = "Usuario por ID",
            description = "Obtener un usuario por ID con todas sus caracteristicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido por ID",
                    content = @Content(schema = @Schema(implementation = EnvCreateUserRs.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping(path = "/{id}")
    public Mono<ResponseEntity<EnvCreateUserRs>> getUserById(@PathVariable(value = "id") Long userId) {
        return userUseCase.getUserById(userId)
                .map(user -> mapper.map(user, UserRs.class))
                .map(userRs -> EnvCreateUserRs.builder().usuario(userRs).build())
                .map(envCreateUserRs -> ResponseEntity.ok().body(envCreateUserRs));
    }

    @Operation(summary = "Actualizar Usuario por ID",
            description = "Actualizar un usuario por ID con todas sus caracteristicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado por ID",
                    content = @Content(schema = @Schema(implementation = EnvCreateUserRs.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PutMapping(path = "/{id}")
    public Mono<ResponseEntity<EnvCreateUserRs>> updateUser(@PathVariable(value = "id") Long userId,
                                                            @RequestBody EnvUpdateUserRq usuario) {
        return Mono.just(usuario)
                .map(envUpdateUserRq -> mapper.map(envUpdateUserRq.getUsuario(), User.class))
                .flatMap(user -> userUseCase.updateUser(userId, user))
                .map(user -> mapper.map(user, UserRs.class))
                .map(userRs -> ResponseEntity.ok()
                        .body(EnvCreateUserRs.builder().usuario(userRs).build()));
    }

    @Operation(summary = "Borrar Usuario por ID",
            description = "Borrar un usuario por ID con todas sus caracteristicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario borrado con exito",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @DeleteMapping(path = "/{id}")
    public Mono<Void> deleteUser(@PathVariable(value = "id") Long userId) {
        return userUseCase.deleteUser(userId);
    }

    @Operation(summary = "Validar Username",
            description = "Validar si existe un username determinado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username validado"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping(path = "/username/{username}")
    public Mono<ResponseEntity<EnvExistsUsernameRs>> getExistsUsername(@PathVariable(value = "username") String username ) {
        return userUseCase.getExistsUsername(username)
                .map(existsUsername -> mapper.map(existsUsername, ExistsUsernameRs.class))
                .map(existsUsernameRs -> ResponseEntity.ok()
                        .body(EnvExistsUsernameRs.builder().usuario(existsUsernameRs).build()));
    }

    @Operation(summary = "Actualizar contrasenia",
            description = "Se actualiza la contrasenia de un usuario validando la anterior"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Servicio no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PatchMapping(path = "/{id}/password")
    public Mono<ResponseEntity<String>> updatePassword(@PathVariable(value = "id") Long userId,
                                                        @RequestBody EnvUpdatePasswordRq envUpdatePasswordRq) {
        return userUseCase
                .updatePassword(userId, mapper.map(envUpdatePasswordRq.getUsuario(), DataPassword.class))
                .map(updated -> ResponseEntity.ok().body(updated));
    }

}
