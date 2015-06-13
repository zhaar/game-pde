package ImageProcessing.GenericProcess;

public interface GenericProcess<T, U> {
    U compute(T source);
}
