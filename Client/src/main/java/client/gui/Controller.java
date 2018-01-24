package client.gui;

import client.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Author;
import models.AuthorsContainer;
import models.Book;
import models.YearOutOfBoundsException;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    //Ограничения ввода
    private static final int BOOK_TITLE_CONSTRAINT = 50;
    private static final int BOOK_YEAR_MIN = 0;
    private static final int BOOK_YEAR_MAX = 2017;
    private static final int BOOK_PUBLISHER_CONSTRAINT = 35;
    private static final int BOOK_BRIEF_CONSTRAINT = 280;
    private static final int AUTHOR_NAME_CONSTRAINT = 50;
    private Client client;

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
    private ComboBox<Author> bookAuthorInp;
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

    @FXML
    private Button runOperationBtn;

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

    private ClientInterface clientInterface;

    public void initialize() {
        try {
            Socket clientSocket = new Socket(InetAddress.getLocalHost(), 4444);
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();

            JAXBContext contextCommands = JAXBContext.newInstance(CommandPacket.class, ViewBooksPacket.class, AddBookPacket.class, SetBookPacket.class, RemoveBookPacket.class, AddAuthorPacket.class, SetAuthorPacket.class, RemoveAuthorPacket.class);
            Marshaller commandMarshaller = contextCommands.createMarshaller();
            XMLInputFactory xmi = XMLInputFactory.newFactory();

            //Создание объекта вспомогательного класса, созданного только для общения с сервером
            clientInterface = new ClientInterface(clientSocket, out, in, commandMarshaller, contextCommands, xmi);
           // client = Client.getInstance();
         //   if(client.connect()==1) System.out.println("error connect to server");


            updateAuthorsCombobox();
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост.");
        } catch (IOException e) {
            System.out.println("Ошибка механизма ввода-вывода.");
            e.printStackTrace();
        } catch (JAXBException e) {
            System.out.println("Ошибка XML-сериализации.");
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("Ошибка потока XML.");
            e.printStackTrace();
        }

        //================================================
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

        //bookRecords.add(new BookRecord(0, "test", "author1", 2017, "EKSMO", "aaaa?"));
        //booksTable.setItems(bookRecords);

        //=================================
    }

    private void updateAuthorsCombobox() throws JAXBException, XMLStreamException {
        AuthorsContainer authorsContainer = clientInterface.viewAuthors();
        ObservableList<Author> authors = FXCollections.observableArrayList();
        authors.addAll(authorsContainer.getAuthors());
        bookAuthorInp.setItems(authors);
    }

    public void runModification(ActionEvent event) throws JAXBException, XMLStreamException {
        Commands currentCommand = null;
        if (selectBook.isSelected()) {
            if (selectAddOperation.isSelected()) {
                currentCommand = Commands.ADD_BOOK;
            }
            if (selectEditOperation.isSelected()) {
                currentCommand = Commands.SET_BOOK;
            }
            if (selectDelOperation.isSelected()) {
                currentCommand = Commands.REMOVE_BOOK;
            }
        } else if (selectAuthor.isSelected()) {
            if (selectAddOperation.isSelected()) {
                currentCommand = Commands.ADD_AUTHOR;
            }
            if (selectEditOperation.isSelected()) {
                currentCommand = Commands.SET_AUTHOR;
            }
            if (selectDelOperation.isSelected()) {
                currentCommand = Commands.REMOVE_AUTHOR;
            }
        }

        if (currentCommand == null)
            return;

        //Считывание информации
        Book book = null;
        Author author = null;
        if (currentCommand != Commands.REMOVE_BOOK && currentCommand != Commands.REMOVE_AUTHOR) {
            book = getBookInfo();
            author = getAuthorInfo();
        }

        BookRecord bookRecord;
        AuthorRecord authorRecord;

        System.out.println(currentCommand);

        switch (currentCommand) {
            case ADD_BOOK: {
                if (book != null) {
                    //bookRecord = new BookRecord(book.getId(), book.getTitle(), book.getAuthor().getName(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                    clientInterface.addBook(book, book.getAuthor());
                    //bookRecords.add(bookRecord);
                }
            }
            break;
            case SET_BOOK: {
                if (book != null) {
                    //int bookId = book.getId();
                    int bookId = -1;
                    try {
                        bookId = Integer.parseInt(bookIdInp.getText());
                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "Уникальный индекс книги должен быть числом.").show();
                    }
                    //bookRecord = new BookRecord(bookId, book.getTitle(), book.getAuthor().getName(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                    if (bookId != -1) {
                        clientInterface.editBook(bookId, book);
                    }
                    /*int recordsCount = bookRecords.size();
                    int i;
                    for (i = 0; i < recordsCount && bookRecords.get(i).getId() != bookId; i++) ;
                    bookRecords.set(i, bookRecord);*/
                }
            }
            break;
            case REMOVE_BOOK: {
                int id = -1;
                try {
                    id = Integer.parseInt(bookIdInp.getText());
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Уникальный Id книги должен быть числом.").show();
                }
                if (id != -1) {
                    clientInterface.deleteBook(id);
                    /*int i;
                    int recordsCount = bookRecords.size();
                    for (i = 0; i < recordsCount && bookRecords.get(i).getId() != id; i++) ;
                    bookRecords.remove(i);*/
                }
            }
            break;
            case ADD_AUTHOR: {
                if (author != null) {
                    //authorRecord = new AuthorRecord(author.getId(), author.getName(), 0);
                    clientInterface.addAuthor(author.getName());
                    //authorRecords.add(authorRecord);
                }
            }
            break;
            case SET_AUTHOR: {
                if (author != null) {
                    //int authorId = author.getId();
                    int authorId = -1;
                    try {
                        authorId = Integer.parseInt(authorIdInp.getText());
                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "Уникальный индекс автора должен быть числом.").show();
                    }
                    //authorRecord = new AuthorRecord(authorId, author.getName(), author.getBooks().size());
                    if (authorId != -1) {
                        clientInterface.editAuthor(authorId, author.getName());
                    }
                    /*int recordsCount = authorRecords.size();
                    int i;
                    for (i = 0; i < recordsCount && authorRecords.get(i).getId() != authorId; i++) ;
                    authorRecords.set(i, authorRecord);*/
                }
            }
            break;
            case REMOVE_AUTHOR: {
                int id = -1;
                try {
                    id = Integer.parseInt(authorIdInp.getText());
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Уникальный Id автора должен быть числом.").show();
                }
                if (id != -1) {
                    clientInterface.deleteAuthor(id);
                    /*int i;
                    int recordsCount = authorRecords.size();
                    for (i = 0; i < recordsCount && authorRecords.get(i).getId() != id; i++) ;
                    authorRecords.remove(i);*/
                }
            }
            break;
        }
        runViewBooks(event);
        runViewAuthors(event);
        //authorsTable.setItems(authorRecords);
        //booksTable.setItems(bookRecords);
    }

    private Book getBookInfo() {
        Book book = null;
        try {
            String title = bookTitleInp.getText();
            Author bookAuthor = bookAuthorInp.getValue();
            int year = Integer.parseInt(bookYearInp.getText());
            String publisher = bookPublisherInp.getText();
            String brief = bookBriefInp.getText();

            if (title.length() <= BOOK_TITLE_CONSTRAINT &&
                    publisher.length() <= BOOK_PUBLISHER_CONSTRAINT &&
                    (year >= BOOK_YEAR_MIN && year <= BOOK_YEAR_MAX) &&
                    brief.length() <= BOOK_BRIEF_CONSTRAINT) {
                book = new Book(title, bookAuthor, year, publisher, brief);
            } else {
                String errorMsg = "";
                if (title.length() > BOOK_TITLE_CONSTRAINT)
                    errorMsg += "* Название книги превышает допустимое число символов.\n";
                if (year < BOOK_YEAR_MIN || year > BOOK_YEAR_MAX)
                    errorMsg += "* Год издания должен быть в диапазоне от " + BOOK_YEAR_MIN + " до " + BOOK_YEAR_MAX + "\n";
                if (publisher.length() > BOOK_PUBLISHER_CONSTRAINT)
                    errorMsg += "* Название издателя превышает допустимое число символов.\n";
                if (brief.length() > BOOK_BRIEF_CONSTRAINT)
                    errorMsg += "* Длина краткого описания превышает " + BOOK_BRIEF_CONSTRAINT + " символов.\n";
                new Alert(Alert.AlertType.ERROR, errorMsg).show();
            }
        } catch (YearOutOfBoundsException ex) {

        } catch (Exception ex) {

        }
        return book;
    }

    private Author getAuthorInfo() {
        Author author = null;
        try {
            String authorName = authorNameInp.getText();
            if (authorName.length() <= AUTHOR_NAME_CONSTRAINT)
                author = new Author(authorName);
            else
                new Alert(Alert.AlertType.ERROR, "* Длина имени автора превышает допустимое число символов.").show();
        } catch (Exception ex) {

        }
        return author;
    }

    public void runViewBooks(ActionEvent event) {
        try {
            bookRecords.clear();
            authorRecords.clear();

            AuthorsContainer authorsContainer = clientInterface.viewBooks();
            if (authorsContainer != null) {
                List<Author> authors = authorsContainer.getAuthors();
                for (Author author : authors) {
                    authorRecords.add(new AuthorRecord(author.getId(), author.getName(), author.getBooks().size()));
                    List<Book> books = author.getBooks();
                    for (Book book : books) {
                        bookRecords.add(new BookRecord(book.getId(), book.getTitle(), author.getName(), book.getPublishYear(), book.getPublisher(), book.getBrief()));
                    }
                }

                System.out.println("Список книг получен.");
            } else {
                System.out.println("Список книг НЕ получен.");
            }

            updateAuthorsCombobox();

            booksTable.setItems(bookRecords);
            authorsTable.setItems(authorRecords);

            enableModificationForm();
        } catch (XMLStreamException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            System.out.println("Ошибка XML-сериализаци.");
            ex.printStackTrace();
        }
    }

    private void enableModificationForm() {
        selectBook.setDisable(false);
        selectAuthor.setDisable(false);
        selectAddOperation.setDisable(false);
        selectEditOperation.setDisable(false);
        selectDelOperation.setDisable(false);
        enableBookMainInfo();
        runOperationBtn.setDisable(false);
    }

    //Применится потом: в случае возникновения ошибки чтения с сервера (из файла), форма блокируется
    private void disableModificationForm() {
        selectBook.setDisable(true);
        selectAuthor.setDisable(true);
        selectAddOperation.setDisable(true);
        selectEditOperation.setDisable(true);
        selectDelOperation.setDisable(true);
        bookIdInp.setDisable(true);
        authorIdInp.setDisable(true);
        disableBookMainInfo();
        runOperationBtn.setDisable(true);
    }

    public void runViewAuthors(ActionEvent event) {
        runViewBooks(event);
        /*try {
            AuthorsContainer authorsContainer = clientInterface.viewAuthors();
            System.out.println("Список авторов получен.");
        } catch (XMLStreamException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            System.out.println("Ошибка XML-сериализаци.");
            ex.printStackTrace();
        }*/
    }

    public void selectBook(ActionEvent event) {
        //Отключение формы с автором

        authorIdInp.setDisable(true);
        authorNameInp.setDisable(true);

        if (selectDelOperation.isSelected() || selectEditOperation.isSelected())
            bookIdInp.setDisable(false);
        else
            bookIdInp.setDisable(true);

        if (!selectDelOperation.isSelected()) {
            enableBookMainInfo();
        } else {
            disableBookMainInfo();
        }

        System.out.println("Operand type Book is selected.");
    }

    public void selectAuthor(ActionEvent event) {

        if (selectEditOperation.isSelected() || selectDelOperation.isSelected())
            authorIdInp.setDisable(false);
        else
            authorIdInp.setDisable(true);

        if (!selectDelOperation.isSelected())
            authorNameInp.setDisable(false);
        else
            authorNameInp.setDisable(true);

        //Отключение формы с книгой
        bookIdInp.setDisable(true);
        disableBookMainInfo();


        System.out.println("Operand type Author is selected.");
    }

    private void enableBookMainInfo() {
        bookTitleInp.setDisable(false);
        bookAuthorInp.setDisable(false);
        bookYearInp.setDisable(false);
        bookPublisherInp.setDisable(false);
        bookBriefInp.setDisable(false);
    }

    private void disableBookMainInfo() {
        bookTitleInp.setDisable(true);
        bookAuthorInp.setDisable(true);
        bookYearInp.setDisable(true);
        bookPublisherInp.setDisable(true);
        bookBriefInp.setDisable(true);
    }

    private void disableAllInfo() {
        disableBookMainInfo();
        bookIdInp.setDisable(true);
        authorIdInp.setDisable(true);
        authorNameInp.setDisable(true);
    }

    public void selectAddOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            bookIdInp.setDisable(true);
            enableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            authorIdInp.setDisable(true);
            authorNameInp.setDisable(false);
        }

        System.out.println("Operation Add is selected.");
    }

    public void selectEditOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            bookIdInp.setDisable(false);
            enableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            authorIdInp.setDisable(false);
            authorNameInp.setDisable(false);
        }

        System.out.println("Operation Edit is selected.");
    }

    public void selectDelOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            bookIdInp.setDisable(false);
            disableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            authorIdInp.setDisable(false);
            authorNameInp.setDisable(true);
        }

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
