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


        stats.setAverageWordLength(anagramRepository.getLengthAverage());
        stats.setMaxWordsLength(anagramRepository.getMaxLength());
        stats.setMinWordsLength(anagramRepository.getMinLength());

        Long totalRows =anagramRepository.count();
        if(totalRows > 0) {
            Long medianId= totalRows / 2;
            stats.setMedianWordsLength((anagramRepository.findById(medianId).get()).getLength());
            //stats.setMedianWordsLength(anagramRepository.getMedianLength());

        }
     return stats;
    }
}
