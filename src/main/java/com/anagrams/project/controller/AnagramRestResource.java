package com.anagrams.project.controller;

import com.anagrams.project.exception.IncorrectAnagramException;
import com.anagrams.project.model.AnagramGet;
import com.anagrams.project.model.AnagramPost;
import com.anagrams.project.model.Stats;
import com.anagrams.project.service.AnagramService;
import com.anagrams.project.service.DataloaderService;
import com.anagrams.project.service.StatsService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @PostMapping("/populate/words.json")
    public @ResponseBody ResponseEntity<String> populateWords(){


           if(dataloaderService.init()){
               return new ResponseEntity<>("Dictionary has been added to the corpus", HttpStatus.CREATED);
           }else {
               return new ResponseEntity<>("There was an error processing your request",HttpStatus.BAD_REQUEST);
           }
        }


    @PostMapping("/anagrams/words.json")
    public @ResponseBody ResponseEntity<String> areAnagrams(@RequestBody AnagramPost anagramPost){

        try {
            if(anagramService.areAnagrams(anagramPost.getWords())){
                return new ResponseEntity<>("This words are Anagrams between each others!", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("This words are not anagrams", HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/words.json")
    public @ResponseBody ResponseEntity<String> addWordsAsAnagram(@RequestBody AnagramPost anagramPost){
        try {
            boolean success = anagramService.addWordsAsAnagram(anagramPost.getWords());
            if (success){
                logger.info("This new words: " + anagramPost.getWords() + " has been inserted into DB successfully");
                return new ResponseEntity<>("Unexpected response code", HttpStatus.CREATED);
            }
            else{
                logger.info("The Anagram for those words already exist in data base");
                return new ResponseEntity<>("Unexpected response code", HttpStatus.CREATED);
            }
        }
        catch (IncorrectAnagramException e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/anagrams/{word}.json")
    public @ResponseBody ResponseEntity<String> fetchAnagramWords(@PathVariable(value = "word") String word, @RequestParam(defaultValue="0", required = false) Integer limit,
                                                                    @RequestParam (defaultValue="true", required = false)Boolean permitPN){
        List<String> result = anagramService.fetchAnagramsOfWord(word, limit, permitPN);//Return All

        if(result == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        }
        else {
            Gson gson = new Gson();
            AnagramGet anagramGet =  new AnagramGet(result);
            return new ResponseEntity<>(gson.toJson(anagramGet), HttpStatus.OK);
        }
    }

    @GetMapping("/stats/words.json")
    public @ResponseBody ResponseEntity<String> fetchStats(){

        Stats stats = statsService.calculateStats();

        if(stats == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        }
        else {
            Gson gson = new Gson();
            return new ResponseEntity<>(gson.toJson(stats), HttpStatus.OK);
        }
    }

    @GetMapping("/most/words.json")
    public @ResponseBody ResponseEntity<String> fetchMostAnagramsWords(){
        List<String> result=anagramService.fetchMostAnagramsWords();
        return createResponseEntity(result);
    }

    @GetMapping("/anagrams/size.json")
    public @ResponseBody ResponseEntity<String> fetchAnagramsGroupOfSize( @RequestParam String groupSize){
        int gs = 0;
        try {
            gs = Integer.valueOf(groupSize);
        }
        catch (NumberFormatException e){
            return new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        }
        List<String> result = anagramService.fetchAnagramGroupOfSize(gs);
        return createResponseEntity(result);
    }

    @DeleteMapping("/words.json")
    public @ResponseBody ResponseEntity deleteAll(){

        try{
            logger.info("Attempt to delete ALL data: ");
            anagramService.deleteALL();
        }
        catch (Exception e){
            logger.info("Error deleting ALL data: " + e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Deleted ALL data successfully!");

        return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/words/{word}.json")
    public @ResponseBody ResponseEntity<String> deleteWord(@PathVariable(value = "word") String word){



        if(!anagramService.deleteWord(word)){
            return new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/words/anagrams/{word}.json")
    public @ResponseBody ResponseEntity<String> deleteAnagramsOfWord(@PathVariable(value = "word") String word){

        if(!anagramService.deleteAllAnagramsOfWord(word)){
            return new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        }
    }

    private ResponseEntity<String> createResponseEntity(List<String> result ){

        if(result == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        }
        else {
            Gson gson = new Gson();
            AnagramGet anagramGet =  new AnagramGet(new ArrayList<>(result));
            return new ResponseEntity<>(gson.toJson(anagramGet), HttpStatus.OK);
        }
    }
}