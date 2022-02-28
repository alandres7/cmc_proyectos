package es.grupocmc.jpa.dto.user;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "tb_user",
        indexes = {@Index(name = "index_username_user",
                columnList = "username",
                unique = true)})
public class UserDTO {

    @Id
    @SequenceGenerator(name = "seqGenIdUser", sequenceName = "seq_Gen_Id_User")
    @GeneratedValue(generator = "seqGenIdUser")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_surname")
    private String firstSurname;

    @Column(name = "last_surname")
    private String lastSurname;

    @Column(name = "email")
    private String email;

}
