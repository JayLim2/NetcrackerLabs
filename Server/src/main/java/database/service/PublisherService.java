package database.service;


import entity.Publisher;

import java.util.List;
import org.springframework.stereotype.Service;


public interface PublisherService {

    Publisher addPublisher(Publisher publisher);

    void delete(Publisher publisher);

    Publisher getByName(String name);
    
    Publisher getByID(int id);

    Publisher editPublisher(Publisher publisher);

    List<Publisher> getAll();

}
