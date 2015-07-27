package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RepetitionParser extends Parser {
    private final boolean oneOrMore;
    private Parser parser;

    public RepetitionParser(boolean oneOrMore, Parser parser, Consumer<List<String>> actionFunc) {
        super(actionFunc);
        if (parser == null) {
            throw new IllegalArgumentException("no parser");
        }
        this.oneOrMore = oneOrMore;
        this.parser = parser;
    }

    public RepetitionParser(boolean oneOrMore, Parser parser) {
        this(oneOrMore, parser, null);
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        List<String> values = new ArrayList<>();
        ParseResult lastResult = parser.parse(tokenBuffer);
        if (lastResult.isSuccess()) {
            values.add(lastResult.getValue());
        }
        if (oneOrMore && !lastResult.isSuccess()) {
            return ParseResult.fail();
        }
        while (lastResult.isSuccess()) {
            lastResult = parser.parse(tokenBuffer);
            if (lastResult.isSuccess()) {
                values.add(lastResult.getValue());
            }
        }
        if (!values.isEmpty()) {
            action(values);
        }
        return ParseResult.success();
    }
}
