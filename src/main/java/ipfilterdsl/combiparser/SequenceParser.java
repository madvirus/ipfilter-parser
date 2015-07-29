package ipfilterdsl.combiparser;

import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.TokenBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class SequenceParser<I,R,T> extends Parser<List<R>,T> {

    private List<Parser<I,R>> parserList;

    public SequenceParser(List<Parser<I,R>> parsers, Action<List<R>,T> action) {
        super(action);
        if (parsers == null || parsers.size() == 0) {
            throw new IllegalArgumentException("no parsers");
        }
        parserList = parsers;
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.currentPosition();
        Iterator<Parser<I,R>> parserIter = parserList.iterator();
        Parser<I,R> firstParser = parserIter.next();
        ParseResult<R> lastResult = firstParser.parse(tokenBuffer);
        if (lastResult.isSuccess()) {
            List<R> values = new ArrayList<>();
            values.add(lastResult.getValue());
            while (parserIter.hasNext() && lastResult.isSuccess()) {
                lastResult = parserIter.next().parse(tokenBuffer);
                if (lastResult.isSuccess()) {
                    values.add(lastResult.getValue());
                } else {
                    throw new MatchingTokenNotFoundException();
                }
            }
            T ret = action(values);
            return new ParseResult(true, ret, tokenBuffer);
        } else {
            tokenBuffer.resetPosition(pos);
            return new ParseResult(false, null, tokenBuffer);
        }
    }
}
