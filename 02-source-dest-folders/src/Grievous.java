import java.io.*;
import java.util.*;

public class Grievous {

    public List<String> coolMessages() throws Exception {
        Scanner scan = new Scanner(new File("./messages.txt"));
        List<String> messages = new ArrayList<>();
        while(scan.hasNext()) {
            messages.add(scan.nextLine());
        }
        return messages;
    }
}
