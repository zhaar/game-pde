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

    public static Pair<Double, Double> cartesianToPolar(double x, double y) {
        double phi = Math.atan(y / x);
        return new Pair<>(x * Math.cos(phi) + y * Math.sin(phi), phi);
    }

    @FunctionalInterface
    public interface Function<T,S>{
        S Apply(T arg);
    }
}
