# Anagram API

# Introduction
 This is the Anagram API application that provides services to work with English anagrams. An anagram of a word is another english word that can be form by rearranging the letters that form the word in question (i.e nomad-monad). It's helpful to look up for words that are anagrams of a given words input, add new words as anagrams, etc.

# Implementation details
 - For the development  application the following resources were used:
	- Spring Framework: A Java platform that provides comprehensive infrastructure support for developing Java applications.
	- Spring boot: A platform to develop a stand-alone and production-grade spring application that can run, with minimum configurations and without the need for an entire Spring configuration setup.
	- Spring H2: A relational database management system written in Java. It can be embedded in Java applications or run in the client-server mode.
	- Mockito: A mocking framework, JAVA-based library that is used for effective unit testing of JAVA applications.
	- JUnit: An open source framework designed for the purpose of writing and running tests in the Java programming language.
	- JVM/JDK 8: The Java Development Kit used for creating this application.
	
# Current Features
 - Adding words as anagrams to the corpus of word in database:
	- POST querying to: `http://localhost:3000/words.json` with body: `{ "words": ["read", "dear", "dare"] }` will this words as anagrams to the DB.
	Note: If the words exist in BD the response will be an HTTP 201, but the word won't be reinserted in the DB

 - Check if words are anagram in between each other:
	- POST querying to: `http://localhost:3000/anagrams/words.json` with body: `{ "words": ["read", "dear", "dare"] }` will return an HTTP 200 OK and message "This words are Anagrams between each others!".

 - Fetch anagrams of a given english word: 
	- GET querying to: `http://localhost:3000/anagrams/read.json` will return: `{"anagrams":["daer","dear","ared","dare"]}`.
	
 - Fetch anagrams of a given english word, using limit, will limit the output:
	- GET querying to: `http://localhost:3000/anagrams/dare.json?limit=1`  will return: `{"anagrams":["read"]}`.

 - Fetch anagrams of a given english word, using permitPN( for permit Proper Noun), will include proper noun in the result:
	- GET querying to: `http://localhost:3000/anagrams/monad.json?permitPN=true` will return: `{"anagrams":["Damon","nomad"]}`.

 - Fetch anagrams for a non english word, will return nothing:
	- GET querying to `http://localhost:3000/anagrams/zyxwv.json` will return: `{"anagrams":[]}`.

 - Fetch Stats for Anagram DB:
	- GET querying to: `http://localhost:3000/stats/words.json` will return: `{"minWordsLength":1,"maxWordsLength":24,"medianWordsLength":10,"averageWordLength":9}`.

 - Fetch anagrams for a group size >= x
	- GET querying to: `http://localhost:3000/anagrams/size.json?groupSize=24` will return: `{"anagrams":["[scientificophilosophical]","[formaldehydesulphoxylate]","[tetraiodophenolphthalein]","[thyroparathyroidectomize]","[pathologicopsychological]"]}`.

 - Fetch most anagrams will return the anagrams with more words
	- GET querying to `http://localhost:3000/most/words.json` will return: `{"anagrams":["organ","groan","orang","angor","grano","goran","argon","Orang","nagor","Ronga","rogan"]}`.
	
 - Delete ALL! Wiped out corpus DB:
	- DELETE querying to: `http://localhost:3000/words.json` will return an HTTP 204 code "No content".
	
 - Delete a word, will delete that word from the corpus DB:
	- DELETE querying to: `http://localhost:3000/words/dear.json` will return an HTTP 204 code "No content".

 - Delete anagrams of a word will delete all the words that are anagrams to that word
	- DELETE querying to: `http://localhost:3000/words/anagrams/dear.json` will return an HTTP 200 code "OK".

# Other Features
 - An endpoint that force a re-ingestion of a dictionary file information into the word corpus DB. (Implemented).
	- GET querying to: `http://localhost:3000/populate/words.json` will return HTTP CODE 201 "Dictionary has been added to the corpus".
	
 - An endpoint that return the difference distance between to words to become anagrams (i.e `alcine` and `malice` are `distance = '1'`). Not available yet.

# Edge Cases
 - Edge cases have been considerate for numeric and word valid inputs. When and invalid input is inserted in the query, most likely it will return a Bad Request HTTP code 400 error, but it can also return an internal error HTTP 500. In other cases, just return an empty result in the body.

# Design Consideration and Trade Off Overview
- A relation database is used to persist information since the information is heavily queried but al mont never modified adn relational data base are usually optimized for more complex queries. If in the future modification to the data base are more frequent and reads request start to escalate, maybe a consideration for a NO SQL database.

# Limits/Restrictions
- Current endpoint can handle limit and edge cases for parametrized queries, but there me some queries that are fast that other, and in those scenarios should be optimized. For example: Fetch anagrams for a group size >= 2 takes some time to retrieve all Anagrams size >= 2.

# Installation (Deliverable)
 -  To run this application you would need JDK 1.8 or later installed in your computer. To check which version you have or if it's installed at all use: `java -version`.
  Then either compile the source code provided into a Jar file or used the one given with the following command: `java -jar AnagramAPI.jar`.

# Use Instruction
 - Once the application jar start working a web server to send request will be available at:  `http://localhost:3000`. Request send to the API endpoints are done through JSON in both direction, meaning that each endpoint most finish in a file.json, body of the request is pass as JSON and if it does return a result in the body as well, it would be in the form of JSON file as well. For example: 
 `http://localhost:3000/anagrams/read.json` would return: `{"anagrams":["daer","dear","ared","dare"]}`.

# Improvement
 - Endpoint for addWordsAsAnagrams() should return a more comprehensive result for words that could successfully be added as a group of anagrams, or as an individual anagram and  finally words that did not fall in those categories(invalid words).
 - Design some sort of cache for DB most frequent/popular queries and preprocess views for really big queries that impact the performing of the DB.
 - For really big result returned (SELECT *) created some sort of pagination mechanism or parameter that allows to batch the query result into smaller pieces instead of returning the whole big chunk.
 - Validate that words are actually English words, using some sor of linguistic resource. Given the scenario that the entire word corpus is deleted from the DB and there is none dictionary file to ingest or it has not been ingested yet, then when trying to add a new word valid or invalid there is no why to know if it's an actual valid English word since there is not previous information to compare the word to.

