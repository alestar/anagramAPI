package com.anagrams.project.service;

import com.anagrams.project.model.Stats;
import com.anagrams.project.repository.AnagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatsService {

    @Autowired
    AnagramRepository anagramRepository;

    public Stats calculateStats (){
        Stats stats = new Stats();

        Long totalRows =anagramRepository.count();

        stats.setAverageWordLength(anagramRepository.getLengthAverage());
        stats.setMaxWordsLength(anagramRepository.getMaxLength());
        stats.setMinWordsLength(anagramRepository.getMinLength());
        stats.setMedianWordsLength(anagramRepository
                .findById(totalRows/2).get().getLength());
     return stats;
    }
}
