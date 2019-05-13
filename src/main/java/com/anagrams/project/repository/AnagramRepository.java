package com.anagrams.project.repository;


import com.anagrams.project.entity.Anagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnagramRepository extends JpaRepository<Anagram, Long> {

    Optional<Anagram> findById(Long id);

    Anagram findByToken(String token);

    List<Anagram> findAllByLength(Integer length);

    //@Query(value = "SELECT FROM Anagram a WHERE a.length >= size  ")// To fix
    //List<Anagram>  getGroupSize(int size);

    @Query("Select AVG(a.length) from Anagram a")
    int getLengthAverage();

    @Query("select max(a.length) from Anagram a")
    int getMaxLength();

    @Query("select min(a.length) from Anagram a")
    int getMinLength();

    @Query(value = "SELECT TOP 1  WORDS FROM ANAGRAM GROUP BY WORDS HAVING MAX(VOLUME) order by  volume desc", nativeQuery = true)
    String getMaxVolume();

    //@Query( "SELECT a.length FROM Anagram GROUP BY LENGHT HAVING a ORDER BY length desc ")// To fix
    //int getMedianLength();

    @Transactional
    Long removeAnagramByToken(String token);
}

