package client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;

public class Controller {
    @FXML
    private TableView booksTable;
    @FXML
    private TableView authorsTable;
    @FXML
    private TableColumn<BookRecord, Integer> bookIdCol;
    @FXML
    private TableColumn<BookRecord, String> bookTitleCol;
    @FXML
    private TableColumn<BookRecord, String> bookAuthorNameCol;
    @FXML
    private TableColumn<BookRecord, Integer> bookYearCol;
    @FXML
    private TableColumn<BookRecord, String> bookPublisherCol;
    @FXML
    private TableColumn<BookRecord, String> bookBriefCol;
    @FXML
    private TableColumn<AuthorRecord, Integer> authorIdCol;
    @FXML
    private TableColumn<AuthorRecord, String> authorNameCol;
    @FXML
    private TableColumn<AuthorRecord, Integer> authorBooksCountCol;

    private ObservableList<BookRecord> bookRecords;
    private ObservableList<AuthorRecord> authorRecords;

    //Форма ввода данных для книги
    @FXML
    private TextField bookIdInp;
    @FXML
    private TextField bookTitleInp;
    @FXML
    private ComboBox<String> bookAuthorInp;
    @FXML
    private TextField bookYearInp;
    @FXML
    private TextField bookPublisherInp;
    @FXML
    private TextArea bookBriefInp;

    //Форма ввода данных для автора
    @FXML
    private TextField authorIdInp;
    @FXML
    private TextField authorNameInp;

    //Селекторы
    @FXML
    private ToggleGroup group1;
    @FXML
    private ToggleGroup group2;
    @FXML
    private RadioButton selectBook;
    @FXML
    private RadioButton selectAuthor;
    @FXML
    private RadioButton selectAddOperation;
    @FXML
    private RadioButton selectEditOperation;
    @FXML
    private RadioButton selectDelOperation;

    public class BookRecord {
        private int id;
        private String title;
        private String authorName;
        private int year;
        private String publisher;
        private String brief;

        public BookRecord(int id, String title, String authorName, int year, String publisher, String brief) {
            this.id = id;
            this.title = title;
            this.authorName = authorName;
            this.year = year;
            this.publisher = publisher;
            this.brief = brief;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }
    }

    public class AuthorRecord {
        private int id;
        private String name;
        private int booksCount;

        public AuthorRecord(int id, String name, int booksCount) {
            this.id = id;
            this.name = name;
            this.booksCount = booksCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBooksCount() {
            return booksCount;
        }

        public void setBooksCount(int booksCount) {
            this.booksCount = booksCount;
        }
    }

    public void initialize() {
        group1 = new ToggleGroup();
        group2 = new ToggleGroup();
        selectBook.setToggleGroup(group1);
        selectAuthor.setToggleGroup(group1);
        selectAddOperation.setToggleGroup(group2);
        selectEditOperation.setToggleGroup(group2);
        selectDelOperation.setToggleGroup(group2);

        bookRecords = FXCollections.observableArrayList();
        authorRecords = FXCollections.observableArrayList();

        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        bookYearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        bookPublisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookBriefCol.setCellValueFactory(new PropertyValueFactory<>("brief"));

        authorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorBooksCountCol.setCellValueFactory(new PropertyValueFactory<>("booksCount"));

        booksTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        bookRecords.add(new BookRecord(0, "test", "author1", 2017, "EKSMO", "aaaa?"));
        booksTable.setItems(bookRecords);
    }

    public void runModification(ActionEvent event) {

    }

    public void runViewBooks(ActionEvent event) {

    }

    public void runViewAuthors(ActionEvent event) {

    }

    public void selectBook(ActionEvent event) {
        System.out.println("Operand type Book is selected.");
    }

    public void selectAuthor(ActionEvent event) {
        System.out.println("Operand type Author is selected.");
    }

    public void selectAddOperation(ActionEvent event) {
        System.out.println("Operation Add is selected.");
    }

    public void selectEditOperation(ActionEvent event) {
        System.out.println("Operation Edit is selected.");
    }

    public void selectDelOperation(ActionEvent event) {
        System.out.println("Operation Delete is selected.");
    }

    //==================== Функции меню =====================
    private File currentFile;

    public void openFileAction(ActionEvent event) {
        openFile();
    }

    public void saveFileAction(ActionEvent event) {
        saveFile();
    }

    public void saveAsFileAction(ActionEvent event) {
        File file = chooseFile();
        if (file != null) {
            currentFile = file;
            saveFile();
        }
    }

    public void closeAction(ActionEvent event) {
        closeApplication();
    }

    private void openFile() {
        File file = chooseFile();
        if (file != null) {
            currentFile = file;
            //выгрузки информации из файла
        }
    }

    private void saveFile() {
        //Сохраняет в файл, находящийся в currentFile
    }

    private File chooseFile() {
        return null;
    }

    private void closeApplication() {
        saveFile();
        System.exit(0);
        //Закрытие приложения с автоматическим вызовом saveFile
    }
}
