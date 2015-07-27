package ipfilter;

public class IpRange {
    private String range;
    public IpRange(String range) {
        this.range = range;
    }

    public boolean ipIn(String ip) {
        return false;
    }

    @Override
    public String toString() {
        return "IpRange{" +
                "range='" + range + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        IpRange that = (IpRange) other;
        return !(range != null ? !range.equals(that.range) : that.range != null);
    }


}
