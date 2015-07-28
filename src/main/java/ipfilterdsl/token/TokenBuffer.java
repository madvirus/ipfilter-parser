package ipfilterdsl.token;

import java.util.List;

public class TokenBuffer {
    private List<Token> tokenList;
    private int currentPosition = 0;

    public TokenBuffer(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Token currentToken() {
        return tokenList.get(currentPosition);
    }

    public Token currentTokenAndMoveNext() {
        return tokenList.get(currentPosition++);
    }

    public boolean hasNext() {
        return currentPosition < tokenList.size() - 1;
    }

    public boolean hasCurrent() {
        return currentPosition < tokenList.size();
    }

    public int currentPosition() {
        return currentPosition;
    }

    public void resetPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void movePrevious() {
        currentPosition--;
    }

    public void moveNext() {
        currentPosition++;
    }
}
