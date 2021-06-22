package libs;

public class Scalar {
    static {
        System.load("scalar.o");
    }
    public native void hello();

}
