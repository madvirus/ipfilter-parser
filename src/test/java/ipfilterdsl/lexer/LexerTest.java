package ipfilterdsl.lexer;

import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import static ipfilterdsl.TokenUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LexerTest {

    @Test
    public void noTokens() throws Exception {
        assertTokens("", eofToken());
    }

    @Test
    public void givenValidCode() throws Exception {
        assertTokens("allow 1.2.3.4",
                allowToken(),
                iprangeToken("1.2.3.4"),
                eofToken());

        assertTokens("deny 1.2.3.4",
                denyToken(),
                iprangeToken("1.2.3.4"),
                eofToken());
    }

    private void assertTokens(String code, Token... expectedTokens) {
        Lexer lexer = new Lexer(code);
        TokenBuffer tokenBuffer = lexer.tokenize();
        for (Token token : expectedTokens) {
            assertThat(tokenBuffer.nextToken(), equalTo(token));
        }
        assertThat(tokenBuffer.hasNext(), equalTo(false));
    }

    @Test
    public void givenBadCode() throws Exception {
        assertMatchingTokenNotFoundExceptionThrown("allow 1.2.3.4 noToken");
        assertMatchingTokenNotFoundExceptionThrown("allow 1.2.3.4/");
        assertMatchingTokenNotFoundExceptionThrown("allowToken 1.2.3.4");
    }

    private void assertMatchingTokenNotFoundExceptionThrown(String code) {
        Lexer lexer = new Lexer(code);
        try {
            lexer.tokenize();
            fail();
        } catch (MatchingTokenNotFoundException ex) {
        }
    }

    private static Token token(TokenType type, String value) {
        return new Token(type, value);
    }
}
