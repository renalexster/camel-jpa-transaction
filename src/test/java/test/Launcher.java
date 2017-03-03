package test;


import org.apache.camel.spring.Main;

public class Launcher {
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
    }
}
