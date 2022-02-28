package es.grupocmc.api.util;

import es.grupocmc.api.command.rq.CreateUserRq;
import es.grupocmc.api.command.rs.UserRs;
import es.grupocmc.model.user.User;

public class UserCreator {

    public CreateUserRq getCreateUserRq() {
        return CreateUserRq.builder()
                .username("test1")
                .password("12345678fF#")
                .firstName("test")
                .lastName("test2")
                .firstSurname("noTest")
                .lastSurname("noTest")
                .email("noTest@test.com")
                .build();
    }

    public User getUserRq() {
        return User.builder()
                .username("test1")
                .password("12345678fF#")
                .firstName("test")
                .lastName("test2")
                .firstSurname("noTest")
                .lastSurname("noTest")
                .email("noTest@test.com")
                .build();
    }

    public UserRs getUserRs() {
        return UserRs.builder()
                .id(1L)
                .username("test1")
                .firstName("test")
                .lastName("test2")
                .firstSurname("noTest")
                .lastSurname("noTest")
                .email("noTest@test.com")
                .build();
    }

    public User getUserDB() {
        return User.builder()
                .id(1L)
                .password("12345678fF#")
                .username("test1")
                .firstName("test")
                .lastName("test2")
                .firstSurname("noTest")
                .lastSurname("noTest")
                .email("noTest@test.com")
                .build();
    }

}
