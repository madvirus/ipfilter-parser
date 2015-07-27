package ipfilterdsl.token;

import java.util.List;

public class TokenBuffer {
    private List<Token> tokenList;
    private int currentPosition = -1;

    public TokenBuffer(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Token nextToken() {
        return tokenList.get(++currentPosition);
    }

    public Token currentToken() {
        return tokenList.get(currentPosition);
    }

    public boolean hasNext() {
        return currentPosition < tokenList.size() - 1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void resetPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void movePrevious() {
        currentPosition--;
    }
}
