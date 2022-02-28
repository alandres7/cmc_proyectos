package es.grupocmc.api.command.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRq {

    @Valid
    @NotBlank(message = "es requerida")
    @NotEmpty(message = "es requerida")
    @Size(min = 3, max = 20, message = "debe tener entre 3 a 20 caracteres")
    private String username;

    @Valid
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
        message = "debe tener entre 8 y 20 caracteres, digitos, letras mayusculas, " +
                "letras minusculas y caracteres especiales")
    private String password;

    @Valid
    @NotBlank(message = "es requerida")
    @NotEmpty(message = "es requerida")
    @Size(min = 3, max = 20, message = "debe tener entre 3 a 20 caracteres")
    private String firstName;

    @Valid
    @Size(max = 20, message = "debe tener máximo 20 caracteres")
    private String lastName;

    @Valid
    @NotBlank(message = "es requerida")
    @NotEmpty(message = "es requerida")
    @Size(min = 3, max = 20, message = "debe tener entre 3 a 20 caracteres")
    private String firstSurname;

    @Valid
    @Size(max = 20, message = "debe tener máximo 20 caracteres")
    private String lastSurname;

    @Valid
    @NotBlank(message = "es requerido")
    @NotEmpty(message = "es requerido")
    @NotNull(message = "es requerido")
    @Size(min = 5, max = 100, message = "debe tener 1 y 100 caracteres")
    @Email(message = "formato de email invalido")
    private String email;
}
