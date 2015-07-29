package ipfilterdsl.combiparser;

import ipfilter.IpFilter;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static ipfilterdsl.IpFilterBuilder.ipFilter;
import static ipfilterdsl.TokenUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IpFilterCombiParserTest {
    @Test
    public void givenNoToken() throws Exception {
        assertParserResult(
                tokens(eofToken()),
                new IpFilter()
        );
    }

    @Test
    public void givenAllowOny() throws Exception {
        assertParserResult(
                tokens(
                        allowToken(), iprangeToken("1.2.3.4"),
                        eofToken()
                ),
                ipFilter().allow("1.2.3.4").result()
        );

        assertParserResult(
                tokens(
                        allowToken(), iprangeToken("1.2.3.4"),
                        allowToken(), iprangeToken("10.20.30.40"),
                        eofToken()
                ),
                ipFilter().allow("1.2.3.4").allow("10.20.30.40").result()
        );
    }

    @Test
    public void givenDenyOnly() throws Exception {
        assertParserResult(
                tokens(
                        denyToken(), iprangeToken("1.2.3.4"),
                        eofToken()
                ),
                ipFilter().deny("1.2.3.4").result()
        );

        assertParserResult(
                tokens(
                        denyToken(), iprangeToken("1.2.3.4"),
                        denyToken(), iprangeToken("10.20.30.40"),
                        eofToken()
                ),
                ipFilter().deny("1.2.3.4").deny("10.20.30.40").result()
        );
    }

    @Test
    public void givenOrderOnly() {
        assertParserResult(
                tokens(
                        orderToken(), allowToken(), commaToken(), denyToken(),
                        eofToken()
                ),
                ipFilter().allowFirst(true).result()
        );
        assertParserResult(
                tokens(
                        orderToken(), denyToken(), commaToken(), allowToken(),
                        eofToken()
                ),
                ipFilter().allowFirst(false).result()
        );
    }

    @Test
    public void badSyntax() throws Exception {
        assertParseFail(tokens(allowToken(),eofToken()));
        assertParseFail(tokens(denyToken(),iprangeToken("1.2.3.4"),allowToken(),eofToken()));
        assertParseFail(tokens(orderToken(),allowToken(),eofToken()));
        assertParseFail(tokens(orderToken(),allowToken(),commaToken(),allowToken(),eofToken()));
        assertParseFail(tokens(orderToken(), denyToken(), commaToken(), denyToken(), eofToken()));
        assertParseFail(tokens(orderToken(), denyToken(), commaToken(), denyToken(), eofToken()));
        assertParseFail(tokens(denyToken(), iprangeToken("1.2.3.4"), orderToken(), denyToken(), commaToken(), eofToken()));
        assertParseFail(tokens(orderToken(), denyToken(), commaToken(), allowToken(), allowToken(), iprangeToken("1.2.3.4"), eofToken()));
    }

    private void assertParseFail(List<Token> tokens) {
        IpFilterCombiParser parser = parserWithTokens(tokens);
        parser.parse();
        assertThat(parser.isFailed(), equalTo(true));
        Optional<IpFilter> filter = parser.getResult();
        assertThat(filter.isPresent(), equalTo(false));
    }

    public void assertParserResult(List<Token> tokens, IpFilter expectedFilter) {
        IpFilterCombiParser parser = parserWithTokens(tokens);
        parser.parse();
        Optional<IpFilter> filter = parser.getResult();
        assertThat(filter.get(), equalTo(expectedFilter));
    }

    private IpFilterCombiParser parserWithTokens(List<Token> tokens) {
        return new IpFilterCombiParser(new TokenBuffer(tokens));
    }


}
