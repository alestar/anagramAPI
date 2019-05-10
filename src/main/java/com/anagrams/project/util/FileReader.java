package com.anagrams.project.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

@Component
public class FileReader {
    private static Logger logger= Logger.getLogger(FileReader.class.getName());

    private final String DIC_PATH = "files/dictionary.txt";//Handle multiple OS for file loading
    private List<String> listOfLines= new ArrayList<>();

    public List<String> getListOfLines() {
        return listOfLines;
    }

    private void readTextFile(String filepath) {
        try {
            Path path = Paths.get(filepath);
            logger.info("Dictionary File path: " + path.toString());
            logger.info("user.dir : " + System.getProperty("user.dir"));

            Scanner sc = new Scanner(path.toFile());

            while (sc.hasNextLine()) {
                listOfLines.add(sc.nextLine());      //listOfLines = Files.readAllLines(path);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readDictionaryFile(){
        readTextFile(DIC_PATH);
    }
}
