package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ChoiceParser<I,R,T> extends Parser<R,T> {
    private List<Parser<I,R>> parsers;

    public ChoiceParser(List<Parser<I,R>> parsers, Action actionFunc) {
        super(actionFunc);
        if (parsers == null || parsers.size() == 0) {
            throw new IllegalArgumentException("no parsers");
        }
        this.parsers = parsers;
    }

    public ChoiceParser(List<Parser<I,R>> parsers) {
        this(parsers, val -> (T)val);
    }

    @Override
    public ParseResult<T> parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.currentPosition();
        ParseResult<R> lastResult;
        Iterator<Parser<I,R>> parserIter = parsers.iterator();
        do {
            Parser<I,R> parser = parserIter.next();
            lastResult = parser.parse(tokenBuffer);
        } while (parserIter.hasNext() && !lastResult.isSuccess());
        if (lastResult.isSuccess()) {
            T ret = action(lastResult.getValue());
            return new ParseResult(true, ret, tokenBuffer);
        } else {
            tokenBuffer.resetPosition(pos);
            return new ParseResult(false, null, tokenBuffer);
        }
    }
}
