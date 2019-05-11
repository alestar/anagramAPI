package com.anagrams.project.entity;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="anagram")
public class Anagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Integer length;

    private String words;

    private Integer volume;

    public Anagram() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anagram anagram = (Anagram) o;
        return Objects.equals(id, anagram.id) &&
                Objects.equals(token, anagram.token) &&
                Objects.equals(length, anagram.length) &&
                Objects.equals(words, anagram.words) &&
                Objects.equals(volume, anagram.volume);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, length, words, volume);
    }
}
