package bobo.methodproxy;

public class GreetingService {

    public void hello() {
        System.out.println("Hello world!");
    }

    @LogInvocation
    public void gloryToUkraine() {
        System.out.println("Glory to Ukraine!!!");
    }
}
