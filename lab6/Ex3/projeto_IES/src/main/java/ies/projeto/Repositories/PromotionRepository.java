package ies.projeto.Repositories;

import ies.projeto.Entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Transactional
public interface PromotionRepository extends JpaRepository<Promotion, Integer>{
    @Query("SELECT p FROM Promotion p WHERE p.email = (:email)")
    ArrayList<Promotion> getByEmail(@Param("email") String email);

    @Query("SELECT p FROM Promotion p WHERE p.id = (:id)")
    Promotion getById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("delete FROM Promotion p where p.id = :id")
    void deletePromotionById(@Param("id") int id);

    @Modifying
    @Query("delete from Promotion where email = :email")
    void deleteByEmail(@Param("email") String email);
}
