package database.dao.postgresql;

import database.dao.daointerfaces.BookDAO;
import factories.BookFactory;
import entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostgreSQLBookDAO implements BookDAO {
    private final Connection connection;

    public PostgreSQLBookDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book create(String bookName, int publishYear, String brief, int publisherID, int[] authorIDs) throws SQLException {
        String sql = "INSERT INTO \"book\" (\"bookName\", \"publishYear\", \"brief\", \"publisherID\") VALUES (?,?,?,?);";
        String sqlConnect = "INSERT INTO \"authorBookConnector\" VALUES (?,?);";
        String sqlSelect = "SELECT * FROM \"book\" WHERE \"bookName\" = ?;";
        String sqlAuthorGet = "SELECT \"authorName\" FROM \"author\" WHERE \"authorID\" = ?;";
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
                try(PreparedStatement stm3 = connection.prepareStatement(sqlConnect)){
                    try(PreparedStatement stm4 = connection.prepareStatement(sqlAuthorGet)){
                        List<String> authorNames = new ArrayList<String>();
                        stm3.setInt(2, rs2.getInt("bookID"));
                        for(int authorID: authorIDs){
                            stm3.setInt(1, authorID);
                            stm3.executeUpdate();
                            stm4.setInt(1, authorID);
                            ResultSet rs4 = stm4.executeQuery();
                            rs4.next();
                            authorNames.add(rs4.getString(1));
                        }
                        return BookFactory.getInstance().createBook(rs2.getInt("bookID"),
                            rs2.getString("bookName"),
                            rs2.getInt("publishYear"),
                            rs2.getString("brief"),
                            rs2.getInt("publisherID"),
                            authorNames);
                    }
                }
            }
        }
    }

    @Override
    public Book read(int bookID) throws SQLException {
        String sql = "SELECT * FROM  \"book\" WHERE \"bookID\" = ?;";
        String retAuthors = "SELECT \"authorName\" FROM \"author\" WHERE "
                + "\"authorID\" IN (SELECT \"authorID\" FROM \"authorBookConnector\" "
                + "WHERE \"bookID\" = ?);" ;  
                
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, bookID);
            try(PreparedStatement stm2 = connection.prepareStatement(retAuthors)){
                stm2.setInt(1,bookID);
                ResultSet rs2 = stm2.executeQuery();
                List<String> authorNameList = new ArrayList<String>();
                while(rs2.next()){
                    authorNameList.add(rs2.getString(1));
                }
                ResultSet rs = stm.executeQuery();
                rs.next();
                return BookFactory.getInstance().createBook(rs.getInt("bookID"),
                        rs.getString("bookName"),
                        rs.getInt("publishYear"),
                        rs.getString("brief"),
                        rs.getInt("publisherID"),
                        authorNameList);
            }
        }
    }

    @Override
    public void update(Book book, int[] authorIDs) throws SQLException {
        String sql = "UPDATE \"book\" SET \"bookName\" = ?, \"publishYear\" = ?, " +
                "\"brief\" = ?, \"publisherID\" = ? WHERE \"bookID\" = ?";
        String sqlClearConnection = "DELETE FROM \"authorBookConnector\" WHERE \"bookID\" = ?";
        String sqlConnect = "INSERT INTO \"authorBookConnector\" VALUES (?,?);";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, book.getBookName());
            stm.setInt(2, book.getPublishYear());
            stm.setString(3, book.getBrief());
            stm.setInt(4, book.getPublisherID());
            stm.setInt(5, book.getBookID());
            stm.executeUpdate();
            try (PreparedStatement stm2 = connection.prepareStatement(sqlClearConnection)){
                stm2.setInt(1, book.getBookID());
                stm2.executeUpdate();
                try(PreparedStatement stm3 = connection.prepareStatement(sqlConnect)){
                    stm3.setInt(2, book.getBookID());
                    for(int authorID: authorIDs){
                        stm3.setInt(1, authorID);
                        stm3.executeUpdate();
                    }
                }
            }
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
                String retAuthors = "SELECT \"authorName\" FROM \"author\" WHERE "
                + "\"authorID\" IN (SELECT \"authorID\" FROM \"authorBookConnector\" "
                + "WHERE \"bookID\" = ?);" ;  
                try(PreparedStatement stm2 = connection.prepareStatement(retAuthors)){
                    stm2.setInt(1,rs.getInt("bookID"));
                    ResultSet rs2 = stm2.executeQuery();
                    List<String> authorNameList = new ArrayList<String>();
                    while(rs2.next()){
                        authorNameList.add(rs2.getString(1));
                    }
                list.add(BookFactory.getInstance().createBook(rs.getInt("bookID"),
                        rs.getString("bookName"),
                        rs.getInt("publishYear"),
                        rs.getString("brief"),
                        rs.getInt("publisherID"),
                        authorNameList));
                }
            }
        }
        return Collections.unmodifiableList(list);
    }
}
