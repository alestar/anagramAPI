package com.anagrams.project.service;

import com.anagrams.project.util.FileIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AnagramLoader {

    private AnagramService anagramService;
    private FileIngestor fileIngestor;

    @Autowired
    public void setAnagramService(AnagramService anagramService) {
        this.anagramService = anagramService;
    }
    @Autowired
    public void setFileIngestor(FileIngestor fileIngestor ){
        this.fileIngestor = fileIngestor;
    }

    private void loadMapWithDicFile(){
        fileIngestor.ingestDicFile();
        try {
            List<String> linesList = fileIngestor.getListOfLines();
            anagramService.addWordsAsAnagram(linesList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @PostConstruct
    public void init(){
       loadMapWithDicFile();
    }


}
