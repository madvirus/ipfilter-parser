package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ipfilterdsl.TokenUtil.allowToken;
import static ipfilterdsl.TokenUtil.denyToken;
import static ipfilterdsl.TokenUtil.tokenBuffer;
import static ipfilterdsl.combiparser.ParserUtil.terminalParser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OptionParserTest {
    @Test
    public void noParsers() throws Exception {
        try {
            new OptionParser(null, null);
            fail("exception should be thrown with no parser");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void givenMatchingToken_success() throws Exception {
        TerminalParser parser = terminalParser(TokenType.TT_ALLOW);
        List<String> values = new ArrayList<>();
        OptionParser optParser = new OptionParser(parser, (val) -> values.addAll(val));
        ParseResult result = optParser.parse(tokenBuffer(allowToken()));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(values.get(0), equalTo("allow"));
    }

    @Test
    public void notMatchingToken_success() throws Exception {
        TerminalParser parser = terminalParser(TokenType.TT_ALLOW);
        List<String> values = new ArrayList<>();
        OptionParser optParser = new OptionParser(parser, (val) -> values.addAll(val));
        ParseResult result = optParser.parse(tokenBuffer(denyToken()));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(values, hasSize(0));
    }

}
