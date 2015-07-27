package ipfilter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IpFilterTest {

    @Test
    public void given_no_config_then_allow_all() throws Exception {
        IpFilter ipFilter = new IpFilter();
        assertThat(ipFilter.allow("1.2.3.4"), equalTo(true));
        assertThat(ipFilter.allow("3.4.5.6"), equalTo(true));
    }

    @Test
    public void given_no_config_and_denyallow_then_deny_all() throws Exception {
        IpFilter ipFilter = new IpFilter();
        ipFilter.setAllowFirst(false);
        assertThat(ipFilter.allow("1.2.3.4"), equalTo(false));
        assertThat(ipFilter.allow("3.4.5.6"), equalTo(false));
    }

    @Test
    public void given_allow_address_range_then_allow_only_ip_in_range() throws Exception {
        IpFilter ipFilter = new IpFilter();
        IpRange ipRange = mock(IpRange.class);
        when(ipRange.ipIn("1.2.3.4")).thenReturn(true);
        ipFilter.addAllowIpRange(ipRange);

        assertThat(ipFilter.allow("1.2.3.4"), equalTo(true));
        assertThat(ipFilter.allow("1.2.3.5"), equalTo(false));
    }

}
