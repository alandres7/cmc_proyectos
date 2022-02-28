package es.grupocmc.api.command.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EnvCreateUserRq {
    @Valid
    @NotNull(message = "es requerida")
    private CreateUserRq usuario;
}
