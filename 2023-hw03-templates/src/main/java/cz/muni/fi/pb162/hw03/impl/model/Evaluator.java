package cz.muni.fi.pb162.hw03.impl.model;

import cz.muni.fi.pb162.hw03.impl.parser.tokens.Commands;
import cz.muni.fi.pb162.hw03.impl.parser.tokens.Token;
import cz.muni.fi.pb162.hw03.impl.parser.tokens.Tokenizer;
import cz.muni.fi.pb162.hw03.template.TemplateException;
import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * class which takes input text and associated model from TemplateEngine
 * and with the help of Tokenizer class evaluates the input, executes the commands
 * @author Jan Maly
 */
public class Evaluator {
    private final TemplateModel model;
    private final String input;
    private final ArrayList<Object[]> tokenizedInput = new ArrayList<>();

    /**
     * constructor
     * @param input input text to be evaluated (so-called template)
     * @param model associated with this input
     */
    public Evaluator(String input, TemplateModel model) {
        this.model = model;
        this.input = input;
    }

    /**
     * processes the input, changes the string into the arrayList of pairs (Token.Kind : its content) called tokenizedInput
     *      the content of token is stored only with TEXT and NAME token kinds
     * this arrayList is then processed by other methods to execute the found commands
     * also serves as ground for isCorrectSyntax method
     * @param input string to be tokenized
     * @param result list where you want to the tokenizedInput be stored
     * @return tokenizedInput processed by the Tokenizer class
     */
    public static List<Object[]> getTokenizedInput(String input, List<Object[]> result) {
        Tokenizer tokenizer = new Tokenizer(input);
        while (!tokenizer.done()) {
            tokenizer.consume();
            if (tokenizer.getLastTokenKind() == Token.Kind.TEXT) {
                result.add(new Object[]{Token.Kind.TEXT, tokenizer.getLastToken().text()});
            } else if (tokenizer.getLastTokenKind() == Token.Kind.NAME) {
                result.add(new Object[]{Token.Kind.NAME, tokenizer.getLastToken().name()});
            } else {
                result.add(new Object[]{tokenizer.getLastTokenKind(), null});
            }
        }

        // this is very nasty part
        // tokenizer somehow adds after each found command pair (NAME : string representation of command),
        // so I just find these and delete them
        Token.Kind lastKind = null;
        String lastContent = null;
        for (int i = 0; i < result.size(); i++) {
            Token.Kind kind = (Token.Kind) result.get(i)[0];
            String content = (String) result.get(i)[1];
            if ((lastKind == Token.Kind.IF && lastContent == null &&
                    kind == Token.Kind.NAME && Objects.equals(content, Commands.IF)) ||
                    (lastKind == Token.Kind.ELSE && lastContent == null &&
                            kind == Token.Kind.NAME && Objects.equals(content, Commands.ELSE)) ||
                    (lastKind == Token.Kind.DONE && lastContent == null&&
                            kind == Token.Kind.NAME && Objects.equals(content, Commands.DONE)) ||
                    (lastKind == Token.Kind.FOR && lastContent == null &&
                            kind == Token.Kind.NAME && Objects.equals(content, Commands.FOR))) {
                result.remove(i);
            }
            lastKind = kind;
            lastContent = content;
        }
        return result;
    }

