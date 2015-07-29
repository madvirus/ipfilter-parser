package ipfilterdsl.combiparser;

import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

public class TerminalParser<R> extends Parser<String, R> {
    private TokenType type;

    public TerminalParser(TokenType type, Action<String, R> action) {
        super(action);
        this.type = type;
    }

    public TerminalParser(TokenType type) {
        super(val -> (R)val);
        this.type = type;
    }

    @Override
    public ParseResult<R> parse(TokenBuffer tokenBuffer) {
        Token token = tokenBuffer.currentToken();
        if (token.getType() == type) {
            tokenBuffer.moveNext();
            R result = action(token.getValue());
            return new ParseResult(true, result, tokenBuffer);
        } else {
            return new ParseResult(false, null, tokenBuffer);
        }
    }
}
