package ies.projeto.Repositories;

import ies.projeto.Entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Repository
@Transactional
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer>{
    @Query("SELECT s FROM Subscription s WHERE s.email = (:email)")
    ArrayList<Subscription> getByEmail(@Param("email") String email);

    @Query("SELECT s FROM Subscription s WHERE s.id = (:id)")
    Subscription getById(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("delete FROM Subscription s where s.id = :id")
    void deleteSubscriptionById(@Param("id") int id);

    @Modifying
    @Query("delete from Subscription where email = :email")
    void deleteByEmail(@Param("email") String email);
}
