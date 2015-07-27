package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class OptionParser extends Parser {
    private Parser parser;

    public OptionParser(Parser parser, Consumer<List<String>> actionFunc) {
        super(actionFunc);
        if (parser == null)
            throw new IllegalArgumentException();

        this.parser = parser;
    }

    public OptionParser(Parser parser) {
        this(parser, null);
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.getCurrentPosition();
        ParseResult result = parser.parse(tokenBuffer);
        if (result.isSuccess()) {
            action(Arrays.asList(result.getValue()));
        } else {
            tokenBuffer.resetPosition(pos);
        }
        return ParseResult.success();
    }

}
