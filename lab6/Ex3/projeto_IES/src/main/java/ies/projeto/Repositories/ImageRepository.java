package ies.projeto.Repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.projeto.Entities.Images;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Images, Integer>{
    @Query("SELECT i FROM Images i WHERE i.fileName = (:nome)")
    Images findByName(@Param("nome") String name);

    @Query("SELECT i FROM Images i WHERE i.userID = (:userid)")
    List<Images> findByUserID(@Param("userid") int userid);

    @Query("SELECT i FROM Images i WHERE i.fileName = (:fileName) AND i.userID = (:userid)")
    Images findByNameAndUser(@Param("fileName") String fileName, @Param("userid") int userid);

    @Modifying
    @Query("delete from Images where userID = :id")
    void deleteByUserID(@Param("id") int id);
}