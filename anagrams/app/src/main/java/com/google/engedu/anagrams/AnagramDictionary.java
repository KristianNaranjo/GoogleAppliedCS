/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, List<String>> lettersToWord = new HashMap<>();
    private Map<Integer,List<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);
            wordList.add(word);

            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }
            else{
                List<String> value = new ArrayList<>();
                value.add(word);
                sizeToWords.put(word.length(),value);
            }

            String key = sortString(word);
            if(lettersToWord.containsKey(key)){
                lettersToWord.get(key).add(word);
            }
            else{
                List<String> value = new ArrayList<>();
                value.add(word);
                lettersToWord.put(key,value);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String dWord:wordList){
            if(dWord.length() == targetWord.length()) {
                String sTarget = sortString(targetWord);
                if (sortString(dWord).equals(sTarget)) {
                    if(isGoodWord(dWord,targetWord))
                        result.add(dWord);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char abc = 'a'; abc <= 'z'; abc++){
            String wordPlusLetter = word + abc;
            String key = sortString(wordPlusLetter);
            if(lettersToWord.containsKey(key)){
                for(String entry:lettersToWord.get(key)) {
                    if(isGoodWord(entry,wordPlusLetter))
                        result.add(entry);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithTwoMoreLetters(String word){
        ArrayList<String> result = new ArrayList<>();
        for(char abc = 'a'; abc <= 'z'; abc++){
            for(char abc2 = abc; abc2 <= 'z'; abc2++){
                String wordPlusLetters = word + abc + abc2;
                String key = sortString(wordPlusLetters);
                if(lettersToWord.containsKey(key)){
                    for(String entry:lettersToWord.get(key)){
                        if(isGoodWord(entry,wordPlusLetters))
                            result.add(entry);
                    }
                }
            }
        }
        return result;
    }


    public String pickGoodStarterWord() {
        String word = "";
        boolean found = false;
        do {
            int maxIndex = sizeToWords.get(wordLength).size()-1;
            int initial = random.nextInt(maxIndex);
            for(int i=0; i<= maxIndex; i++){
                word = sizeToWords.get(wordLength).get(initial % maxIndex);
                initial++;
                if(lettersToWord.get(sortString(word)).size() >= MIN_NUM_ANAGRAMS){
                    found = true;
                    break;
                }
            }
            if(wordLength != MAX_WORD_LENGTH)
                wordLength++;
        }while(!found);
        return word;
    }

    public String sortString(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;
    }
}
