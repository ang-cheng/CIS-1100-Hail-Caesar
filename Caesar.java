/**
* Execution: java Caesar encrypt/decrypt/crack ciphertext/plaintext key/english.txt
*
* Program Description: encrypts a message using a Caesar cipher key, decrypts
*                      an encrypted message using a Caesar cipher key, or
*                      calculates the Caesar cipher key of an encrypted message
*                      and decrypts the encrypted message
*/

public class Caesar {
    
    /*
    * Description: converts a string to a symbol array,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  the message text to be converted
    * Output: integer encoding of the message
    */
    public static int[] stringToSymbolArray(String str) {
        int[] integerArray = new int[str.length()];
        str = str.toUpperCase();

        for (int i = 0; i < str.length(); i++) {
            char letter = str.charAt(i);
            int asciiRepresentation = (int) letter;
            integerArray[i] = letter - 'A';
        }

        return integerArray;
    }
    
    /*
    * Description: converts an array of symbols to a string,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  integer encoding of the message
    * Output: the message text
    */
    public static String symbolArrayToString(int[] symbols) {
        String str = "";

        for (int i = 0; i < symbols.length; i++) {
            char letter = (char) (symbols[i] + 'A');
            str += letter;
        }

        return str;
    }
    
    /**
    * Description: shifts the integer reprsentation of an unencrypted symbol
    *              over by an integer amount, giving the encrypted version 
    *              of the symbol.
    * Input: unencrypted integer representation of the char/symbol
    * Output: encrypted integer version of the symbol
    */
    public static int shift(int symbol, int offset) {
        int shiftedSymbol;

        if (0 <= symbol && symbol <= 25) { // checks if symbol is a letter
            if (symbol + offset > 25) {
                shiftedSymbol = ((symbol + offset) % 25) - 1;
            } else {
                shiftedSymbol = symbol + offset;
            }
            return shiftedSymbol;
        } else { // if symbol is not a letter
            return symbol;
        }
    }
    
    /**
    * Description: takes the encrypted version integer version of a symbol
    *              and unencrypts it using the key, returning the
    *              unencrypted version of the symbol.
    * Input: encrypted integer version of the symbol
    * Output: unencrypted integer version of the symbol
    */
    public static int unshift(int symbol, int offset) {
        int unshiftedSymbol;

        if (0 <= symbol && symbol <= 25) { // if symbol is a letter
            if (symbol - offset < 0) {
                // temporary integer determining how much 'wrapping' around the
                // alphabet is necessary
                int temp = offset - symbol - 1;
                unshiftedSymbol = 25 - temp;
            } else {
                unshiftedSymbol = symbol - offset;
            }

            return unshiftedSymbol;
        } else { // if symbol is not a letter
            return symbol;
        }
    }
    
    /**
    * Description: takes an unencrypted string message and a given 
    *              integer key, and encrypts the message using the key. only 
    *              encrypts uppercase characters.
    * Input: unencrypted string message
    * Output: encryptd string message
    */
    public static String encrypt(String message, int key) {
        int[] symbolsArray = new int[message.length()];
        symbolsArray = stringToSymbolArray(message);

        for (int i = 0; i < message.length(); i++) {
            symbolsArray[i] = shift(symbolsArray[i], key);
        }

        String encryptedMessage = symbolArrayToString(symbolsArray);

        return encryptedMessage;
    }
    
    /**
    * Description: takes an encrypted string message and a given integer
    *              key, and then unencrypts the message using the key. will only
    *              unencrypt uppercase characters.
    * Input: encrypted string message
    * Output: unencrypted string message
    */
    public static String decrypt(String cipher, int key) {
        int[] symbolsArray = new int[cipher.length()];
        symbolsArray = stringToSymbolArray(cipher);

        for (int i = 0; i < cipher.length(); i++) {
            symbolsArray[i] = unshift(symbolsArray[i], key);
        }

        String unencryptedMessage = symbolArrayToString(symbolsArray);

        return unencryptedMessage;
    }
    
    /**
    * Description: takes the file, english.txt, containing letter frequencies,
    *              and reads them into a double array with each index
    *              corresponding to the frequency of a letter.
    * Input: file name in the form of a string
    * Output: a double array containing indices corresponding to letter
    *         frequencies
    */
    public static double[] getDictionaryFrequencies(String filename) {
        double[] letterFrequencies = new double[26];
        In fileRead = new In(filename);
        
        for (int i = 0; i < 26; i++) {
            letterFrequencies[i] = fileRead.readDouble();
        }

        return letterFrequencies;
    }
    
    /**
    * Description: takes an integer representation of a text and
    *              calculates the frequencies at which letters appear
    *              in the text.
    * Input: integer representation of the text
    * Output: double array containing the unique frequencies of letters in
    *         the text
    */
    public static double[] findFrequencies(int[] symbols) {
        double[] frequencies = new double[26];
        int count = 0;

        for (int i = 0; i < symbols.length; i++) {
            if (0 <= symbols[i] && symbols[i] <= 25) { // checks if is a letter
                frequencies[symbols[i]] += 1.0;
                count++;
            }
        }

        for (int i = 0; i < 26; i++) {
            frequencies[i] = frequencies[i] / count;
        }

        return frequencies;
    }
    
    /**
    * Description: tells us how close the encrypted message is to
    *              decryted English, in the form of a totaled double 
    *              scoreFrequencies
    * Inputs: double array containing letter frequencies of the 
    *         english language and a double array containing
    *         letter frequencies of the encryted text
    * Output: a double value that tells us how close to decryted
    *         English the text is
    */
    public static double scoreFrequencies(double[] english, double[] currentFreqs) {
        double totalScore = 0.0;

        for (int i = 0; i < english.length; i++) {
            totalScore += Math.abs(english[i] - currentFreqs[i]);
        }

        return totalScore;
    }

    /**
    * Description: calculates the integer key needed to unencrypt an
    *              encrypted file, by texting each individual possible 
    *              letter key, and determining which key has the lowest
    *              score frequency.
    * Input: the encrypted message and the file containing standard
    *        letter frequencies in the English language
    * Output: the key that is needed to unencrypt the encrypted message
    *         in integer form
    */
    public static int crack(String encryptedFile, String englishText) {
        double[] englishFreqs = getDictionaryFrequencies(englishText);
        double minimum = 1.0;
        int lowestKey = 0;

        for (int i = 0; i < 26; i++) { // for each of the 26 possible keys
            String unshiftedText = decrypt(encryptedFile, i);
            int[] symbolArray = stringToSymbolArray(unshiftedText);
            double[] textFreqs = findFrequencies(symbolArray);
            double freqScore = scoreFrequencies(englishFreqs, textFreqs);

            if (freqScore < minimum) {
                minimum = freqScore;
                lowestKey = i;
            }
        }

        return lowestKey;
    }
    
    public static void main(String[] args) {
        String function = args[0];
        String fileName = args[1];

        In inStream = new In(fileName);

        if (function.equals("encrypt") || function.equals("decrypt")) {
            int key = (char) args[2].charAt(0);
            key = key - 'A';

            if (function.equals("encrypt")) { // if user encrypts
                String text = inStream.readAll();
                System.out.println(encrypt(text, key));
            } else if (function.equals("decrypt")) { // if user decrypts
                String text = inStream.readAll();
                System.out.println(decrypt(text, key));
            }
        } else if (function.equals("crack")) { // if user cracks
            String englishFile = args[2];
            String text = inStream.readAll();
            int key = crack(text, englishFile);
            System.out.println(decrypt(text, key));
        }
    }
    
}
