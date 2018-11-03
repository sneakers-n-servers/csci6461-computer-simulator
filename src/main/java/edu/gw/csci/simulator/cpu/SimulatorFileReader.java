package edu.gw.csci.simulator.cpu;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class SimulatorFileReader {
    private static final int DEFAULT_WORD_STORE_INDEX = 512;
    private static final int DEFAULT_CODE = 32;
    public static HashMap<String, Integer> WordMap = new HashMap<>();
    private static int code = DEFAULT_CODE;
    private static int WordStoreIndex = DEFAULT_WORD_STORE_INDEX;


    public static void initializeFileReader() {
        WordMap.clear();
        code = DEFAULT_CODE;
        WordStoreIndex = DEFAULT_WORD_STORE_INDEX;
    }


    public static void readSentences(String fileName, CPU cpu) {
        File file = new File(fileName);
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            int tempChar;
            StringBuilder tempString = new StringBuilder();

            while ((tempChar = reader.read()) != -1) {
                if (((char) tempChar) == ' ') {
                    if (tempString.length() != 0) {
                        tempString.append((char) tempChar);
                    }
                } else {
                    tempString.append((char) tempChar);
                }
                if (((char) tempChar) == '.') {
                    String temp = tempString.toString();
                    cpu.consoleOutput.add(temp);
                    tempString.delete(0, tempString.length());

                    temp = temp.replaceAll("[\\pP‘’“”]", "");


                    for (String word : temp.split(" ")) {
                        word = word.toLowerCase();
                        encode(word);
                        StoreToMemory(WordStoreIndex, getCode(word), cpu);
                        WordStoreIndex += 1;
                    }
                    StoreToMemory(WordStoreIndex, 10, cpu);
                    WordStoreIndex += 1;
                }
            }
            StoreToMemory(WordStoreIndex, 11, cpu);
            WordStoreIndex += 1;
            //reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encode(String word) {
        if (!WordMap.containsKey(word)) {
            WordMap.put(word, code);
            code += (int) (Math.random() * 8 + 1);
        }
    }

    public static int getCode(String word) {
        return WordMap.get(word);
    }

    private static void StoreToMemory(int index, int value, CPU cpu) {
        cpu.StoreValue(index, value);
    }
}
