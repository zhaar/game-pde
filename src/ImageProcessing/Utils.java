package ImageProcessing;

public class Utils {

    private Utils() {}

    public static class Pair<T, S> {

        public final T _1;
        public final S _2;
        public final T r;
        public final S phi;

        public Pair(T t, S s) {
            this._1 = t;
            this._2 = s;
            this.r = t;
            this.phi = s;
        }

        @Override
        public String toString() {
            return "(" + _1.toString() + ", " + _2.toString() + ")";
        }
    }

    public static class Triple<T, S, R> {
        public final T _1;
        public final S _2;
        public final R _3;

        public Triple(T t, S s, R r) {
            this._1 = t;
            this._2 = s;
            this._3 = r;
        }

        @Override
        public String toString() {
            return "(" + _1.toString() + ", " + _2.toString() + ", " + _3.toString() + ")";
        }
    }

    public static boolean goesThrough(int x, int y, int r, int phi) {
        return r == x * Math.cos(phi) + y * Math.sin(phi);
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

        /**
         * Radius, and then angle
         * @param r radius
         * @param phi angle
         * @return value at (r,phi)
         */
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
