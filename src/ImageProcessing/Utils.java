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
        public T r() { return t; }
        public S phi() { return s; }

        @Override
        public String toString() {
            return "(" + t.toString() + ", " + s.toString() + ")";
        }
    }

    public static boolean goesThrough(int x, int y, int r, int phi) {
//        System.out.println("testing value " + x + ", " + y + " with line r:" + r + ", phi:" + phi);
        return r == x * Math.cos(phi) + y * Math.sin(phi);
    }

    @FunctionalInterface
    public interface Function<T,S>{
        S Apply(T arg);
    }

    public static class ArrayData {
        public final int[] dataArray;
        public final int radius;
        public final int angle;

        public ArrayData(int radius, int angle) {
            this(new int[radius * angle], radius, angle);
        }

        private ArrayData(int[] dataArray, int radius, int angle) {
            this.dataArray = dataArray;
            this.radius = radius;
            this.angle = angle;
        }

        public int get(int r, int phi) {
            return dataArray[phi * radius + r];
        }

        public void set(int r, int phi, int value) {
            dataArray[phi * radius + r] = value;
        }

        public void accumulate(int r, int phi, int delta) {
            dataArray[phi * radius + r] += delta;
        }
    }
}
