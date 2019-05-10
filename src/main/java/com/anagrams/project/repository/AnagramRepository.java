package com.anagrams.project.repository;


import com.anagrams.project.entity.Anagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnagramRepository extends JpaRepository<Long, Anagram>{


}

