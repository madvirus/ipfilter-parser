package ipfilterdsl.combiparser;

public interface Action<I, R> {
    R tranform(I input);
}
