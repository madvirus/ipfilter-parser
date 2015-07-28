package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ChoiceParser extends Parser {
    private List<Parser> parsers;

    public ChoiceParser(List<Parser> parsers, Consumer<List<String>> actionFunc) {
        super(actionFunc);
        if (parsers == null || parsers.size() == 0) {
            throw new IllegalArgumentException("no parsers");
        }
        this.parsers = parsers;
    }

    public ChoiceParser(List<Parser> parsers) {
        this(parsers, null);
    }

    @Override
    public ParseResult parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.currentPosition();
        ParseResult lastResult;
        Iterator<Parser> parserIter = parsers.iterator();
        do {
            Parser parser = parserIter.next();
            lastResult = parser.parse(tokenBuffer);
            if (lastResult.isSuccess()) {
                action(Arrays.asList(lastResult.getValue()));
            }
        } while (parserIter.hasNext() && !lastResult.isSuccess());
        if (lastResult.isSuccess()) {
            return ParseResult.success(tokenBuffer);
        } else {
            tokenBuffer.resetPosition(pos);
            return ParseResult.fail(tokenBuffer);
        }
    }
}
