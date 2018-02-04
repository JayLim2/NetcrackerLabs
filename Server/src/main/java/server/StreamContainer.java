package server;

import com.sun.deploy.util.SessionState;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;

import java.io.OutputStream;
import java.util.*;

public class StreamContainer {
    private ArrayList <OutputStream> clientOutputStreams;
    private static StreamContainer instance;
    private Map<Integer, OutputStream > streamMap;

    private StreamContainer() {
        this.streamMap = new HashMap<>();
    }

    public static StreamContainer getInstance() {
        if (instance == null)
            instance = new StreamContainer();
        return instance;
    }

    public List<OutputStream> getStreams(){
       // return Collections.unmodifiableList(clientOutputStreams);
        LinkedList<OutputStream> list = new LinkedList<>(streamMap.values());
        return Collections.unmodifiableList(list);
    }

    public void addStream(Integer key, OutputStream stream) {
        streamMap.put(key, stream);
    }


   //todo не забыть удалить
   public void View() {
        for (Map.Entry entry : streamMap.entrySet()) {
            Integer key = (Integer) entry.getKey();
            OutputStream value = (OutputStream) entry.getValue();
            System.out.println("Key: " + key +"  Value: " + value);
        }
    }

    public void removeStream(Integer key){
        streamMap.remove(key);
    }
}
