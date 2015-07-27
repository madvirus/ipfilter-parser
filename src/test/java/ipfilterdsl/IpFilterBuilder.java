package ipfilterdsl;

import ipfilter.IpFilter;
import ipfilter.IpRange;

public class IpFilterBuilder {
    private IpFilter filter = new IpFilter();

    public static IpFilterBuilder ipFilter() {
        return new IpFilterBuilder();
    }
    public IpFilterBuilder allow(String ip) {
        filter.addAllowIpRange(new IpRange(ip));
        return this;
    }

    public IpFilterBuilder deny(String ip) {
        filter.addDenyIpRange(new IpRange(ip));
        return this;
    }

    public IpFilterBuilder allowFirst(boolean allowFirst) {
        filter.setAllowFirst(allowFirst);
        return this;
    }

    public IpFilter result() {
        return filter;
    }
}
