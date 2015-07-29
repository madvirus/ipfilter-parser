package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RepetitionParser<I,R,T> extends Parser<List<R>,T> {
    private final boolean oneOrMore;
    private Parser<I,R> parser;

    public RepetitionParser(boolean oneOrMore, Parser parser, Action<List<R>, T> action) {
        super(action);
        if (parser == null) {
            throw new IllegalArgumentException("no parser");
        }
        this.oneOrMore = oneOrMore;
        this.parser = parser;
    }

    @Override
    public ParseResult<T> parse(TokenBuffer tokenBuffer) {
        List<R> values = new ArrayList<>();
        ParseResult<R> lastResult = parser.parse(tokenBuffer);
        if (lastResult.isSuccess()) {
            values.add(lastResult.getValue());
        }
        if (oneOrMore && !lastResult.isSuccess()) {
            return new ParseResult<>(false, null, tokenBuffer);
        }
        while (lastResult.isSuccess()) {
            lastResult = parser.parse(tokenBuffer);
            if (lastResult.isSuccess()) {
                values.add(lastResult.getValue());
            }
        }
        T ret = action(values);
        return new ParseResult(true, ret, tokenBuffer);
    }
}
