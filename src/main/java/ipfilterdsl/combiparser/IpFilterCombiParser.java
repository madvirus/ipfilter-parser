package ipfilterdsl.combiparser;

import ipfilter.IpFilter;
import ipfilter.IpRange;
import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class IpFilterCombiParser {
    private TokenBuffer tokenBuffer;
    private boolean allowFirst = true;
    private List<IpRange> allowIpRanges = new ArrayList<>();
    private List<IpRange> denyIpRanges = new ArrayList<>();

    private Exception occuredException;
    private IpFilter result;

    // termianl
    private TerminalParser allowParser = new TerminalParser(TokenType.TT_ALLOW, (vals) -> {});
    private TerminalParser denyParser = new TerminalParser(TokenType.TT_DENY, (vals) -> {});
    private TerminalParser iprangeParser = new TerminalParser(TokenType.TT_IPRANGE, (vals) -> {});
    private TerminalParser orderParser = new TerminalParser(TokenType.TT_ORDER, (vals) -> {});
    private TerminalParser commandParser = new TerminalParser(TokenType.TT_COMMA, (vals) -> {});

    // non-terminal
    // allowDecl : ALLOW iprange;
    private SequenceParser allowDecl = new SequenceParser(Arrays.asList(allowParser, iprangeParser), (vals) -> {
        allowIpRanges.add(new IpRange(vals.get(1)));
    });
    // denyDecl : DENY iprange;
    private SequenceParser denyDecl = new SequenceParser(Arrays.asList(denyParser, iprangeParser), (vals) -> {
        denyIpRanges.add(new IpRange(vals.get(1)));
    });
    // allowOrDenyDecl : allowDecl | denyDecl
    private ChoiceParser allowOrDenyDecl = new ChoiceParser(Arrays.asList(allowDecl, denyDecl), (vals) -> {});
    // allowOrDenyDeclList : allowOrDenyDecl*
    private RepetitionParser allowOrDenyList = new RepetitionParser(false, allowOrDenyDecl, (vals) -> {});
    // allowDeny : ALLOW ',' DENY;
    private SequenceParser allowDeny = new SequenceParser(Arrays.asList(allowParser, commandParser, denyParser),
            (vals) -> allowFirst = true );
    // denyAllow : DENY ',' ALLOW
    private SequenceParser denyAllow = new SequenceParser(Arrays.asList(denyParser, commandParser, allowParser),
            (vals) -> allowFirst = false );

    // orderChoice: allowDeny | denyAllow
    private ChoiceParser orderChoice = new ChoiceParser(Arrays.asList(allowDeny, denyAllow), (vals) -> {});
    // orderDecl : ORDER orderChoice
    private SequenceParser orderDecl = new SequenceParser(Arrays.asList(orderParser, orderChoice), (vals) -> {});

    private OptionParser orderDeclOpt = new OptionParser(orderDecl, (vals) -> {});

    private SequenceParser configParser = new SequenceParser(Arrays.asList(allowOrDenyList, orderDeclOpt), (vals) -> {});

    public IpFilterCombiParser(TokenBuffer tokenBuffer) {
        this.tokenBuffer = tokenBuffer;
    }

    public void parse() {
        try {
            ParseResult result = configParser.parse(tokenBuffer);
            if (result.isSuccess()) {
                if (tokenBuffer.currentTokenAndMoveNext().getType() == TokenType.TT_EOF) {
                    createResult();
                } else {
                    throw new MatchingTokenNotFoundException();
                }
            }
        } catch(Exception e) {
            occuredException = e;
        }
    }

    private void createResult() {
        IpFilter ipFilter = new IpFilter();
        ipFilter.setAllowFirst(allowFirst);
        ipFilter.addAllowIpRanges(allowIpRanges);
        ipFilter.addDenyIpRanges(denyIpRanges);
        this.result = ipFilter;
    }

    public Optional<IpFilter> getResult() {
        return Optional.ofNullable(result);
    }

    public boolean isFailed() {
        return occuredException != null;
    }

}
