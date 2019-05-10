package com.anagrams.project.service;

import com.anagrams.project.model.StatsModel;
import com.anagrams.project.repository.AnagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

@Component
public class StatsService {

    @Autowired
    AnagramRepository anagramRepository;


    public StatsModel calculateStats (){
        StatsModel statsModel = new StatsModel();

        Long totalRows =anagramRepository.count();

        statsModel.setAverageWordLength(anagramRepository.getLengthAverage());
        statsModel.setMaxWordsLength(anagramRepository.getMaxLength());
        statsModel.setMinWordsLength(anagramRepository.getMinLength());
        statsModel.setMedianWordsLength(anagramRepository
                .findById(totalRows/2).get().getLength());
     return statsModel;

    }


}
