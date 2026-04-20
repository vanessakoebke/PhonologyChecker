import java.util.*;

public class Inventory {
    public static final List<String> inventory = parse(
            "i, y, ū, u, e, ø, ō, o, è, a, a, â, m, n , nj, ng, p, t, kj, k, b, d, g, gj, mh, nh, njh, ngh, s, z, l, hp, ht, hkj, hk, pm, tn, knj, kng, l, lh, h, bb, dd, gg, ggj");
    private static List<String> vowelsList = parse("i, y, ū, u, e, ø, ō, o, è, a, â");
    private static List<String> roundedList = parse("y, u, ø, o, â");
    private static List<String> unroundedList = parse("i, ū, e, ō, a");
    private static List<String> highList = parse("y, u, i, ū");
    private static List<String> lowList = parse("ø, o, â, e, ō, a");
    private static List<String> disallowedInitialList = parse("b, d, gj, g, z, bb, dd, gg, ggj");
    private static List<String> allowedFinalList = parse("i, y, ū, u, e, ø, ō, o, è, a, â, m, n");
    private static List<String> disallowedBetweenVowelsList = parse("p, t, kj, k");
    private static List<String> disallowedHighList = parse ("k, g, ng, hk, kng, gg");
    private static List<String> disallowedLowList = parse("kj, nj, njh, hkj, knj, ggj");

    public static void test(String s) throws PhonologicalError{
        // Parse into phonemes
        List<Phoneme> wordPhonemes = new LinkedList<>();
        int i = 0;
        while (i < s.length()) {
            boolean matched = false;

            for (String sound : Inventory.inventory) {
                if (s.toLowerCase().startsWith(sound, i)) {
                    wordPhonemes.add(new Phoneme(sound));
                    i += sound.length();
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new PhonologicalError(ErType.INPUT, s);
            }
        }
        // Test rules
        //Rule 1: word beginning
        Phoneme first = wordPhonemes.getFirst();
        if (!first.allowedInitial) {
            throw new PhonologicalError(ErType.BEGIN, s, first.symbol);
        }
        //Rule 2: word ending
        Phoneme last = wordPhonemes.getLast();
        if (!last.allowedFinal) {
            throw new PhonologicalError(ErType.END, s, last.symbol);
        }
      //Rule 3: disallowed consonants between vowels
        for (int p = 1; p < wordPhonemes.size() -1; p++) {
            if (!wordPhonemes.get(p).allowedBetweenVowels && wordPhonemes.get(p-1).vowel && wordPhonemes.get(p+1).vowel) {
                throw new PhonologicalError(ErType.MIDDLE, s, wordPhonemes.get(p).symbol);  
            }
        }
        //Rule 4: disallowed consonants with high vowels
        boolean highVow = false;  
        boolean disallHigh = false;
        String[] problem = new String[2];
        for (Phoneme p : wordPhonemes) {
            if (p.highness == 2) {
                highVow = true;
                problem[0] = p.symbol;
                if (disallHigh) {
                    throw new PhonologicalError(ErType.TOGETHER, s, problem);
                }
            }
            if (p.disallowedWithHigh) {
                disallHigh = true;
                problem[1] = p.symbol;
                if (highVow) {
                    throw new PhonologicalError(ErType.TOGETHER, s, problem);
                }
            }
        }
        
        //Rule 5: disallowed consonants with low vowels
        boolean lowVow = false;  
        boolean disallLow = false;
        problem = new String[2];
        for (Phoneme p : wordPhonemes) {
            if (p.highness == 0) {
                lowVow = true;
                problem[0] = p.symbol;
                if (disallLow) {
                    throw new PhonologicalError(ErType.TOGETHER, s, problem);
                }
            }
            if (p.disallowedWithLow) {
                disallLow = true;
                problem[1] = p.symbol;
                if (lowVow) {
                    throw new PhonologicalError(ErType.TOGETHER, s, problem);
                }
            }
        }
        
        //Rule 6: rounded and unrounded sounds cannot occur in the same word
        int wordRounded = 1;
        problem = new String[2];
        for (Phoneme p: wordPhonemes) {
            if (p.roundness == 2) {
                problem[0] = p.symbol;
                if (wordRounded == 0) {
                    throw new PhonologicalError(ErType.ROUND, s, problem);
                }
                wordRounded = 2;
            }
            if (p.roundness == 0) {
                problem[1] = p.symbol;
                if (wordRounded == 2) {
                    throw new PhonologicalError(ErType.ROUND, s, problem);
                }
                wordRounded = 0;
            }
        }
    }

    public static class Phoneme {
        final String symbol;
        final int roundness; // 0 = unround, 1 = neutral, 2 = rounded
        final int highness; // 0 = low, 1 = neutral, 2 = high
        final boolean allowedInitial;
        final boolean allowedFinal;
        final boolean allowedBetweenVowels;
        final boolean disallowedWithHigh;
        final boolean disallowedWithLow;
        final boolean vowel;

        public Phoneme(String s) {
            this.symbol = s;
            this.vowel = vowelsList.contains(s);
            this.allowedInitial = !disallowedInitialList.contains(s);
            this.allowedFinal = allowedFinalList.contains(s);
            this.allowedBetweenVowels = !disallowedBetweenVowelsList.contains(s);
            this.disallowedWithHigh = disallowedHighList.contains(s);
            this.disallowedWithLow = disallowedLowList.contains(s);
            // Roundness
            if (roundedList.contains(s)) {
                this.roundness = 2;
            } else if (unroundedList.contains(s)) {
                this.roundness = 0;
            } else {
                this.roundness = 1;
            }
            // Highness
            if (highList.contains(s)) {
                this.highness = 2;
            } else if (lowList.contains(s)) {
                this.highness = 0;
            } else {
                this.highness = 1;
            }
        }
    }

    private static List<String> parse(String input) {
        String[] array = input.split(", ");
        List<String> output = new ArrayList<>();
        for (String e : array) {
            output.add(e);
        }
        output.sort((x, y) -> Integer.compare(y.length(), x.length())); // Sort descending by length to find longest
                                                                        // segments first.
        return output;
    }
}
//Inventory: {i, y, ū, u, e, ø, ō, o, è, a, â, m, n , nj, ng, p, t, kj, k, b, d, g, gj, mh, nh, njh, ngh, s, z, l, hp, ht, hkj, hk, pm, tn, knj, kng, l, lh, h, bb, dd, gg, ggj}
//Vowels: {i, y, ū, u, e, ø, ō, o, è, a, â}
//
//Rounded class high: {y, u, è} 
//Unrounded class high: {i, ū, è}
//Disallowed with these vowels except è: {k, g, ng, hk, kng, gg}
//
//Rounded class low: {ø, o, è, â}
//Unrounded class low: {e, ō, è, a}
//Disallowed with these vowels except è: {kj, nj, njh, hkj, knj, ggj}
//
//Disallowed between vowels: {p, t, kj, k}
//Disallowed initially: {b, d, gj, g, z, bb, dd, gg, ggj}
//Allowed Wordfinal: {i, y, ū, u, e, ø, ō, o, è, a, â, m, n}
//Disallowed in one word: rounded and unrounded vowels
