package server;

public class IdGenerator {
    private int id;
    private static IdGenerator instance;

    private IdGenerator(int id) {
        this.id = id;
    }

    public static synchronized IdGenerator getInstance(int id){
        if (instance == null)
            instance = new IdGenerator(id);
        return instance;
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null)
            instance= new IdGenerator(0);
        return instance;
    }

    public synchronized int createId() {
        return  ++id;
    }
}
