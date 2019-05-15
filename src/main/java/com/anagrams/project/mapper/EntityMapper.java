package com.anagrams.project.mapper;

import com.anagrams.project.entity.Anagram;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class EntityMapper {

    public List<Anagram> mapper(Map<String, Set<String>> tokenToWords){
        List<Anagram> anagramList =  new ArrayList<>();

        for(Map.Entry<String, Set<String>> entry : tokenToWords.entrySet()){

            Anagram anagram = new Anagram();

            anagram.setLength(entry.getKey().length());
            anagram.setToken(entry.getKey());
            anagram.setWords(entry.getValue().toString().replaceAll("\\[|]", ""));
            anagram.setVolume(entry.getValue().size());
            anagramList.add(anagram);

        }
        return anagramList;
    }

}


