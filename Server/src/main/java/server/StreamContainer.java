package server;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StreamContainer {
    private ArrayList <OutputStream> clientOutputStreams;
    private static StreamContainer instance;

    private StreamContainer() {
        this.clientOutputStreams = new ArrayList<>();
    }

    public static StreamContainer getInstance() {
        if (instance == null)
            instance = new StreamContainer();
        return instance;
    }

    public List<OutputStream> getStreams(){
        return Collections.unmodifiableList(clientOutputStreams);
    }

    public void addStream(OutputStream stream) {
        clientOutputStreams.add(stream);
    }
}
