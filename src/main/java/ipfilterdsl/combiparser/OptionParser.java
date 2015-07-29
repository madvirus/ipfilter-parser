package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class OptionParser<I,R,T> extends Parser<R,T> {
    private Parser parser;

    public OptionParser(Parser<I,R> parser, Action<R,T> action) {
        super(action);
        if (parser == null)
            throw new IllegalArgumentException();

        this.parser = parser;
    }

    @Override
    public ParseResult<Optional<T>> parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.currentPosition();
        ParseResult<R> result = parser.parse(tokenBuffer);
        if (result.isSuccess()) {
            T ret = action(result.getValue());
            return new ParseResult(true, Optional.ofNullable(ret), tokenBuffer);
        } else {
            tokenBuffer.resetPosition(pos);
        }
        return new ParseResult(true, Optional.empty(), tokenBuffer);
    }

}
