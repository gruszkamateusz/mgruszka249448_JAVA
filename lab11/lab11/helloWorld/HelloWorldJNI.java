
public class HelloWorldJNI {

    static {
        System.loadLibrary("libnative");
    }
    
    public static void main(String[] args) {
        new HelloWorldJNI().sayHello();
    }

    private native void sayHello();
}
