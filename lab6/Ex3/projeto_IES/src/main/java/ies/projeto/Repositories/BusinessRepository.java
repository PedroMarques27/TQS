package ies.projeto.Repositories;

import ies.projeto.Entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface BusinessRepository extends JpaRepository<Business, Integer>{
    @Query("SELECT b FROM Business b WHERE b.email = (:email)")
    Business getByEmail(@Param("email") String email);

    @Modifying
    @Query("delete from Business where email = :email")
    void deleteByEmail(@Param("email") String email);
}
