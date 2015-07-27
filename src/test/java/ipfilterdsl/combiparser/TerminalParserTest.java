package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import static ipfilterdsl.TokenUtil.allowToken;
import static ipfilterdsl.TokenUtil.tokenBuffer;
import static ipfilterdsl.combiparser.ParserUtil.terminalParser;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TerminalParserTest {
    @Test
    public void terminalParser_match() throws Exception {
        TerminalParser terminalParser = terminalParser(TokenType.TT_ALLOW);
        TokenBuffer tokenBuffer = tokenBuffer(allowToken());
        ParseResult result = terminalParser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(result.getValue(), equalTo("allow"));
        assertThat(tokenBuffer.hasNext(), equalTo(false));
    }

}
