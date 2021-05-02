package ies.projeto.Repositories;

import ies.projeto.Entities.Client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {
    
    @Query("SELECT c FROM Client c WHERE c.email = (:email)")
    Client existsClient(@Param("email") String email);

    @Query("SELECT c FROM Client c WHERE c.email = (:email)")
    Client getByEmail(@Param("email") String email);

    @Modifying
    @Query("delete from Client where email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT u FROM Client u WHERE u.gender = (:sex)")
    List<Client> getBySexualOrientation(@Param("sex") String sex);


}
