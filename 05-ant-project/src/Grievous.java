import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Grievous {

    private Gson gson = new Gson();

    public Quotes coolMessages() throws Exception {
        return gson.fromJson(Files
                .readString(Paths.get("./messages.json")), Quotes.class);

    }
}
