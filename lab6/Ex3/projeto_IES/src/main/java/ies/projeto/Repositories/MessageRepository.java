package ies.projeto.Repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.projeto.Entities.Message;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Integer>{
    @Query("SELECT m FROM Message m WHERE m.conversation = (:conversation)")
    List<Message> getUsersPreviousMessages(@Param("conversation") String conversation);

    @Modifying
    @Query("delete from Message where sender = :email or receiver = :email")
    void deleteByEmail(@Param("email") String email);
 
}
