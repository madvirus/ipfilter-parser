package ipfilterdsl.combiparser;

import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.TokenBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class SequenceParser extends Parser {

    private List<Parser> parserList;

    public SequenceParser(List<Parser> parsers, Consumer<List<String>> actionFunc) {
        super(actionFunc);
        if (parsers == null || parsers.size() == 0) {
            throw new IllegalArgumentException("no parsers");
        }
        parserList = parsers;
    }

    public SequenceParser(List<Parser> parsers) {
        this(parsers, null);
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.getCurrentPosition();
        Iterator<Parser> parserIter = parserList.iterator();
        Parser firstParser = parserIter.next();
        ParseResult lastResult = firstParser.parse(tokenBuffer);
        if (lastResult.isSuccess()) {
            List<String> values = new ArrayList<>();
            values.add(lastResult.getValue());
            while (parserIter.hasNext() && lastResult.isSuccess()) {
                lastResult = parserIter.next().parse(tokenBuffer);
                if (lastResult.isSuccess()) {
                    values.add(lastResult.getValue());
                } else {
                    throw new MatchingTokenNotFoundException();
                }
            }
            action(values);
            return new ParseResult(true, "");
        } else {
            tokenBuffer.resetPosition(pos);
            return new ParseResult(false, "");
        }
    }
}
