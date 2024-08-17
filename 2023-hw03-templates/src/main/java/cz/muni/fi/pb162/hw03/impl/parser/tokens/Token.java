package cz.muni.fi.pb162.hw03.impl.parser.tokens;


import java.util.Objects;


/**
 * Representation of parser token
 */

public final class Token {

    /**
     * Enumeration of token kinds
     */
    public enum Kind {
        TEXT,
        NAME,
        OPEN("{{"),
        CLOSE("}}"),
        CMD("#"),
        IF("#"),
        FOR("#"),
        DONE("#"),
        ELSE("#"),
        IN(":"),
        ;

        private final String prefix;

        Kind(String prefix) {
            this.prefix = prefix;
        }

        Kind() {
            this(null);
        }

        public String getPrefix() {
            return prefix;
        }
    }

    private final Kind kind;
    private final String chunk;

    /**
     * Creates token of given kind
     *
     * @param kind kind of token
     * @param chunk part of input corresponding to this token
     */
    public Token(Kind kind, String chunk) {
        this.kind = Objects.requireNonNull(kind);
        this.chunk = Objects.requireNonNull(chunk);
    }

    /**
     * Creates token of given kind
     *
     * @param kind kind of token
     */
    public Token(Kind kind) {
        this(kind, kind.getPrefix());
    }

    /**
     * @throws UnexpectedTokenException if token is not {@link Kind#TEXT}
     * @return text
     */
    public String text() {
        requireTokenKind(Kind.TEXT);
        return chunk;
    }

    /**
     * @throws UnexpectedTokenException if token is not {@link Kind#NAME}
     * @return name
     */
    public String name() {
        requireTokenKind(Kind.NAME);
        return chunk.strip();
    }

    /**
     * @throws UnexpectedTokenException if token is not {@link Kind#CMD}
     * @return command name
     */
    public String cmd() {
        requireTokenKind(Kind.CMD);
        return chunk.substring(1);
    }

    private void requireTokenKind(Kind expected) {
        if (kind != expected) {
            throw new UnexpectedTokenException(kind.name());
        }
    }
    /**
     * @return size of matching {@link #chunk}
     */
    public int getSize() {
        return chunk.length();
    }

    /**
     * @return kind of this token
     */
    public Kind getKind() {
        return kind;
    }
}