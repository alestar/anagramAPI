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

    List<Anagram> findAllByLengthGreaterThanEqual(Integer length);

    @Query("Select AVG(a.length) from Anagram a")
    int findLengthAverage();

    @Query("select max(a.length) from Anagram a")
    int findMaxLength();

    @Query("select min(a.length) from Anagram a")
    int findMinLength();

    @Query(value = "SELECT TOP 1  WORDS FROM ANAGRAM GROUP BY WORDS HAVING MAX(VOLUME) order by  volume desc", nativeQuery = true)
    String getMaxVolume();

    @Transactional
    Long removeAnagramByToken(String token);

    Anagram findByIdOrderByLengthDesc(Long id);
}

