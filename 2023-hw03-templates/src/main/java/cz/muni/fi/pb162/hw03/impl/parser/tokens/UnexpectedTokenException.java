package cz.muni.fi.pb162.hw03.impl.parser.tokens;

import cz.muni.fi.pb162.hw03.template.TemplateException;

/**
 * Exception representing problems with Tokens
 */
public class UnexpectedTokenException extends TemplateException {

    /**
     * Constructs a new unexpected token exception with the specified token
     *
     * @param token token which was not expected
     */
    public UnexpectedTokenException(Token token) {
        this(token.getKind().name());
    }

    /**
     * Constructs a new unexpected token exception with the token kind
     *
     * @param kind token kind which was not expected
     */
    public UnexpectedTokenException(String kind) {
        super("Unexpected token: " + kind);
    }

}
