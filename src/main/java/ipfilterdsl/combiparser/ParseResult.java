package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

public class ParseResult<R> {
    private boolean success;
    private R value;
    private TokenBuffer tokenBuffer;

    public ParseResult(boolean success, R value, TokenBuffer tokenBuffer) {
        this.success = success;
        this.value = value;
        this.tokenBuffer = tokenBuffer;
    }

    public boolean isSuccess() {
        return success;
    }

    public R getValue() {
        return value;
    }

    public TokenBuffer getTokenBuffer() {
        return tokenBuffer;
    }

}
