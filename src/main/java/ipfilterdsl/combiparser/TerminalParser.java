package ipfilterdsl.combiparser;

import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.List;
import java.util.function.Consumer;

public class TerminalParser extends Parser {
    private TokenType type;

    public TerminalParser(TokenType type, Consumer<List<String>> action) {
        super(action);
        this.type = type;
    }

    public TerminalParser(TokenType type) {
        super();
        this.type = type;
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        Token token = tokenBuffer.nextToken();
        if (token.getType() == type) {
            return new ParseResult(true, token.getValue());
        } else {
            tokenBuffer.movePrevious();
            return new ParseResult(false, "");
        }
    }
}
