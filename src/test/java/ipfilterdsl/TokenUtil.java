package ipfilterdsl;

import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.Arrays;
import java.util.List;

public abstract class TokenUtil {
    public static Token eofToken() {
        return new Token(TokenType.TT_EOF, null);
    }

    public static Token allowToken() {
        return new Token(TokenType.TT_ALLOW, "allow");
    }

    public static Token denyToken() {
        return new Token(TokenType.TT_DENY, "deny");
    }

    public static Token commaToken() {
        return new Token(TokenType.TT_COMMA, ",");
    }

    public static Token iprangeToken(String range) {
        return new Token(TokenType.TT_IPRANGE, range);
    }

    public static Token orderToken() {
        return new Token(TokenType.TT_ORDER, "order");
    }

    public static List<Token> tokens(Token...tokens) {
        return Arrays.asList(tokens);
    }


    public static TokenBuffer tokenBuffer(Token...tokens) {
        return new TokenBuffer(Arrays.asList(tokens));
    }}
