package ipfilterdsl.token;

import ipfilterdsl.TokenUtil;
import org.junit.Test;

import java.util.Arrays;

import static ipfilterdsl.TokenUtil.allowToken;
import static ipfilterdsl.TokenUtil.denyToken;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TokenBufferTest {
    @Test
    public void basicMoveFunction() throws Exception {
        TokenBuffer tb = new TokenBuffer(Arrays.asList(allowToken(), denyToken()));
        assertThat(tb.currentPosition(), equalTo(0));
        assertThat(tb.currentToken(), equalTo(allowToken()));
        assertThat(tb.hasNext(), equalTo(true));
        assertThat(tb.currentPosition(), equalTo(0));

        tb.moveNext();
        assertThat(tb.currentPosition(), equalTo(1));
        assertThat(tb.currentToken(), equalTo(denyToken()));

        assertThat(tb.hasNext(), equalTo(false));
        assertThat(tb.hasCurrent(), equalTo(true));

        tb.resetPosition(0);
        assertThat(tb.currentPosition(), equalTo(0));
        assertThat(tb.currentTokenAndMoveNext(), equalTo(allowToken()));
        assertThat(tb.currentPosition(), equalTo(1));
    }
}
