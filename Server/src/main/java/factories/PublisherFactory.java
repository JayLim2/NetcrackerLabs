package factories;

import model.Publisher;

public class PublisherFactory {

    private static PublisherFactory instance;

    public static PublisherFactory getInstance() {
        if (instance == null)
            instance = new PublisherFactory();
        return instance;
    }

    public Publisher createPublisher(int publisherID, String publisherName) {
        return new Publisher(publisherID, publisherName);
    }
}
