package ies.projeto.Repositories;

import ies.projeto.Entities.Promotion;
import ies.projeto.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email) and u.password = (:password)")
    User existsUser(@Param("email") String email, @Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.id = (:id)")
    User getById(@Param("id") int id);

    @Query("SELECT u FROM User u WHERE u.email = (:email)")
    User getByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.tipo = (:tipo)")
    ArrayList<User> getByTipo(@Param("tipo") String tipo);

    

    @Transactional
    @Modifying
    @Query("delete FROM User u where u.id = :id")
    void deleteUsersById(@Param("id") int id);

    @Query("select count(u)>0 from User u where u.email = (:to)")
	boolean existsUser(String to);

}