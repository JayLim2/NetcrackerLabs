package database.service;


import entity.Publisher;

import java.util.List;


public interface PublisherService {

    Publisher addPublisher(Publisher publisher);

    void delete(Publisher publisher);

    Publisher getByName(String name);

    Publisher editPublisher(Publisher publisher);

    List<Publisher> getAll();

}
