package ipfilterdsl.token;

public enum TokenType {
    TT_ALLOW("^allow", true),
    TT_DENY("^deny", true),
    TT_ORDER("^order", true),
    TT_IPRANGE("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]((/|-)[0-9]+)?", true),
    TT_COMMA(",", true),
    TT_WS("^[ \\t\\r\\n]+", false),
    TT_EOF(null, true);

    private String regex;
    private boolean outputIncluded;

    TokenType(String regex, boolean outputIncluded) {
        this.regex = regex;
        this.outputIncluded = outputIncluded;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isOutputIncluded() {
        return outputIncluded;
    }

    public boolean hasRegExp() {
        return regex != null && !regex.isEmpty();
    }
}
