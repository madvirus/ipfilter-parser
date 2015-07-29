package ipfilterdsl.combiparser;

import ipfilterdsl.token.TokenBuffer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Parser<I,R> {

    private Action<I,R> action = null;

    public Parser(Action<I,R> actionFunc) {
        this.action = actionFunc;
    }

    public abstract ParseResult parse(TokenBuffer tokenBuffer);

    public R action(I input) {
        return action.tranform(input);
    }

}
