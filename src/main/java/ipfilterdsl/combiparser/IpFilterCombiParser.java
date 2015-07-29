package ipfilterdsl.combiparser;

import ipfilter.IpFilter;
import ipfilter.IpRange;
import ipfilterdsl.token.MatchingTokenNotFoundException;
import ipfilterdsl.token.TokenBuffer;
import ipfilterdsl.token.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class IpFilterCombiParser {
    private TokenBuffer tokenBuffer;

    private Exception occuredException;
    private IpFilter result;
    //
    // termianl
    private TerminalParser<String> allowParser = new TerminalParser<>(TokenType.TT_ALLOW, v -> v);
    private TerminalParser<String> denyParser = new TerminalParser<>(TokenType.TT_DENY, v -> v);
    private TerminalParser<IpRange> iprangeParser = new TerminalParser<>(TokenType.TT_IPRANGE, v -> new IpRange(v));
    private TerminalParser<String> orderParser = new TerminalParser<>(TokenType.TT_ORDER, v -> v);
    private TerminalParser<String> commandParser = new TerminalParser<>(TokenType.TT_COMMA, v -> v);

    // non-terminal
    // allowDecl : ALLOW iprange;
    private SequenceParser<String, Object, Tuple2<String,IpRange>> allowDecl =
            new SequenceParser<>(
                    Arrays.asList(allowParser, iprangeParser),
                    vals -> new Tuple2<>((String)vals.get(0), (IpRange)vals.get(1))
            );

    // denyDecl : DENY iprange;
    private SequenceParser<String, Object, Tuple2<String, IpRange>> denyDecl =
            new SequenceParser<>(
                    Arrays.asList(denyParser, iprangeParser),
                    vals -> new Tuple2<>((String)vals.get(0), (IpRange)vals.get(1))
            );

    // allowOrDenyDecl : allowDecl | denyDecl
    private ChoiceParser<String, Tuple2<String, IpRange>, Tuple2<String, IpRange>> allowOrDenyDecl =
            new ChoiceParser(Arrays.asList(allowDecl, denyDecl), val -> val);

    // allowOrDenyDeclList : allowOrDenyDecl*
    private RepetitionParser<Tuple2<String, IpRange>, Tuple2<String, IpRange>, List<Tuple2<String, IpRange>>> allowOrDenyList =
            new RepetitionParser(false, allowOrDenyDecl, vals -> vals);

    // allowDeny : ALLOW ',' DENY;
    private SequenceParser<String, String, Boolean> allowDeny =
            new SequenceParser<>(Arrays.asList(allowParser, commandParser, denyParser), vals -> true);
    // denyAllow : DENY ',' ALLOW
    private SequenceParser<String, String, Boolean> denyAllow =
            new SequenceParser<>(Arrays.asList(denyParser, commandParser, allowParser), vals -> false);

    // orderChoice: allowDeny | denyAllow
    private Parser<Boolean, Boolean> orderChoice =
            new ChoiceParser<>(Arrays.asList(allowDeny, denyAllow), val -> val);
    // orderDecl : ORDER orderChoice
    private SequenceParser<Object, Object, Boolean> orderDecl =
            new SequenceParser<>(Arrays.asList(orderParser, orderChoice), vals -> (Boolean)vals.get(1));


    private OptionParser<List<Object>, Boolean, Boolean> orderDeclOpt = new OptionParser<>(orderDecl, v -> v);

    private SequenceParser<Object, Object, IpFilter> configParser =
            new SequenceParser<>(
                    Arrays.asList(allowOrDenyList, orderDeclOpt),
                    (vals) -> {
                        List<Tuple2<String, IpRange>> ipranges = (List<Tuple2<String, IpRange>>) vals.get(0);
                        Optional<Boolean> allowFirstOpt = (Optional<Boolean>) vals.get(1);
                        IpFilter filter = new IpFilter();
                        ipranges.forEach(tuple -> {
                            if (tuple.e1.equals("allow")) {
                                filter.addAllowIpRange(tuple.e2);
                            } else {
                                filter.addDenyIpRange(tuple.e2);
                            }
                        });
                        filter.setAllowFirst(allowFirstOpt.orElse(true));
                        return filter;
                    });

    public IpFilterCombiParser(TokenBuffer tokenBuffer) {
        this.tokenBuffer = tokenBuffer;
    }

    public void parse() {
        try {
            ParseResult<IpFilter> parseResult = configParser.parse(tokenBuffer);
            if (parseResult.isSuccess()) {
                if (tokenBuffer.currentTokenAndMoveNext().getType() == TokenType.TT_EOF) {
                    this.result = parseResult.getValue();
                } else {
                    throw new MatchingTokenNotFoundException();
                }
            }
        } catch(Exception e) {
            occuredException = e;
        }
    }

    public Optional<IpFilter> getResult() {
        return Optional.ofNullable(result);
    }

    public boolean isFailed() {
        return occuredException != null;
    }

}
