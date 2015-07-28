package ipfilterdsl.lexer;

import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private String code;
    private List<Token> tokenList = new ArrayList<>();
    private List<TokenTypePattern> typePattern = new ArrayList<>();

    public Lexer(String code) {
        this.code = code;
        for (TokenType type : TokenType.values()) {
            if (type.hasRegExp())
                typePattern.add(new TokenTypePattern(type));
        }
    }

    public TokenBuffer tokenize() {
        while (matchToken() && !eof()) {
        }
        if (!eof()) {
            // 일치하지 않은 토큰이 존재하는 것임!
            throw new MatchingTokenNotFoundException();
        }
        tokenList.add(new Token(TokenType.TT_EOF, null));
        return new TokenBuffer(tokenList);
    }

    private boolean matchToken() {
        boolean match = false;
        Iterator<TokenTypePattern> patterIter = typePattern.iterator();
        while (!match && patterIter.hasNext()) {
            TokenTypePattern ttPattern = patterIter.next();
            Matcher matcher = ttPattern.pattern.matcher(code);
            if (matcher.find()) {
                if (ttPattern.type.isOutputIncluded()) {
                    tokenList.add(new Token(ttPattern.type, matcher.group()));
                }
                match = true;
                code = code.substring(matcher.end());
            }
        }
        return match;
    }

    private boolean eof() {
        return code.length() == 0;
    }

    private class TokenTypePattern {
        private TokenType type;
        private Pattern pattern;

        public TokenTypePattern(TokenType type) {
            this.type = type;
            this.pattern = Pattern.compile(type.getRegex());
        }
    }
}
