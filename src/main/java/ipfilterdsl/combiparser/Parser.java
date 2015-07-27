package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Parser {

    private Optional<Consumer<List<String>>> actionFuncOpt = null;

    public Parser(Consumer<List<String>> actionFunc) {
        this.actionFuncOpt = Optional.ofNullable(actionFunc);
    }
    public Parser() {
        this.actionFuncOpt = Optional.empty();
    }

    public abstract ParseResult parse(TokenBuffer tokenBuffer);

    public void action(List<String> results) {
        actionFuncOpt.ifPresent(actionFunc -> actionFunc.accept(results));
    }

}
