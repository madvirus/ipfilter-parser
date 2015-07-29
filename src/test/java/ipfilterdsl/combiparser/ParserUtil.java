package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenType;

import java.util.Arrays;
import java.util.List;

public abstract class ParserUtil {
    public static <I,R> List<Parser<I,R>> parsers(Parser<I,R>... parsers) {
        return Arrays.asList(parsers);
    }

    public static <T> TerminalParser<T> terminalParser(TokenType tokenType, Action<String, T> action) {
        return new TerminalParser<T>(tokenType, action);
    }

    public static TerminalParser<String> terminalParser(TokenType tokenType) {
        return new TerminalParser<>(tokenType, val->val);
    }

}
