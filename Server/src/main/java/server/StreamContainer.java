package server;

import com.sun.deploy.util.SessionState;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StreamContainer {
    private ArrayList <OutputStream> clientOutputStreams;
    private static StreamContainer instance;
    //private Map<Integer, > streamMap;

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
