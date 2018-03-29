package database.postgresql;

import database.daointerfaces.AuthorDAO;
import factories.AuthorFactory;
import model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLAuthorDAO implements AuthorDAO {
    private final Connection connection;

    public PostgreSQLAuthorDAO(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Author create(String authorName) throws SQLException {
        String sql = "INSERT INTO \"author\" (\"authorName\") VALUES (?);";
        String sqlSelect = "SELECT * FROM \"author\" WHERE \"authorName\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, authorName);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, authorName);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                return AuthorFactory.getInstance().createAuthor(rs2.getInt("authorID"),
                        rs2.getString("authorName"));
            }
        }
    }

    @Override
    public Author read(int authorID) throws SQLException {
        String sql = "SELECT * FROM  \"author\" WHERE \"authorID\" = ?;";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, authorID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return AuthorFactory.getInstance().createAuthor(rs.getInt("authorID"), rs.getString("authorName"));
        }
    }

    @Override
    public void update(Author author) throws SQLException {
        String sql = "UPDATE \"author\" SET \"authorName\" = ? WHERE \"authorID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, author.getAuthorName());
            stm.setInt(2, author.getAuthorID());
            stm.executeUpdate();
        }
    }

    @Override
    public void delete(int authorID) throws SQLException {
        String sql = "DELETE FROM \"author\" WHERE \"authorID\" = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, authorID);
            stm.executeUpdate();
        }
    }

    @Override
    public List<Author> getAll() throws SQLException {
        List<Author> list = new LinkedList<>();
        String sql = "SELECT * FROM \"author\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(AuthorFactory.getInstance().createAuthor(rs.getInt("authorID"),
                        rs.getString("authorName")));
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public Author read(String authorName) throws SQLException {
        String sql = "SELECT * FROM  \"author\" WHERE \"authorName\" = ?;";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, authorName);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return AuthorFactory.getInstance().createAuthor(rs.getInt("authorID"), rs.getString("authorName"));
        }
    }
}
