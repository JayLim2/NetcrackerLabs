package database.postgresql;

import database.daointerfaces.AuthorDAO;
import models.Author;

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
        Author author;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, authorName);
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlSelect)) {
                stm2.setString(1, authorName);
                ResultSet rs2 = stm2.executeQuery();
                rs2.next();
                author = new Author(rs2.getString("authorName"));

            }
            return author;
        }
    }

    @Override
    public Author read(int authorID) {
        return null;
    }

    @Override
    public void update(Author author) {

    }

    @Override
    public void delete(int authorID) {

    }

    @Override
    public List<Author> getAll() throws SQLException {
        List<Author> list = new LinkedList<>();
        String sql = "SELECT * FROM \"author\"";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(new Author(rs.getString("authorName")));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
