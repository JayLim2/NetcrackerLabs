package factories;

import model.Publisher;

public class PublisherFactory {

    public static Publisher createPublisher(int publisherID, String publisherName){
        return new Publisher(publisherID, publisherName);
    }
}
