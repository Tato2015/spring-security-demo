package pe.mrtato.spring.security.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.mrtato.spring.security.demo.model.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long>{

	List<RoleEntity> findRoleEntiesByRoleEnumIn(List<String> roleNames);
	
	
}
