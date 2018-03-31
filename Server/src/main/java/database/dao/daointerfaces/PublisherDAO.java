package database.dao.daointerfaces;

import entity.Publisher;

import java.sql.SQLException;
import java.util.List;

public interface PublisherDAO {

    Publisher create(String publisherName) throws SQLException;

    Publisher read(int publisherID) throws SQLException;
    
    Publisher read(String publisherName) throws SQLException;

    void update(Publisher publisher) throws SQLException;

    void delete(int publisherID) throws SQLException;

    List<Publisher> getAll()throws SQLException;
}


