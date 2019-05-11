package com.anagrams.project.repository;


import com.anagrams.project.entity.Anagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnagramRepository extends JpaRepository<Anagram, Long> {

    Anagram findByToken(String token);

    Anagram findByVolume(Integer vol);

    List<Anagram> findByLength(Integer Length);

    @Query("Select AVG(a.length) from Anagram a")
    int getLengthAverage();

    @Query("select max(a.length) from Anagram a")
    int getMaxLength();

    @Query("select min(a.length) from Anagram a")
    int getMinLength();

    @Query("select max(a.volume) from Anagram a")
    int getMaxVolume();

    @Query("select words from Anagram where max(a.volume)")
    String getMostAnagrams();

    @Query("delete a.words from Anagram where a.words=words")

    @Override
    void deleteAll();
}

