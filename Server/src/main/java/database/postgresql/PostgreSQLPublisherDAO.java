package database.postgresql;

import database.daointerfaces.PublisherDAO;
import factories.PublisherFactory;
import model.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLPublisherDAO implements PublisherDAO {
    private final Connection connection;

    public PostgreSQLPublisherDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Publisher create(String publisherName) throws SQLException {
        String sql = "INSERT INTO \"publisher\" (\"publisherName\") VALUES (?);";
        String sqlSelect = "SELECT * FROM \"publisher\" WHERE \"publisherName\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, publisherName);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, publisherName);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                return PublisherFactory.getInstance().createPublisher(rs2.getInt("publisherID"),
                        rs2.getString("publisherName"));
            }
        }
    }

    @Override
    public Publisher read(int publisherID) throws SQLException {
        String sql = "SELECT * FROM  \"publisher\" WHERE \"publisherID\" = ?;";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, publisherID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return PublisherFactory.getInstance().createPublisher(rs.getInt("publisherID"), rs.getString("publisherName"));
        }
    }
    
    @Override
    public Publisher read(String publisherName) throws SQLException {
        String sql = "SELECT * FROM  \"publisher\" WHERE \"publisherName\" = ?;";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, publisherName);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return PublisherFactory.getInstance().createPublisher(rs.getInt("publisherID"), rs.getString("publisherName"));
        }
    }

    @Override
    public void update(Publisher publisher) throws SQLException {
        String sql = "UPDATE \"publisher\" SET \"publisherName\" = ? WHERE \"publisherID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, publisher.getPublisherName());
            stm.setInt(2, publisher.getPublisherID());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(int publisherID) throws SQLException {
        String sql = "DELETE FROM \"publisher\" WHERE \"publisherID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, publisherID);
            stm.executeUpdate();
        }
    }

    @Override
    public List<Publisher> getAll() throws SQLException {
        List<Publisher> list = new LinkedList<>();
        String sql = "SELECT * FROM \"publisher\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(PublisherFactory.getInstance().createPublisher(rs.getInt("publisherID"),
                        rs.getString("publisherName")));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
