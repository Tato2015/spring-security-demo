package pe.mrtato.spring.security.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.mrtato.spring.security.demo.model.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	Optional<UserEntity> findUserEntityByUsername(String username);

}
