package ipfilterdsl.combiparser;

public class ParseResult {
    private boolean success;
    private String value;

    public ParseResult(boolean success, String value) {
        this.success = success;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getValue() {
        return value;
    }

    public static ParseResult success() {
        return new ParseResult(true, "");
    }
    public static ParseResult fail() {
        return new ParseResult(false, "");
    }

}
