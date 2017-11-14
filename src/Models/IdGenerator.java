package Models;

import java.util.UUID;

public class IdGenerator {
    public static UUID createId(){
        return UUID.randomUUID();
    }
}
