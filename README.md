# Deliverable


# Introduction

# Features
- An endopoint that force a reingestion of a dictionary information in case the word corpus DB is wiped out
- 

# Implementation detais
- For the development  aplication the following resources were used

- Spring Framework: A Java platform that provides comprehensive infrastructure support for developing Java applications
- Spring boot: A platform to develop a stand-alone and production-grade spring application that can run, with minimum configurations and without the need for an entire Spring configuration setup.
- Spring H2: A relational database management system written in Java. It can be embedded in Java applications or run in the client-server mode.
- Mockito: A mocking framework, JAVA-based library that is used for effective unit testing of JAVA applications
- JUnit: An open source framework designed for the purpose of writing and running tests in the Java programming language
- JVM/JDK 8: The Java Development Kit used for creating this aplication

# Edge Cases

# Desing Consideration and Trade Off Overview

# Limits/Restrictions

# Installation

# Improvement
- Endpoint for addWordsAsAnagrams() should return a more comprehensive result for words that could succesfully be added as a group of anagrams, or as individual angram and words that didn't fall in that category(invalid words)
- Desing some sort of cache for DB most frequent/popular queries and preproceed views for really big quireies that impact the performing of the DB
- For really big result returned (SELECT *) created some sort of pagination mechanism or parameter that allows to batch the querie result into smaller pieces instead of returning the whole big chunk