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


        stats.setAverageWordLength(anagramRepository.findLengthAverage());
        stats.setMaxWordsLength(anagramRepository.findMaxLength());
        stats.setMinWordsLength(anagramRepository.findMinLength());

        Long totalRows =anagramRepository.count();
        if(totalRows > 0) {
            Long medianId= totalRows / 2;
            stats.setMedianWordsLength(anagramRepository.findByIdOrderByLengthDesc(medianId).getLength());
        }
     return stats;
    }
}
