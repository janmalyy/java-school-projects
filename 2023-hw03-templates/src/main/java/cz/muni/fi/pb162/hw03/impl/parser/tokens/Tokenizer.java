package cz.muni.fi.pb162.hw03.impl.parser.tokens;

import cz.muni.fi.pb162.hw03.template.utils.StringUtils;

/**
 * Consumes input and emits parser tokens
 */
public final class Tokenizer {

    /**
     * Modes in which this token consumes input
     */
    public enum Mode {
        /**
         * In text mode, when chunk matches a token
         * the rest of the input is untouched
         */
        TEXT(false),
        /**
         * In command mode, when chunk matches a token
         * all trailing whitespaces are discarded.
         */
        COMMAND(true),
        ;

        private final boolean skipWhiteSpace;

        Mode(boolean skipWhiteSpace) {
            this.skipWhiteSpace = skipWhiteSpace;
        }

        public boolean isSkipWhiteSpace() {
            return skipWhiteSpace;
        }
    }

    private String input;
    private Mode mode;
    private Mode nextMode;
    private Token lastToken;


    /**
     * Creates tokenizer
     *
     * @param input input string
     */
    public Tokenizer(String input) {
        this.input = input;
        this.mode = Mode.TEXT;
    }

    /**
     * Set mode of consumption
     *
     * @param mode new mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private void setNextMode(Mode mode) {
        this.nextMode = mode;
    }

    private void switchMode() {
        this.mode = (nextMode != null) ? nextMode : mode;
        this.nextMode = null;
    }

    /**
     * @return next token
     * @throws IllegalStateException on unexpected character
     */
    public Token get() {
        lastToken = switch (mode) {
            case TEXT -> getBlock();
            case COMMAND -> getCommand();
        };
        return lastToken;
    }

    private Token getBlock() {
        // OPEN TOKEN
        if (input.startsWith(Token.Kind.OPEN.getPrefix())) {
            setNextMode(Mode.COMMAND);
            return new Token(Token.Kind.OPEN);
        }

        // TEXT TOKEN
        var chunk = StringUtils.takeUntil(input, Token.Kind.OPEN.getPrefix());
        return new Token(Token.Kind.TEXT, chunk);
        // --
    }

    /**
     * getCommand distinguishes between different commands
     */
    private Token getCommand() {
        // one of command tokens
        if (input.startsWith(Token.Kind.CMD.getPrefix())) {
            String chunk = StringUtils.takeWhile(input, 1, Character::isLetterOrDigit);
            switch (chunk) {
                case Commands.IF -> {
                    return new Token(Token.Kind.IF);
                }
                case Commands.DONE -> {
                    return new Token(Token.Kind.DONE);
                }
                case Commands.ELSE -> {
                    return new Token(Token.Kind.ELSE);
                }
                case Commands.FOR -> {
                    return new Token(Token.Kind.FOR);
                }
                default -> {
                    return new Token(Token.Kind.CMD);
                }
            }
        }

        // ---

        // IN TOKEN
        if (input.startsWith(Token.Kind.IN.getPrefix())) {
            return new Token(Token.Kind.IN);
        }
        // ---

        // NAME TOKEN
        if (Character.isLetterOrDigit(input.charAt(0))) {
            var chunk = StringUtils.takeWhile(input, c-> Character.isLetterOrDigit(c) || c == '.');
            return new Token(Token.Kind.NAME, chunk);
        }
        // ---

        // CLOSE TOKEN
        if (input.startsWith(Token.Kind.CLOSE.getPrefix())) {
            setNextMode(Mode.TEXT);
            return new Token(Token.Kind.CLOSE);
        }
        // ---

        throw new UnexpectedTokenException("unknown");
    }


    /**
     * Skips next n character
     *
     * @param n number of characters to skip
     */
    public void skip(int n) {
        input = input.substring(n);
    }

    /**
     * Skips leading whitespace characters
     */
    public void skipWhitespace() {
        input = input.stripLeading();
    }


    /**
     * Skips up to {@code n} prefix whitespace characters
     *
     * @see StringUtils#stripLeadingWhitespace(String, int)
     * @param n max number of skipped whitespace characters
     */
    public void skipWhitespace(int n) {
        input = StringUtils.stripLeadingWhitespace(input, n);
    }


    /**
     * Combination of {@link #get()} and {@link #skip(int)}
     * to důležitý!
     * @return consumed token
     */
    public Token consume() {
        var token = get();
        switchMode();
        skip(token.getSize());

        if (mode.isSkipWhiteSpace()) {
            skipWhitespace();
        }

        return token;
    }

    /**
     * The same token as returned by last call to {@link #get()}
     * @return last token;
     */
    public Token getLastToken() {
        return lastToken;
    }

    /**
     * Shortcut for calling {@code getLastToken().getKind()}
     *
     * @return kind of the last consumed token or null
     */
    public Token.Kind getLastTokenKind() {
        return (lastToken != null) ? lastToken.getKind() : null;
    }

    /**
     * @return true if end of input was reached
     */
    public boolean done() {
        return input.isEmpty();
    }
}