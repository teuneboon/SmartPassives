package gd.leet.smartpassives;

public class Application {
    public void run() throws Exception {
        System.out.println("TEST");
    }

    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}