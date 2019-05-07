package com.anagrams.project.controller;

import com.anagrams.project.model.AnagramGet;
import com.anagrams.project.model.AnagramPost;
import com.anagrams.project.model.StatsResource;
import com.anagrams.project.service.AnagramService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
public class Controller {
    private static Logger logger= Logger.getLogger(Controller.class.getName());

    @Autowired
    private AnagramService anagramService;

    @PostMapping("/words.json")
    public @ResponseBody ResponseEntity<String> addWordsAsAnagram(@RequestBody AnagramPost anagramPost){

        try {
            anagramService.addWordsAsAnagram(anagramPost.getWords());
        }
        catch (Exception e){
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("List of words has been added to the corpus", HttpStatus.CREATED);
    }

    @PostMapping("/anagrams/words.json")
    public @ResponseBody ResponseEntity<String> areAnagrams(@RequestBody AnagramPost anagramPost){

        try {
            if(anagramService.areAnagrams(anagramPost.getWords())){
                return new ResponseEntity<>("This words are Anagrams between each others!", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/anagrams/{word}.json")
    public @ResponseBody ResponseEntity<String> fetchAnagramWords(@PathVariable(value = "word") String word, @RequestParam(defaultValue="-1", required = false) Integer limit,
                                                                    @RequestParam (defaultValue="true", required = false)Boolean permitPN){
        Set<String> result;
        logger.info("Passing param limit: " + limit);
        logger.info("Passing param permitPN: " + permitPN);
        result = anagramService.fetchAnagramsOfWord(word, limit, permitPN);//Return All

        if(result == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        }
        else {
            Gson gson = new Gson();
            AnagramGet anagramGet =  new AnagramGet(new ArrayList<>(result));
            return new ResponseEntity<>(gson.toJson(anagramGet), HttpStatus.OK);
        }
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

    @GetMapping("/stats/words.json")
    public @ResponseBody ResponseEntity<String> fetchStats(){

        StatsResource statsResource=anagramService.fetchStatsResource();

        if(statsResource == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        }
        else {
            Gson gson = new Gson();
            return new ResponseEntity<>(gson.toJson(statsResource), HttpStatus.OK);
        }
    }

    @GetMapping("/most/words.json")
    public @ResponseBody ResponseEntity<String> fetchMostAnagramsWords(){

        Set<String> result=anagramService.fetchMostAnagramsWords();

        if(result == null) {
            return new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        }
        else {
            Gson gson = new Gson();
            AnagramGet anagramGet =  new AnagramGet(new ArrayList<>(result));
            return new ResponseEntity<>(gson.toJson(anagramGet), HttpStatus.OK);
        }
    }

    @GetMapping("/anagrams/size.json")
    public @ResponseBody ResponseEntity<String> fetchAnagramsGroupOfSize( @RequestParam String groupSize){

        Set<String> result = anagramService.anagramGroupOfSize(Integer.valueOf(groupSize));

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