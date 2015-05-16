package ImageProcessing;

public class Utils {

    private Utils() {}

    public static class Pair<T, S> {

        private final T t;
        private final S s;

        public Pair(T t, S s) {
            this.t = t;
            this.s = s;
        }

        public T _1() { return t; }
        public S _2() { return s; }
    }

    public static boolean goesThrough(int x, int y, int r, int phi) {
        return r == x * Math.cos(phi) + y * Math.sin(phi);
    }

    @FunctionalInterface
    public interface Function<T,S>{
        S Apply(T arg);
    }
}
