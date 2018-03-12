package database.daointerfaces;

import model.Publisher;

import java.sql.SQLException;
import java.util.List;

public interface PublisherDAO {

    Publisher create(String publisherName) throws SQLException;

    Publisher read(int publisherID) throws SQLException;

    void update(Publisher publisher) throws SQLException;

    void delete(int publisherID) throws SQLException;

    List<Publisher> getAll()throws SQLException;
}


