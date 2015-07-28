package ipfilterdsl.rdparser;

import ipfilter.IpFilter;
import ipfilter.IpRange;
import ipfilterdsl.ParseFailException;
import ipfilterdsl.token.Token;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IpFilterParser {

    private TokenBuffer tokenBuffer;

    private boolean allowFirst = true;
    private List<IpRange> allowIpRanges = new ArrayList<>();
    private List<IpRange> denyIpRanges = new ArrayList<>();

    private Exception occuredException;

    private IpFilter result;

    public IpFilterParser(TokenBuffer tokenBuffer) {
        this.tokenBuffer = tokenBuffer;
    }

    public void parse() {
        try {
            if (config()) {
                createResult();
            }
        } catch (Exception e) {
            occuredException = e;
        }
    }

    public boolean config() {
        // config : allowOrDenyDeclList orderDecl;
        if (allowOrDenyDeclList()) {
            if (optionalOrderDecl()) {
                return true;
            }
        }
        return false;
    }

    private void createResult() {
        IpFilter ipFilter = new IpFilter();
        ipFilter.setAllowFirst(allowFirst);
        ipFilter.addAllowIpRanges(allowIpRanges);
        ipFilter.addDenyIpRanges(denyIpRanges);
        this.result = ipFilter;
    }

    private boolean allowOrDenyDeclList() {
        // allowOrDenyDeclList: allowOrDenyDecl*;
        while (allowOrDenyDecl()) {
        }
        return true;
    }

    private boolean allowOrDenyDecl() {
        // allowOrDenyDecl : allowDecl | denyDecl;
        if (parse(this::allowDeclLambda)) {
            return true;
        } else if (denyDecl()) {
            return true;
        }
        return false;
    }

    private boolean parse(Supplier<Boolean> parsing) {
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = parsing.get();
        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return parseSuccess;
    }

    private boolean allowDeclLambda() {
        // allowDecl : ALLOW iprange;
        if (tokenBuffer.currentTokenAndMoveNext().getType() == TokenType.TT_ALLOW) {
            Token token = tokenBuffer.currentTokenAndMoveNext();
            if (token.getType() == TokenType.TT_IPRANGE) {
                allowIpRanges.add(new IpRange(token.getValue()));
                return true;
            } else {
                fail("IpRange token expected, but " + token.toString(), token);
            }
        }
        return false;
    }

    private boolean allowDecl() {
        // allowDecl : ALLOW iprange;
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = false;
        if (tokenBuffer.currentTokenAndMoveNext().getType() == TokenType.TT_ALLOW) {
            Token token = tokenBuffer.currentTokenAndMoveNext();
            if (token.getType() == TokenType.TT_IPRANGE) {
                allowIpRanges.add(new IpRange(token.getValue()));
                parseSuccess = true;
            } else {
                fail("IpRange token expected, but " + token.toString(), token);
            }
        }
        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return parseSuccess;
    }

    private boolean denyDecl() {
        // denyDecl : DENY iprange;
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = false;
        if (tokenBuffer.currentTokenAndMoveNext().getType() == TokenType.TT_DENY) {
            Token token = tokenBuffer.currentTokenAndMoveNext();
            if (token.getType() == TokenType.TT_IPRANGE) {
                denyIpRanges.add(new IpRange(token.getValue()));
                parseSuccess = true;
            } else {
                fail("IpRange token expected, but " + token.toString(), token);
            }
        }
        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return parseSuccess;
    }

    private boolean optionalOrderDecl() {
        // orderDecl : ORDER allowDeny | denyAllow;
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = false;
        Token token = tokenBuffer.currentTokenAndMoveNext();
        if (token.getType() == TokenType.TT_ORDER) {
            if (allowDeny()) {
                parseSuccess = true;
            } else if (denyAllow()) {
                parseSuccess = true;
            } else {
                fail("order keyword must follow 'allow,deny' or 'deny,allow", token);
            }
        }
        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return true;
    }

    private boolean allowDeny() {
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = false;

        Token order1 = tokenBuffer.currentTokenAndMoveNext();
        if (order1.getType() == TokenType.TT_ALLOW) {
            Token token = tokenBuffer.currentTokenAndMoveNext();
            if (token.getType() == TokenType.TT_COMMA) {
                Token order2 = tokenBuffer.currentTokenAndMoveNext();
                if (order2.getType() == TokenType.TT_DENY) {
                    parseSuccess = true;
                    allowFirst = true;
                }
            }
        }
        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return parseSuccess;
    }

    private boolean denyAllow() {
        int save = tokenBuffer.currentPosition();
        boolean parseSuccess = false;

        Token order1 = tokenBuffer.currentTokenAndMoveNext();
        if (order1.getType() == TokenType.TT_DENY) {
            Token token = tokenBuffer.currentTokenAndMoveNext();
            if (token.getType() == TokenType.TT_COMMA) {
                Token order2 = tokenBuffer.currentTokenAndMoveNext();
                if (order2.getType() == TokenType.TT_ALLOW) {
                    parseSuccess = true;
                    allowFirst = false;
                }
            }
        }

        if (!parseSuccess) {
            tokenBuffer.resetPosition(save);
        }
        return parseSuccess;
    }

    private void fail(String s, Token token) {
        throw new ParseFailException(s, token);
    }

    public Optional<IpFilter> getResult() {
        return Optional.ofNullable(result);
    }

    public boolean isFailed() {
        return occuredException != null;
    }

}
