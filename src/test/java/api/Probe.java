package api;

public interface Probe {

    default void go(){
        int a = 2;
    }

    default int go1(){
        int a = 2;
        return 2;
    }

    static void go2(){

    }
}
