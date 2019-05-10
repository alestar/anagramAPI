package com.anagrams.project.repository;


import com.anagrams.project.entity.Anagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnagramRepository extends JpaRepository<Anagram, Long> {

    Anagram findByToken(String token);

    @Query("Select AVG(a.length) from Anagram a")
    int getLengthAverage();

    @Query("select max(a.length) from Anagram a")
    int getMaxLength();

    @Query("select min(a.length) from Anagram a")
    int getMinLength();

}

