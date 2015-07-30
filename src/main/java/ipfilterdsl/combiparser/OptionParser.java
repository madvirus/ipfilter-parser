package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class OptionParser<I,R> extends Parser<I,Optional<R>> {
    private Parser parser;

    public OptionParser(Parser<I,R> parser) {
        super(null);
        if (parser == null)
            throw new IllegalArgumentException();

        this.parser = parser;
    }

    @Override
    public ParseResult<Optional<R>> parse(TokenBuffer tokenBuffer) {
        int pos = tokenBuffer.currentPosition();
        ParseResult<R> result = parser.parse(tokenBuffer);
        if (result.isSuccess()) {
            return new ParseResult(true, Optional.ofNullable(result.getValue()), tokenBuffer);
        } else {
            tokenBuffer.resetPosition(pos);
        }
        return new ParseResult(true, Optional.empty(), tokenBuffer);
    }

}
