package database.service.impl;

import database.service.PublisherService;
import entity.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import database.repository.PublisherRepository;

import java.util.List;
@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public Publisher addPublisher(Publisher publisher) {
        Publisher savedPublisher = publisherRepository.saveAndFlush(publisher);
        return savedPublisher;
    }

    @Override
    public void delete(Publisher publisher) {
        publisherRepository.delete(publisher);

    }

    @Override
    public Publisher getByName(String name) {
        return publisherRepository.findByName(name);
    }

    @Override
    public Publisher editPublisher(Publisher publisher) {
        return publisherRepository.saveAndFlush(publisher);
    }

    @Override
    public List<Publisher> getAll() {
        return publisherRepository.findAll();
    }
}
