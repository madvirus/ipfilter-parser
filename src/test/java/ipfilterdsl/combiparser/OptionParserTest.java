package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            new OptionParser(null);
            fail("exception should be thrown with no parser");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void givenMatchingToken_success() throws Exception {
        TerminalParser<String> parser = terminalParser(TokenType.TT_ALLOW);
        OptionParser<String,String> optParser = new OptionParser<>(parser);
        ParseResult<Optional<String>> result = optParser.parse(tokenBuffer(allowToken()));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getValue().get(), equalTo("allow"));
    }

    @Test
    public void notMatchingToken_success() throws Exception {
        TerminalParser<String> parser = terminalParser(TokenType.TT_ALLOW);
        OptionParser<String,String> optParser = new OptionParser<>(parser);
        ParseResult<Optional<String>> result = optParser.parse(tokenBuffer(denyToken()));
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getValue().isPresent(), equalTo(false));
    }

}
