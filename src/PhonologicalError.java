public class PhonologicalError extends Error {
    private ErType type;
    private String word;
    private String[] sounds;
    private String message;
    
    public PhonologicalError(ErType type, String word) {
        if (type == ErType.INPUT) {
            message =  "Word " + word + ": Input contains a sequence that is not part of the sound inventory.";
        }
    }

    public PhonologicalError(ErType type, String word, String sound) {
        this.word = word;
        message = "Word " + word + ": ";
        switch (type) {
        case BEGIN: message += sound + " cannot appear at the beginning of the word."; break;
        case END: message += sound + " cannot appear at the end of the word."; break;
        case MIDDLE: message += sound + " cannot appear between vowels."; break;
        default: message += "There is something wrong with the word.";
        }
    }

    public PhonologicalError(ErType type, String word, String[] sound) {
        this.word = word;
        message = "Word " + word + ": ";
        switch(type) {
        case TOGETHER: message += sound[0] + " und " + sound[1] + " cannot appear in the same word."; break;
        case ROUND: message += sound[0] + " und " + sound[1] + " cannot appear in the same word due to roundness harmony."; break;
        default: message += "There is something wrong with the word.";
        }
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
}
