package ipfilter;

import java.util.ArrayList;
import java.util.List;

public class IpFilter {
    private boolean allowFirst;
    private List<IpRange> allowIpRangeList = new ArrayList<>();
    private List<IpRange> denyIpRangeList = new ArrayList<>();

    public IpFilter() {
        allowFirst = true;
    }

    public boolean allow(String ip) {
        if (allowFirst)
            return true;
        else
            return false;
    }

    public void setAllowFirst(boolean allowFirst) {
        this.allowFirst = allowFirst;
    }

    public void addAllowIpRange(IpRange ipRange) {
        allowIpRangeList.add(ipRange);
    }

    public void addAllowIpRanges(List<IpRange> allowIpRanges) {
        allowIpRangeList.addAll(allowIpRanges);
    }

    public void addDenyIpRange(IpRange ipRange) {
        denyIpRangeList.add(ipRange);
    }

    public void addDenyIpRanges(List<IpRange> denyIpRanges) {
        denyIpRangeList.addAll(denyIpRanges);
    }

    @Override
    public String toString() {
        return "IpFilter{" +
                "allowFirst=" + allowFirst +
                ", allowIpRangeList=" + allowIpRangeList +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !(other instanceof IpFilter)) return false;
        IpFilter that = (IpFilter) other;
        if (this.allowFirst != that.allowFirst) return false;
        if (!this.allowIpRangeList.equals(that.allowIpRangeList)) return false;
        if (!this.denyIpRangeList.equals(that.denyIpRangeList)) return false;
        return true;
    }
}
