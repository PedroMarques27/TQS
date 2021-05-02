package ies.projeto.Repositories;

import ies.projeto.Entities.Match;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MatchesRepository extends JpaRepository<Match, Integer> {
    @Query("SELECT m FROM Match m WHERE m.email1 = (:email) OR m.email2 = (:email)")
    ArrayList<Match> findMatchByEmail(@Param("email") String email);

    @Query("SELECT m FROM Match m WHERE ((m.email1 = (:email1) AND m.email2 = (:email2)) OR (m.email1 = (:email2) AND m.email2 = (:email1)) )")
    Match findMatchByEmails(@Param("email1") String email1, @Param("email2") String email2);

    @Query("SELECT m FROM Match m WHERE (m.email1 = (:email) AND m.swipe_email1=0) OR (m.email2 = (:email) AND m.swipe_email2=0)")
    ArrayList<Match> findUndecidedMatchByEmail(@Param("email") String email);


    @Query("SELECT m FROM Match m WHERE (m.email1 = (:email) OR m.email2 = (:email)) AND (m.swipe_email1=1 AND m.swipe_email2=1 AND m.talking=0)")
    ArrayList<Match> findDecidedMatchByEmailNotTalked(@Param("email") String email);

    @Query("SELECT m FROM Match m WHERE ((m.email1 = (:email) OR m.email2 = (:email)) AND (m.swipe_email1=1 AND m.swipe_email2=1 AND m.talking=1))")
    ArrayList<Match> findDecidedMatchByEmailTalked(@Param("email") String email);
    
    @Transactional
    @Modifying
    @Query("delete FROM Match m where m.id = :id")
    void deleteMatchById(@Param("id") int id);

    @Modifying
    @Query("delete from Match where email1 = :email or email2=(:email)")
    void deleteByEmail(@Param("email") String email);

}