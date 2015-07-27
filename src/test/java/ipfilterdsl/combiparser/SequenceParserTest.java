package ipfilterdsl.combiparser;

import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ipfilterdsl.TokenUtil.*;
import static ipfilterdsl.combiparser.ParserUtil.parsers;
import static ipfilterdsl.combiparser.ParserUtil.terminalParser;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SequenceParserTest {
    @Test
    public void noParsers() throws Exception {
        try {
            new SequenceParser(null, null);
            fail("exception should be thrown with no parser");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void success() throws Exception {
        assertSuccess(
                parsers(terminalParser(TokenType.TT_IPRANGE)),
                tokens(iprangeToken("1.2.3.4")));

        assertSuccess(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_IPRANGE)
                ),
                tokens(allowToken(), iprangeToken("1.2.3.4")));

        assertSuccess(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_COMMA),
                        terminalParser(TokenType.TT_DENY)
                ),
                tokens(allowToken(), commaToken(), denyToken()));
    }

    @Test
    public void firstTokenNotMatch_then_fail() throws Exception {
        assertFail(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_IPRANGE)
                ),
                tokens(denyToken(), iprangeToken("1.2.3.4")));
    }

    private void assertFail(List<Parser> parsers, List<Token> tokens) {
        SequenceParser seqParsers = new SequenceParser(parsers, null);
        ParseResult result = seqParsers.parse(new TokenBuffer(tokens));
        assertThat(result.isSuccess(), equalTo(false));
    }

    @Test
    public void firstMatch_thenNotMatch_then_exceptionThrown() throws Exception {
        assertMatchingTokenNotFoundExceptionThrown(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_COMMA),
                        terminalParser(TokenType.TT_DENY)
                ), tokens(allowToken(), denyToken(), commaToken()));

        assertMatchingTokenNotFoundExceptionThrown(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_COMMA),
                        terminalParser(TokenType.TT_DENY)
                ), tokens(allowToken(), commaToken(), commaToken()));

        assertMatchingTokenNotFoundExceptionThrown(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_IPRANGE)
                ), tokens(allowToken(), commaToken()));
    }

    private void assertMatchingTokenNotFoundExceptionThrown(List<Parser> parsers, List<Token> tokens) {
        SequenceParser seqParsers = new SequenceParser(parsers, null);
        try {
            seqParsers.parse(new TokenBuffer(tokens));
            fail("exception should thrown");
        } catch (MatchingTokenNotFoundException e) {
        }
    }

    private void assertSuccess(List<Parser> parsers, List<Token> tokens) {
        List<String> values = new ArrayList<>();
        SequenceParser seqParsers = new SequenceParser(parsers, (vals) -> values.addAll(vals));
        ParseResult result = seqParsers.parse(new TokenBuffer(tokens));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(values.size(), equalTo(tokens.size()));
        for (int i = 0; i < values.size(); i++) {
            assertThat(values.get(i), equalTo(tokens.get(i).getValue()));
        }
    }

}
