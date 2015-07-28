package ipfilterdsl.rdparser;

import ipfilterdsl.lexer.Lexer;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RegexTest {

    @Test
    public void testName() throws Exception {
        Pattern pattern = Pattern.compile("^$");
        Matcher matcher = pattern.matcher("");
        System.out.println(matcher.find());
    }

    @Test
    public void twoTokens() throws Exception {
        Lexer lexer = new Lexer("allowToken 1.2.3.4");
        TokenBuffer tokenBuffer = lexer.tokenize();
        assertThat(tokenBuffer.currentTokenAndMoveNext(), equalTo(new Token(TokenType.TT_ALLOW, "allowToken")));
        assertThat(tokenBuffer.currentTokenAndMoveNext(), equalTo(new Token(TokenType.TT_IPRANGE, "1.2.3.4")));
        assertThat(tokenBuffer.currentTokenAndMoveNext(), equalTo(new Token(TokenType.TT_EOF, null)));
        assertThat(tokenBuffer.hasNext(), equalTo(false));
    }

}
