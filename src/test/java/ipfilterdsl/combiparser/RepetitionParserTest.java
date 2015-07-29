package ipfilterdsl.combiparser;

import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ipfilterdsl.TokenUtil.*;
import static ipfilterdsl.combiparser.ParserUtil.terminalParser;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RepetitionParserTest {
    @Test
    public void noParsers() throws Exception {
        try {
            new RepetitionParser(false, null, null);
            fail("exception should be thrown with no parser");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void oneMore_firstNotMatching_fail() throws Exception {
        RepetitionParser<String,String,List<String>> parser = new RepetitionParser(
                true, terminalParser(TokenType.TT_IPRANGE), vals -> vals);
        ParseResult<List<String>> result = parser.parse(tokenBuffer(allowToken()));
        assertThat(result.isSuccess(), equalTo(false));
    }

    @Test
    public void oneMore_first_N_Matching_success() throws Exception {
        assertOneMore_First_N_IpRange_Match_Success("1.2.3.4");
        assertOneMore_First_N_IpRange_Match_Success("1.2.3.4", "5.6.7.8");
        assertOneMore_First_N_IpRange_Match_Success("1.2.3.4", "5.6.7.8", "10.20.30.40");
    }

    private void assertOneMore_First_N_IpRange_Match_Success(String... ipRanges) {
        assert_First_N_IpRange_Match_Success(true, ipRanges);
    }

    private void assert_First_N_IpRange_Match_Success(boolean oneOrMore, String[] ipRanges) {
        List<Token> tokens = new ArrayList<>();
        for (String iprange : ipRanges) {
            tokens.add(new Token(TokenType.TT_IPRANGE, iprange));
        }
        tokens.add(denyToken());
        RepetitionParser<String,String,List<String>> parser = new RepetitionParser(
                oneOrMore,
                terminalParser(TokenType.TT_IPRANGE), vals -> vals);
        TokenBuffer tokenBuffer = new TokenBuffer(tokens);
        ParseResult<List<String>> result = parser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(true));
        for (int i = 0; i < ipRanges.length; i++) {
            assertThat(result.getValue().get(i), equalTo(ipRanges[i]));
        }
        assertThat(tokenBuffer.hasCurrent(), equalTo(true));
        assertThat(tokenBuffer.currentToken().getType(), equalTo(TokenType.TT_DENY));
    }

    @Test
    public void zeroMore_firstNotMatching_success() throws Exception {
        RepetitionParser parser = new RepetitionParser(false, terminalParser(TokenType.TT_IPRANGE), vals -> vals);
        TokenBuffer tokenBuffer = tokenBuffer(allowToken());
        ParseResult result = parser.parse(tokenBuffer);
        assertThat(result.isSuccess(), equalTo(true));
        assertThat(tokenBuffer.currentPosition(), equalTo(0));
    }


    @Test
    public void zeroMore_first_N_Matching_success() throws Exception {
        assertZeroMore_First_N_IpRange_Match_Success("1.2.3.4");
        assertZeroMore_First_N_IpRange_Match_Success("1.2.3.4", "5.6.7.8");
    }

    private void assertZeroMore_First_N_IpRange_Match_Success(String... ipRanges) {
        boolean oneOrMore = false;
        assert_First_N_IpRange_Match_Success(oneOrMore, ipRanges);
    }

}
