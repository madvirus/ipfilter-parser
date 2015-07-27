package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenType;

import java.util.Arrays;
import java.util.List;

public abstract class ParserUtil {
    public static List<Parser> parsers(Parser... parsers) {
        return Arrays.asList(parsers);
    }

    public static TerminalParser terminalParser(TokenType tokenType) {
        return new TerminalParser(tokenType, (values) -> {
        });
    }

}
