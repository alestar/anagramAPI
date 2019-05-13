# Anagram API

# Introduction
 This is the Anagram API application that provides functionalities to work with English anagrams. An anagram of a word is another english word that can be form by rearranging the letters that form the word in question (i.e nomad-monad). It's helpful to look up for words that are anagrams of a given words input, add new words as anagrams, etc.

# Implementation details
- For the development  application the following resources were used:
	- Spring Framework: A Java platform that provides comprehensive infrastructure support for developing Java applications
	- Spring boot: A platform to develop a stand-alone and production-grade spring application that can run, with minimum configurations and without the need for an entire Spring configuration setup.
	- Spring H2: A relational database management system written in Java. It can be embedded in Java applications or run in the client-server mode.
	- Mockito: A mocking framework, JAVA-based library that is used for effective unit testing of JAVA applications
	- JUnit: An open source framework designed for the purpose of writing and running tests in the Java programming language
	- JVM/JDK 8: The Java Development Kit used for creating this application
	
# Use Instruction
 Request done to the API endpoint are done through JSON in both direction meaning that each edp[oint most finish in a file.json and if it does return a result in would be in the form of JSON file as well. Fo example: 
 http://localhost:3000/anagrams/read.json would return  -> {"anagrams":["daer","dear","ared","dare"]}

# Current Features
- Adding words as anagrams to the corpus of word in database:
	POST querying to http://localhost:3000/words.json with body { "words": ["read", "dear", "dare"] } will this words as anagrams to the DB
	Note: If the words exist in BD the response will be an HTTP 201, but the word won't be reinserted in the DB

- Check if words are anagram in between each other:
	POST querying to http://localhost:3000/anagrams/words.json with body { "words": ["read", "dear", "dare"] } will return an HTTP 200 OK and message "This words are Anagrams between each others!"

- Fetch anagrams of a given english word: 
	GET querying to http://localhost:3000/anagrams/read.json will return {"anagrams":["daer","dear","ared","dare"]}
	
- Fetch anagrams of a given english word, using limit, will limit the output:
	GET querying to http://localhost:3000/anagrams/dare.json?limit=1  will return {"anagrams":["read"]}

- Fetch anagrams of a given english word, using permitPN( for permit Proper Noun), will include proper noun in the result:
	GET querying to http://localhost:3000/anagrams/monad.json?permitPN=true will return {"anagrams":["Damon","nomad"]}

- Fetch anagrams for a non english word, will return nothing:
	GET querying to http://localhost:3000/anagrams/zyxwv.json will return {"anagrams":[]}

- **Fetch Stats for Anagram DB:
	GET querying to http://localhost:3000/stats/words.json will return

- **Fetch anagrams for a group size = x
	GET querying http://localhost:3000/anagrams/size.json?groupSize=2 will return {"anagrams":["[aa]","[Ab, ba]","[ca]","[ad, da]","[ae, ea]","[fa]","[Ga, ga]","[Ah, ah, ha]","[ai]","[ka, ak]","[la, Al, al]","[Ma, ma, am]","[na, an]","[Ao]","[pa]","[ar, ra]","[As, as, sa]","[at, ta]","[aw, Wa, wa]","[ax]","[Ay, ay, ya]","[za]","[be]","[bo]","[Bu, bu]","[by]","[ce]","[de, Ed]","[di, id]","[Od, od, do]","[Td]","[Ud]","[fe]","[Ge, ge]","[eh, he]","[ie]","[el]","[Em, em, me]","[ne, en]","[oe]","[re, er]","[se, es]","[te]","[eu]","[we]","[ex]","[ey, ye]","[fi, if]","[of, Fo]","[fu]","[Gi]","[go, Og]","[ug]","[hi]","[Ho, ho, oh]","[sh]","[th]","[Hu]","[Hy]","[Ji]","[li]","[mi]","[in, ni]","[Io, io]","[pi]","[si, is]","[Ti, ti, it]","[wi]","[xi]","[Jo, jo]","[Ju]","[Ko, ko, Ok]","[Lo, lo]","[Lu]","[ly]","[Mo, mo, om]","[Mr]","[mu, um]","[ym, my]","[No, no, on]","[nu, un]","[yn]","[Po, po]","[or, Ro]","[Os, os, so]","[to]","[ow, wo]","[ox]","[yo]","[zo]","[pu, up]","[ur]","[yr]","[st]","[us]","[tu, ut]","[Vu]","[Wu]","[wy]"]}

- Fetch most anagrams will return the angrams with more words
	GET querying to http://localhost:3000/most/words.json will return {"anagrams":["organ","groan","orang","angor","grano","goran","argon","Orang","nagor","Ronga","rogan"]}
	
- Delete ALL! Wiped out corpus DB:
	DELETE querying to http://localhost:3000/words.json will return an HTTP 204 code "No content"
	
- Delete a word, will delete that word from the corpus DB:
	DELETE querying to http://localhost:3000/words/dear.json will return an HTTP 204 code "No content"

- Delete anagrams of a word will delete all the words that are anagrams to that word
	DELETE querying to http://localhost:3000/words/anagrams/dear.json will return an HTTP 200 code "OK"


# Other Features
- An endpoint that force a re-ingestion of a dictionary file information into the word corpus DB. (Implemented)
	GET querying http://localhost:3000/populate/words.json will return HTTP CODE 201 "Dictionary has been added to the corpus"
	
- An endpoint that return the difference distance between to words to become anagrams (i.e alcine and malice are distance '1')

# Edge Cases
Edge cases have been considarate for numeric and word valid inputs. When and invalid input is inserted in the query, most likely it will return a Bad Request HTTP code 400 error, but it can also return an internal error HTTP 500. In other cases, just return an empty result in the body.

# Design Consideration and Trade Off Overview

# Limits/Restrictions

# Installation

# Improvement
- Endpoint for addWordsAsAnagrams() should return a more comprehensive result for words that could successfully be added as a group of anagrams, or as individual angram and words that didn't fall in that category(invalid words)
- Design some sort of cache for DB most frequent/popular queries and preprocess views for really big queries that impact the performing of the DB
- For really big result returned (SELECT *) created some sort of pagination mechanism or parameter that allows to batch the query result into smaller pieces instead of returning the whole big chunk

