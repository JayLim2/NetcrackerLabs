package entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PublisherContainer {

    private HashMap<Integer, Publisher> publishers;

    public PublisherContainer() {
        this.publishers = new HashMap<>();
    }

    public void addPublisher(Publisher publisher) {
        publishers.put(publisher.getPublisherID(), publisher);
    }

    public void deletePublisher(int id) {
        publishers.remove(id);
    }

    public Publisher getPublisher(int id) {
        return publishers.get(id);
    }

    public List<Publisher> getPublishers() {
        List<Publisher> publishersList = new LinkedList<>(publishers.values());
        return Collections.unmodifiableList(publishersList);
    }
}
