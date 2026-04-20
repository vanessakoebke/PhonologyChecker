# The Phonology Checker

The PhonologyChecker allows to check words against a set of feature-based phonological rules. The app provides a simple window where the input
can be inserted (each word in a separate line). After clicking on "Check", the result is displayed in the same window.

## Motivation
This project was created because a friend of mine - a linguist and a Game Master in Dungeons and Dragons -
wanted to construct a language for one of his campaigns. He created a set of phonological rules, i.e. rules about what sounds can go together in a word.  
Example: "ren" obeys the phonological rules of English (or German), whereas "nre" does not.

One of the main (and more exotic) features of his language is [roundness harmony](https://en.wikipedia.org/wiki/Vowel_harmony), i.e. a rounded and an unrounded vowel cannot coexist in the same word.  
Example: "obu" is a possible word in his language (note how you round your lips when you pronounce "o" or "u"), however, "obi" is not
(note how you tighten your lips when you pronounce "i").

He asked me to write a program to help him check whether a word he invented obeyed the phonological rules of his language.

## Input for this project: the phonological rules
The input I received from my friend was the following inventory of sounds:  
{i, y, ū, u, e, ø, ō, o, è, a, â, m, n, nj, ng, p, t, kj, k, b, d, g, gj, mh, nh, njh, ngh, s, z, l, hp, ht, hkj, hk, pm, tn, knj, kng, l, lh, h, bb, dd, gg, ggj}

The following classification of vowels:  
- Rounded class high: {y, u, è} 
- Unrounded class high: {i, ū, è}
- Rounded class low: {ø, o, è, â}  
- Unrounded class low: {e, ō, è, a}

The following restrictions for consonants:
- Disallowed with high vowels except è: {k, g, ng, hk, kng, gg}  
- Disallowed with low vowels except è: {kj, nj, njh, hkj, knj, ggj}  
- Disallowed between vowels: {p, t, kj, k}  
- Disallowed at the beginning of a word: {b, d, gj, g, z, bb, dd, gg, ggj}  
- Allowed at the end of a word: {i, y, ū, u, e, ø, ō, o, è, a, â, m, n}  
- Disallowed in the same word (roundness harmony): rounded and unrounded vowels

## Challenges
Some sounds are orthographically represented by more than one letter, e.g. in German or English "ph" often stands for the sound "f". Therefore, 
when parsing the word into phonemes (sound units), the longest match within the phonological inventory needs to be selected. E.g. when parsing the word "philosophy"
the "ph" should be parsed as one phoneme instead of two ("p" + "h"). To accomplish this, the phonological inventory is sorted in descending order
and the first (and therefore longest) match is used.

In linguistic theory after Chomsky, features are normally viewed as binary, e.g. the roundness feature of a given sound is either true or false. 
However, the sound *schwa* (it's the "e" in "the") is often described as being neither high nor low, neither rounded nor unrounded. If it were not
for *schwa*, the rules could have been implemented in a much more elegant and succint manner, using booleans. However, the existence of *schwa* required
a more fine-grained approach. For this purpose instead of using booleans I used integers, with 0 being low/unrounded, 2 being high/rounded and 
1 being neutral and exclusively reserved for *schwa*.

## Implementation

- The GUI was created using Java Swing.
- The Phoneme class contains a set of attributes which represent the features (roundness, highness, vowel/consonant) and the restrictions for the sound
- The Inventory class possesses a static utility function test(String s) which checks the argument s against the phonological rules, and throws an error if the word violates a rule
- The PhonologicalError class stores information about the word in which the error occurred, the error type and the sound(s) that caused the error.
- The "Check" Button iterates through each word in the input field and displays possible errors.

## Usage

1. Run the application.
2. Enter one or multiple words (one word per line).
3. Click "Check".
4. The results will be displayed in the window.

## Outlook
The phonological rules are hard-coded, since currently my friend is only developing one language. If in the future, more languages - each with
its own set of rules - should be checked, it might be helpful to allow the user to input the phonological rules into the program, store them
persistently with the corresponding language, and select against which language's rules the input words should be checked.

## License

This project is licensed under the MIT License – see the LICENSE file for details.