    /**
     * checks some syntax which should be kept in order to proceed successfully the evaluation
     * @return boolean whether the tests for correctness passed or not
     * @param input tokenized input which you want to check for syntax
     */
    public static boolean isCorrectSyntax(List<Object[]> input) {
        ArrayList<Token.Kind> kinds = new ArrayList<>();
        for (Object[] token : input) {
            kinds.add((Token.Kind) token[0]);
        }
        //there is the same amount of DONE as of IF and FOR together
        int ifCounter = Collections.frequency(kinds, Token.Kind.IF);
        int forCounter = Collections.frequency(kinds, Token.Kind.FOR);
        int doneCounter = Collections.frequency(kinds, Token.Kind.DONE);
        if (ifCounter + forCounter != doneCounter) {
            return false;
        }

        //all ELSE and DONE are wrapped in braces
        for (int i = 0; i < kinds.size(); i++) {
            if (kinds.get(i) == Token.Kind.ELSE || kinds.get(i) == Token.Kind.DONE) {
                if (kinds.get(i-1) != Token.Kind.OPEN || kinds.get(i+1) != Token.Kind.CLOSE) {
                    return false;
                }
            }
        }

        //construction of FOR looks like: OPEN FOR NAME IN NAME CLOSE ...
        for (int i = 0; i < kinds.size(); i++) {
            if (kinds.get(i) == Token.Kind.FOR) {
                if (kinds.get(i-1) != Token.Kind.OPEN || kinds.get(i+1) != Token.Kind.NAME
                        || kinds.get(i+2) != Token.Kind.IN || kinds.get(i+3) != Token.Kind.NAME
                        || kinds.get(i+4) != Token.Kind.CLOSE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * executes print; finds sequences {{ NAME }} and replaces them with the content associated with NAME in the model
     * then deletes the opening and closing braces
     *
     * restriction: for correct evaluation, you have to call executePrintCommand after executeForCommand
     */
    private void executePrintCommand() {
        Token.Kind prePreLast = null;
        Token.Kind preLast = null;
        Token.Kind last = null;
        int i = 0;
        while (i < tokenizedInput.size()) {
            prePreLast = preLast;
            preLast = last;
            last = (Token.Kind) tokenizedInput.get(i)[0];
            if (prePreLast == Token.Kind.OPEN && (preLast == Token.Kind.NAME) && (last == Token.Kind.CLOSE)) {
                // i is now on CLOSE position
                tokenizedInput.get(i-1)[0] = Token.Kind.TEXT;
                tokenizedInput.get(i-1)[1] = model.getAsString((String) tokenizedInput.get(i-1)[1]);
                tokenizedInput.remove(i);
                // there is some issue with indexes if you remove two times,
                // so the second remove is replaced by a replacement of an empty string
                tokenizedInput.get(i-2)[0] = Token.Kind.TEXT;
                tokenizedInput.get(i-2)[1] = "";
            }
            i++;
        }
        // these inserted empty strings are then removed
        for (int j = 0; j < tokenizedInput.size(); j++) {
            if (tokenizedInput.get(j)[0] == Token.Kind.TEXT && tokenizedInput.get(j)[1] == "") {
                tokenizedInput.remove(j);
            }

        }
    }

    /**
     * execute all if-else commands in the tokenizedInput, starts from the last one;
     * kinds is only auxiliary list of Token.Kind values in tokenizedInput used for counting the indexes of if, else, done commands
     *
     * restrictions: for correct evaluation, you have to call executeIfCommand as last
     *               does not support for command nested in if command
     */
    private void executeIfCommand() {
        //TODO je to neefektivní, protože vyhodnocuju všechny podmínky, i ty, do kterých se třeba pak nedostanu
        int ifCounter = 0;
        ArrayList<Token.Kind> kinds = new ArrayList<>();
        for (Object[] token : tokenizedInput) {
            kinds.add((Token.Kind) token[0]);
            if (token[0] == Token.Kind.IF) {
                ifCounter += 1;
            }
        }
        while (ifCounter > 0) {
            int ifIndex = kinds.lastIndexOf(Token.Kind.IF);
            int doneIndex = kinds.subList(ifIndex, kinds.size()).indexOf(Token.Kind.DONE) + ifIndex;
            // elseIndex is -1 if it is not present
            int elseIndex;
            if (!kinds.subList(ifIndex, doneIndex).contains(Token.Kind.ELSE)) {
                elseIndex = -1;
            } else {
                elseIndex = kinds.subList(ifIndex, doneIndex).indexOf(Token.Kind.ELSE) + ifIndex;
            }
            StringBuilder ifContent = new StringBuilder();
            if (model.getAsBoolean((String) tokenizedInput.get(ifIndex+1)[1])) {
                //name was false, inside if branch
                for (Object[] token : tokenizedInput.subList(ifIndex+3, tokenizedInput.size())) {
                    if (token[0] != Token.Kind.TEXT) {
                        break;
                    }
                    ifContent.append(token[1].toString());
                }
            } else {
                //name was false, else branch exists, inside else branch
                if (elseIndex != -1) {
                    for (Object[] token : tokenizedInput.subList(elseIndex+2, doneIndex)) {
                        if (token[0] != Token.Kind.TEXT) {
                            break;
                        }
                        ifContent.append(token[1].toString());
                    }
                }
            }
            Object[] ifResult = new Object[]{Token.Kind.TEXT, ifContent.toString()};
            //deleting
            tokenizedInput.subList(ifIndex-1, doneIndex+2).clear();
            kinds.subList(ifIndex-1, doneIndex+2).clear();
            tokenizedInput.add(ifIndex-1, ifResult);
            kinds.add(ifIndex-1, Token.Kind.TEXT);
            ifCounter -= 1;
        }
    }

    /**
     * executes foreach command, returns as output the core of for command as many times as is the length of the given list
     * the syntax should look like OPEN FOR ITEM(kind=NAME) IN LIST(kind=NAME) CLOSE CORE_OF_FOR_CYCLE OPEN DONE CLOSE
     * restrictions: for correct evaluation, you have to call executeForCommand before executePrintCommand
     *               does not support any commands nested in for command (deals with them as with a text)
     */
    private void executeForCommand() {
        int forCounter = 0;
        ArrayList<Token.Kind> kinds = new ArrayList<>();
        for (Object[] token : tokenizedInput) {
            kinds.add((Token.Kind) token[0]);
            if (token[0] == Token.Kind.FOR) {
                forCounter += 1;
            }
        }
        while (forCounter > 0) {
            StringBuilder forResult = new StringBuilder();
            int forIndex = kinds.lastIndexOf(Token.Kind.FOR);
            int doneIndex = kinds.subList(forIndex, kinds.size()).indexOf(Token.Kind.DONE)+forIndex;
            // the list of items which we iterate over
            Iterable<Object> list = model.getAsIterable((String) tokenizedInput.get(forIndex+3)[1]);
            //forIndex+5 is where the core of for cycle starts
            List<Object[]> input = new ArrayList<>(tokenizedInput.subList(forIndex + 5, doneIndex - 1));
            for (Object item : list) {
                TemplateModel actualModel = model.copy();
                actualModel.put((String) tokenizedInput.get(forIndex+1)[1], item);
                forResult.append(forCommandPrint(input, actualModel));
            }

            //delete whole for cycle, with opening and closing braces too
            tokenizedInput.subList(forIndex-1, doneIndex+2).clear();
            kinds.subList(forIndex-1, doneIndex+2).clear();
            //and insert the result at its place (forIndex-1 is index of the opening brace)
            //substring because of additional not wanted \n
            tokenizedInput.add(forIndex-1, new Object[]{Token.Kind.TEXT, forResult.substring(0, forResult.length()-1)});
            kinds.add(forIndex-1, Token.Kind.TEXT);
            forCounter -= 1;
        }
    }

    /**
     * takes the core of for command and evaluates it
     * @param input sublist of tokenizedInput to be evaluated by print command
     * @param model associated model (which changes with every call of for command)
     * @return evaluated input as one string
     */
    private String forCommandPrint(List<Object[]> input, TemplateModel model) {
        Token.Kind prePreLast = null;
        Token.Kind preLast = null;
        Token.Kind last = null;
        int i = 0;
        ArrayList<String> processedInputList = new ArrayList<>();
        while (i < input.size()) {
            prePreLast = preLast;
            preLast = last;
            last = (Token.Kind) input.get(i)[0];
            processedInputList.add((String) input.get(i)[1]);
            if (prePreLast == Token.Kind.OPEN && (preLast == Token.Kind.NAME) && (last == Token.Kind.CLOSE)) {
                // i is now on CLOSE position
                processedInputList.set(i-1, model.getAsString((String) input.get(i-1)[1]));
            }
            i++;
        }
        //get rid of non-text items (null values for commands or for empty texts)
        processedInputList.removeIf(token -> token == null || token.equals(""));
        StringBuilder processedInput = new StringBuilder();
        for (String token: processedInputList ) {
            //deals with starting \n chars
            if (token.startsWith("\n") && token.length() > 1) {
                processedInput.append(token.substring(1));
            } else {
                processedInput.append(token);
            }
        }
        return processedInput.toString();
    }

    /**
     * the executive public method of this class, calls other methods to evaluate the text
     * @return output = evaluated input
     */
    public String evaluate() {
        if (input == null) {
            throw new TemplateException("Empty template was given.");
        }
        getTokenizedInput(input, tokenizedInput);
        if (isCorrectSyntax(tokenizedInput)) {
            StringBuilder output = new StringBuilder();
            executeForCommand();
            executePrintCommand();
            executeIfCommand();
            for (Object[] token : tokenizedInput) {
                output.append(token[1]);
            }
            return output.toString();
        } else {
            throw new TemplateException("Something wrong in syntax when trying to evaluate the given template: "
                    + input);
        }
    }
}
