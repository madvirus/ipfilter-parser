package ipfilterdsl;

import ipfilterdsl.token.Token;

public class ParseFailException extends RuntimeException {
    private final Token token;

    public ParseFailException(String msg, Token token) {
        super(msg);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
