package es.grupocmc.jpa.dto.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JPARepository extends CrudRepository<UserDTO, Long>, QueryByExampleExecutor<UserDTO> {

    UserDTO findByUsername(String username);

}
