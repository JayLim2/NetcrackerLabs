package database.postgresql;

import database.daointerfaces.BookDAO;
import factories.BookFactory;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLBookDAO implements BookDAO {
    private final Connection connection;

    public PostgreSQLBookDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book create(String bookName, int publishYear, String brief, int publisherID) throws SQLException {
        String sql = "INSERT INTO \"book\" (\"bookName\", \"publishYear\", \"brief\", \"publisherID\") VALUES (?,?,?,?);";
        String sqlSelect = "SELECT * FROM \"book\" WHERE \"bookName\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, bookName);
            stm.setInt(2, publishYear);
            stm.setString(3, brief);
            stm.setInt(4, publisherID);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, bookName);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                return BookFactory.getInstance().createBook(rs2.getInt("bookID"),
                        rs2.getString("bookName"),
                        rs2.getInt("publishYear"),
                        rs2.getString("brief"),
                        rs2.getInt("publisherID"));
            }
        }
    }

    @Override
    public Book read(int bookID) throws SQLException {
        String sql = "SELECT * FROM  \"book\" WHERE \"bookID\" = ?;";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, bookID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return BookFactory.getInstance().createBook(rs.getInt("bookID"),
                    rs.getString("bookName"),
                    rs.getInt("publishYear"),
                    rs.getString("brief"),
                    rs.getInt("publisherID"));
        }
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE \"book\" SET \"bookName\" = ?, \"publishYear\" = ?, " +
                "\"brief\" = ?, \"publisherID\" = ? WHERE \"bookID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, book.getBookName());
            stm.setInt(2, book.getPublushYear());
            stm.setString(3, book.getBrief());
            stm.setInt(4, book.getPublisherID());
            stm.setInt(5, book.getBookID());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(int bookID) throws SQLException {
        String sql = "DELETE FROM \"book\" WHERE \"bookID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, bookID);
            stm.executeUpdate();
        }
    }

    @Override
    public List<Book> getAll() throws SQLException {
        List<Book> list = new LinkedList<>();
        String sql = "SELECT * FROM \"book\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(BookFactory.getInstance().createBook(rs.getInt("bookID"),
                        rs.getString("bookName"),
                        rs.getInt("publishYear"),
                        rs.getString("brief"),
                        rs.getInt("publisherID")));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
