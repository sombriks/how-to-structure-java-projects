package sample.structure;

public class HelloThere {

    public static void main(String ...args) throws Exception {
        new Grievous()
                .coolMessages()
                .messages()
                .forEach(System.out::println);
    }
}
