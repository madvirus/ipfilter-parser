package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

public class ParseResult {
    private boolean success;
    private String value;
    private TokenBuffer tokenBuffer;

    public ParseResult(boolean success, String value, TokenBuffer tokenBuffer) {
        this.success = success;
        this.value = value;
        this.tokenBuffer = tokenBuffer;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getValue() {
        return value;
    }

    public TokenBuffer getTokenBuffer() {
        return tokenBuffer;
    }

    public static ParseResult success(TokenBuffer tokenBuffer) {
        return new ParseResult(true, "", tokenBuffer);
    }
    public static ParseResult fail(TokenBuffer tokenBuffer) {
        return new ParseResult(false, "", tokenBuffer);
    }

}
