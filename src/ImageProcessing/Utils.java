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
//        System.out.println("testing value " + x + ", " + y + " with line r:" + r + ", phi:" + phi);
        return r == x * Math.cos(phi) + y * Math.sin(phi);
    }

    @FunctionalInterface
    public interface Function<T,S>{
        S Apply(T arg);
    }

    public static class ArrayData {
        public final int[] dataArray;
        public final int width;
        public final int height;

        public ArrayData(int width, int height) {
            this(new int[width * height], width, height);
        }

        public ArrayData(int[] dataArray, int width, int height) {
            this.dataArray = dataArray;
            this.width = width;
            this.height = height;
        }

        public int get(int x, int y) {
            return dataArray[y * width + x];
        }

        public void set(int x, int y, int value) {
            dataArray[y * width + x] = value;
        }

        public void accumulate(int x, int y, int delta) {
            set(x, y, get(x, y) + delta);
        }
    }
}
