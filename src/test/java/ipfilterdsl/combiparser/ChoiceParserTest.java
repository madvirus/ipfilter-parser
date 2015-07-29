package ipfilterdsl.combiparser;

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

public class ChoiceParserTest {
    @Test
    public void noParsers() throws Exception {
        try {
            new ChoiceParser(null, null);
            fail("exception should be thrown with no parser");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void allNotMatching_fail() throws Exception {
        ChoiceParser<String, String, String> parser = new ChoiceParser(
                parsers(
                        terminalParser(TokenType.TT_ALLOW),
                        terminalParser(TokenType.TT_DENY),
                        terminalParser(TokenType.TT_IPRANGE)
                ));
        TokenBuffer tokenBuffer = tokenBuffer(commaToken());
        int oriPosition = tokenBuffer.currentPosition();
        ParseResult result = parser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(false));
        assertThat(tokenBuffer.currentPosition(), equalTo(oriPosition));
    }

    @Test
    public void someMatching_success() throws Exception {
        List<Parser<String,String>> parsers = parsers(
                terminalParser(TokenType.TT_DENY),
                terminalParser(TokenType.TT_ALLOW),
                terminalParser(TokenType.TT_IPRANGE)
        );
        assertSuccess(parsers, allowToken());
        assertSuccess(parsers, denyToken());
        assertSuccess(parsers, iprangeToken("1.2.3.4"));
    }
//
    private void assertSuccess(List<Parser<String,String>> parsers, Token token) {
        ChoiceParser<String,String,String> parser = new ChoiceParser<>(parsers, val -> val);
        ParseResult<String> result = parser.parse(tokenBuffer(token));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getValue(), equalTo(token.getValue()));
    }

}
