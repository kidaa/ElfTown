package Simulation;

import java.util.*;
/**
 *
 * @author Alex Mulkerrin
 */
public class RandomGenerator {
    Random random;
    public int seed;
    
    public RandomGenerator(int seeded) {
        seed=seeded;
        random = new Random();
        random.setSeed(seed);
    }
    
    public int getSeed() {
        return seed;
    }
    
    public void resetSeed(int seeded) {
        seed=seeded;
        random.setSeed(seed);
    }
    
    public int integer(int bound) {
        return random.nextInt(bound);
    }
    
    public boolean bool() {
        return random.nextBoolean();
    }
    
    public String name(int length) {
        char[] vowels = new char[]{'a','e','i','o','u'};
        char[] consonants = new char[]{'w','l','f','s','y'};
        String text="", result="";
            text="";
            int wordLength= length;
            int letterType = random.nextInt(2);
            for (int j=0; j<wordLength; j++) {
                if (letterType==0) {
                    text += randomChoice(consonants);
                    letterType++;
                } else {
                    text += randomChoice(vowels);
                    letterType=0;
                }
                if (j==0) text = text.toUpperCase();
            }
            result += text;
        return result;
    }
    
    public char randomChoice(char[] choices) {
        int pick = random.nextInt(choices.length);
        return choices[pick];
    }
}
