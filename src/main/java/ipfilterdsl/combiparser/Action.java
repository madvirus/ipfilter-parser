package ipfilterdsl.combiparser;

public interface Action<I, O> {
    O tranform(I input);
}
