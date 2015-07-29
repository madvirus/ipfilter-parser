package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import static ipfilterdsl.TokenUtil.allowToken;
import static ipfilterdsl.TokenUtil.denyToken;
import static ipfilterdsl.TokenUtil.tokenBuffer;
import static ipfilterdsl.combiparser.ParserUtil.terminalParser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class TerminalParserTest {
    @Test
    public void match() throws Exception {
        TerminalParser<String> terminalParser = terminalParser(TokenType.TT_ALLOW, val -> val);
        TokenBuffer tokenBuffer = tokenBuffer(allowToken());
        ParseResult<String> result = terminalParser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getValue(), equalTo("allow"));
        assertThat(tokenBuffer.hasCurrent(), equalTo(false));
    }

    @Test
    public void noMatch() throws Exception {
        TerminalParser<String> terminalParser = terminalParser(TokenType.TT_ALLOW, val -> val);
        TokenBuffer tokenBuffer = tokenBuffer(denyToken());
        ParseResult<String> result = terminalParser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(false));
        assertThat(result.getValue(), nullValue());
        assertThat(tokenBuffer.currentPosition(), equalTo(0));
        assertThat(tokenBuffer.hasCurrent(), equalTo(true));
    }

}
