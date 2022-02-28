package es.grupocmc.api.command.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRs {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String firstSurname;

    private String lastSurname;

    private String email;
}
