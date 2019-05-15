package com.anagrams.project.controller;

import com.anagrams.project.model.AnagramGet;
import com.anagrams.project.model.AnagramPost;
import com.anagrams.project.model.Stats;
import com.anagrams.project.service.AnagramService;
import com.anagrams.project.service.DataloaderService;
import com.anagrams.project.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class AnagramRestResource {
    private static Logger logger= Logger.getLogger(AnagramRestResource.class.getName());

    @Autowired
    private AnagramService anagramService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private DataloaderService dataloaderService;

    //-------------------------------------------------------------------------------------------------------------------
    //                                          ADD (POST METHODS)
    //-------------------------------------------------------------------------------------------------------------------

    @PostMapping("/populate/words.json")
    public @ResponseBody ResponseEntity<String> populateWords(){

        if(dataloaderService.init()){
           return new ResponseEntity<>("Dictionary file has been loaded in to the corpus DB", HttpStatus.CREATED);
       }else {
           return new ResponseEntity<>("The DB is already filled up",HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/words.json")
    public @ResponseBody ResponseEntity<String> addWordsAsAnagram(@RequestBody AnagramPost anagramPost){

        if (anagramService.addWordsAsAnagram(anagramPost.getWords())){
            logger.info("This new words: " + anagramPost.getWords() + " has been inserted into DB successfully");
            return new ResponseEntity<>("The new word(s) were added as anagrams", HttpStatus.CREATED);
        }
        else{
            logger.info("The Anagram for those words already exist in data base");
            return new ResponseEntity<>("The input word(s) have already been added", HttpStatus.CREATED);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          FETCH (GET METHODS)
    //-------------------------------------------------------------------------------------------------------------------

    @PostMapping("/anagrams/words.json")
    public @ResponseBody ResponseEntity<String> areAnagrams(@RequestBody AnagramPost anagramPost){

        if(anagramService.areAnagrams(anagramPost.getWords())){
            return new ResponseEntity<>("This words are Anagrams between each others!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("This words are not anagrams", HttpStatus.OK);
        }
    }

    @GetMapping("/anagrams/{word}.json")
    public @ResponseBody ResponseEntity fetchAnagramsOfWord(@PathVariable(value = "word") String word, @RequestParam(defaultValue="0", required = false) Integer limit,
                                                                    @RequestParam (defaultValue="true", required = false)Boolean permitPN){

        AnagramGet anagramGet = anagramService.fetchAnagramsOfWord(word, limit, permitPN);
        return new ResponseEntity<>(anagramGet, HttpStatus.OK);
    }

    @GetMapping("/most/words.json")
    public @ResponseBody ResponseEntity fetchMostAnagramsWords(){

        AnagramGet anagramGet = anagramService.fetchMostAnagramsWords();
        return new ResponseEntity<>(anagramGet, HttpStatus.OK);
    }

    @GetMapping("/anagrams/size.json")
    public @ResponseBody ResponseEntity fetchAnagramsOfGroupSize(@RequestParam String groupSize){

        AnagramGet anagramGet = anagramService.fetchAnagramsOfGroupSize(groupSize);
        return new ResponseEntity<>(anagramGet, HttpStatus.OK);
    }

    @GetMapping("/stats/words.json")
    public @ResponseBody ResponseEntity fetchStats(){

        Stats stats = statsService.calculateStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          DELETE(DELETE METHODS)
    //-------------------------------------------------------------------------------------------------------------------

    @DeleteMapping("/words.json")
    public @ResponseBody ResponseEntity deleteAll(){

        anagramService.deleteAll();
        return new ResponseEntity<>("Deleted All Data successfully!", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/words/{word}.json")
    public @ResponseBody ResponseEntity<String> deleteWord(@PathVariable(value = "word") String word){

        if(anagramService.deleteWord(word)){
            return new ResponseEntity<>("The input word was successfully deleted", HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>("Unable to delete the input word", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/words/anagrams/{word}.json")
    public @ResponseBody ResponseEntity<String> deleteAnagramsOfWord(@PathVariable(value = "word") String word){

        if(anagramService.deleteAnagramsOfWord(word)){
            return new ResponseEntity<>("The anagrams of the input word were successfully deleted", HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>("Unable to delete the anagrams of the input word", HttpStatus.BAD_REQUEST);
        }
    }
}