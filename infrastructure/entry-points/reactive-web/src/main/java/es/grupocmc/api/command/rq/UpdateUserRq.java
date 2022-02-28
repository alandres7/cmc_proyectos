package es.grupocmc.api.command.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRq {

    private String username;

    private String firstName;

    private String lastName;

    private String firstSurname;

    private String lastSurname;

    private String email;
}
