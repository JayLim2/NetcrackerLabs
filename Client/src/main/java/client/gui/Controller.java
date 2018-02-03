package client.gui;

import client.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.*;
import protocol.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
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
    private ClientInterface clientInterface;


    @FXML
    private TableView booksTable;
    @FXML
    private TableView authorsTable;
    @FXML
    private TableColumn<BookRecord, Integer> bookIdCol;
    @FXML
    private TableColumn<BookRecord, String> bookTitleCol;
    @FXML
    private TableColumn<BookRecord, Author> bookAuthorNameCol;
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
    private RadioButton selectSearchOperation;
    @FXML
    private TextField anyAuthorTextField;

    @FXML
    private Button runOperationBtn;
    @FXML
    private Button clearAllBtn;

    private Comparator<BookRecord> bookComparator;
    private Comparator<AuthorRecord> authorComparator;

    public class BookRecord {
        private int id;
        private String title;
        private Author author;
        //private String authorName;
        private int year;
        private String publisher;
        private String brief;

        public BookRecord(int id, String title, Author author, int year, String publisher, String brief) {
            this.id = id;
            this.title = title;
            this.author = author;
            //this.authorName = authorName;
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

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        /*public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }*/

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
        try {
            Socket clientSocket = new Socket(InetAddress.getLocalHost(), 4444);
            OutputStream out = clientSocket.getOutputStream();
            InputStream in = clientSocket.getInputStream();

            JAXBContext contextCommands = JAXBContext.newInstance(CommandPacket.class, ViewBooksPacket.class, AddBookPacket.class, SetBookPacket.class, RemoveBookPacket.class, AddAuthorPacket.class, SetAuthorPacket.class, RemoveAuthorPacket.class, SearchPacket.class);
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
        selectSearchOperation.setToggleGroup(group2);

        bookRecords = FXCollections.observableArrayList();
        authorRecords = FXCollections.observableArrayList();

        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorNameCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookYearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        bookPublisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookBriefCol.setCellValueFactory(new PropertyValueFactory<>("brief"));

        authorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorBooksCountCol.setCellValueFactory(new PropertyValueFactory<>("booksCount"));

        booksTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        bookComparator = new BookComparator();
        authorComparator = new AuthorComparator();

        //bookRecords.add(new BookRecord(0, "test", "author1", 2017, "EKSMO", "aaaa?"));
        //booksTable.setItems(bookRecords);

        //=================================
    }

    @FXML
    private void handleRowSelect(MouseEvent event) {
        Node node = ((Node) event.getTarget()).getParent();
        TableRow row;
        if (node instanceof TableRow) {
            row = (TableRow) node;
        } else {
            // clicking on text part
            row = null;
            Parent parent = node.getParent();
            if (parent instanceof TableRow) {
                row = (TableRow) node.getParent();
            }
        }

        //Обработка выбранной записи
        if (row != null) {
            Object item = row.getItem();
            if (item instanceof BookRecord) {
                BookRecord record = (BookRecord) row.getItem();
                if (record != null) {
                    bookIdInp.setText(Integer.toString(record.id));
                    bookTitleInp.setText(record.title);
                    bookBriefInp.setText(record.brief);
                    bookPublisherInp.setText(record.publisher);
                    bookYearInp.setText(Integer.toString(record.year));
                    bookAuthorInp.getSelectionModel().select(record.author);
                }
            }

            if (item instanceof AuthorRecord) {
                AuthorRecord record = (AuthorRecord) row.getItem();
                if (record != null) {
                    authorIdInp.setText(Integer.toString(record.id));
                    authorNameInp.setText(record.name);
                }
            }
        }
    }

    /**
     * Updates the authors list
     *
     * @throws JAXBException
     * @throws XMLStreamException
     */
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
            if (selectSearchOperation.isSelected()) {
                currentCommand = Commands.SEARCH;
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
            if (selectBook.isSelected()) {
                book = getBookInfo();
            }
            if (selectAuthor.isSelected()) {
                author = getAuthorInfo();
            }
        }

        BookRecord bookRecord;
        AuthorRecord authorRecord;

        System.out.println(currentCommand);

        switch (currentCommand) {
            case ADD_BOOK: {
                if (book != null) {
                    //bookRecord = new BookRecord(book.getId(), book.getTitle(), book.getAuthor().getName(), book.getPublishYear(), book.getPublisher(), book.getBrief());
                    clientInterface.addBook(book, book.getAuthor());
                    clearBookForm();
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
                    clearAuthorForm();
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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "При удалении автора, удалятся также все его книги. Продолжить?", ButtonType.YES, ButtonType.NO);
                    final int tmp_id = id;

                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType.getButtonData() == ButtonBar.ButtonData.YES) {
                            try {
                                clientInterface.deleteAuthor(tmp_id);
                            } catch (JAXBException ex) {
                            } catch (XMLStreamException ex) {
                            }
                        }

                        if (buttonType.getButtonData() == ButtonBar.ButtonData.NO) {
                            System.out.println("REMOVE AUTHOR has been cancelled.");
                        }
                    });

                    //clientInterface.deleteAuthor(id);
                    /*int i;
                    int recordsCount = authorRecords.size();
                    for (i = 0; i < recordsCount && authorRecords.get(i).getId() != id; i++) ;
                    authorRecords.remove(i);*/
                }
            }
            break;

            case SEARCH: {
                String authorName = anyAuthorTextField.getText();
                String title = bookTitleInp.getText();
                String publishYear = bookYearInp.getText();
                String brief = bookBriefInp.getText();
                String publisher = bookPublisherInp.getText();

                try {
                    bookRecords.clear();
                    authorRecords.clear();

                    AuthorsContainer authorsContainer = clientInterface.searchBook(title, authorName, publishYear, brief, publisher);
                    if (authorsContainer != null) {
                        List<Author> authors1 = authorsContainer.getAuthors();
                        for (Author author1 : authors1) {
                            authorRecords.add(new AuthorRecord(author1.getId(), author1.getName(), author1.getBooks().size()));
                            List<Book> books1 = author1.getBooks();
                            for (Book book1 : books1) {
                                bookRecords.add(new BookRecord(book1.getId(), book1.getTitle(), author1, book1.getPublishYear(), book1.getPublisher(), book1.getBrief()));
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
            break;
        }
        if (currentCommand != Commands.SEARCH) {
            runViewBooks(event);
            runViewAuthors(event);
        }
        //authorsTable.setItems(authorRecords);
        //booksTable.setItems(bookRecords);
    }

    /**
     * Creates a new BOOK by the parameters received from the form
     *
     * @return book
     */
    private Book getBookInfo() {
        Book book = null;
        try {
            Author bookAuthor = bookAuthorInp.getValue();
            if (bookAuthor == null) {
                throw new NoAuthorException();
            }
            String title = bookTitleInp.getText();
            int year = Integer.parseInt(bookYearInp.getText());
            String publisher = bookPublisherInp.getText();
            String brief = bookBriefInp.getText();

            if (title.isEmpty() || publisher.isEmpty() || brief.isEmpty()) {
                throw new EmptyFieldException();
            }

            if (title.length() <= BOOK_TITLE_CONSTRAINT &&
                    publisher.length() <= BOOK_PUBLISHER_CONSTRAINT &&
                    (year >= BOOK_YEAR_MIN && year <= BOOK_YEAR_MAX) &&
                    brief.length() <= BOOK_BRIEF_CONSTRAINT) {
                book = new Book(title, bookAuthor, year, publisher, brief);
            } else {
                String errorMsg = "";
                if (title.length() > BOOK_TITLE_CONSTRAINT)
                    errorMsg += "* Название книги превышает допустимое число символов.\n";
                /*if (year < BOOK_YEAR_MIN || year > BOOK_YEAR_MAX)
                    errorMsg += "* Год издания должен быть в диапазоне от " + BOOK_YEAR_MIN + " до " + BOOK_YEAR_MAX + "\n";
                */
                if (publisher.length() > BOOK_PUBLISHER_CONSTRAINT)
                    errorMsg += "* Название издателя превышает допустимое число символов.\n";
                if (brief.length() > BOOK_BRIEF_CONSTRAINT)
                    errorMsg += "* Длина краткого описания превышает " + BOOK_BRIEF_CONSTRAINT + " символов.\n";
                new Alert(Alert.AlertType.ERROR, errorMsg).show();
            }
        } catch (YearOutOfBoundsException ex) {
            new Alert(Alert.AlertType.ERROR, "Год издания должен быть в диапазоне от " + BOOK_YEAR_MIN + " до " + BOOK_YEAR_MAX).show();
        } catch (NoAuthorException ex) {
            new Alert(Alert.AlertType.ERROR, "Невозможно добавить/изменить книгу без автора.").show();
        } catch (EmptyFieldException ex) {
            new Alert(Alert.AlertType.ERROR, "Недопустимы пустые поля.").show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Книга не добавлена/изменена из-за некорректного ввода или если одно из полей не заполнено.").show();
        }
        return book;
    }

    /**
     * Creates new AUTHOR by the parameters received from the form
     *
     * @return
     */
    private Author getAuthorInfo() {
        Author author = null;
        try {
            String authorName = authorNameInp.getText();
            if (!authorName.isEmpty() && authorName.length() <= AUTHOR_NAME_CONSTRAINT)
                author = new Author(authorName);
            else
                new Alert(Alert.AlertType.ERROR, "* Author name length exceeds the allowed number of characters OR empty.").show();
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
                        bookRecords.add(new BookRecord(book.getId(), book.getTitle(), author, book.getPublishYear(), book.getPublisher(), book.getBrief()));
                    }
                }

                System.out.println("Список книг получен.");
            } else {
                System.out.println("Список книг НЕ получен.");
            }

            bookRecords.sort(bookComparator);
            authorRecords.sort(authorComparator);

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
        selectSearchOperation.setVisible(true);
        if ((selectAddOperation.isSelected() || selectEditOperation.isSelected()) && selectBook.isSelected())
            enableBookMainInfo();
        runOperationBtn.setDisable(false);
        clearAllBtn.setDisable(false);
    }

    //Применится потом: в случае возникновения ошибки чтения с сервера (из файла), форма блокируется
    private void disableModificationForm() {
        selectBook.setDisable(true);
        selectAuthor.setDisable(true);
        selectAddOperation.setDisable(true);
        selectEditOperation.setDisable(true);
        selectDelOperation.setDisable(true);
        selectSearchOperation.setVisible(false);
        //bookIdInp.setDisable(true);
        //authorIdInp.setDisable(true);
        if ((selectAddOperation.isSelected() || selectEditOperation.isSelected()) && selectBook.isSelected())
            disableBookMainInfo();
        runOperationBtn.setDisable(true);
        clearAllBtn.setDisable(true);
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

        selectSearchOperation.setVisible(true);

        //authorIdInp.setDisable(true);
        authorNameInp.setDisable(true);

        if (selectDelOperation.isSelected() || selectEditOperation.isSelected()) {
            //bookIdInp.setDisable(false);
            anyAuthorTextField.setVisible(false);
        } else {
            //bookIdInp.setDisable(true);
        }

        if (!selectDelOperation.isSelected()) {
            enableBookMainInfo();
            anyAuthorTextField.setVisible(false);
        } else {
            disableBookMainInfo();
        }

        if (selectSearchOperation.isSelected()) {
            anyAuthorTextField.setVisible(true);
        } else anyAuthorTextField.setVisible(false);

        System.out.println("Operand type Book is selected.");
    }

    public void selectAuthor(ActionEvent event) {

        if (selectEditOperation.isSelected() || selectDelOperation.isSelected()) {
            //authorIdInp.setDisable(false);
        } else {
            //authorIdInp.setDisable(true);
        }

        if (!selectDelOperation.isSelected())
            authorNameInp.setDisable(false);
        else
            authorNameInp.setDisable(true);

        //Отключение формы с книгой
        //bookIdInp.setDisable(true);
        disableBookMainInfo();
        selectSearchOperation.setVisible(false);
        anyAuthorTextField.setVisible(false);


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
        //bookIdInp.setDisable(true);
        //authorIdInp.setDisable(true);
        authorNameInp.setDisable(true);
    }

    public void selectAddOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            //bookIdInp.setDisable(true);
            enableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            //authorIdInp.setDisable(true);
            authorNameInp.setDisable(false);
        }

        System.out.println("Operation Add is selected.");
    }

    public void selectEditOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            //bookIdInp.setDisable(false);
            enableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            //authorIdInp.setDisable(false);
            authorNameInp.setDisable(false);
        }

        System.out.println("Operation Edit is selected.");
    }

    public void selectDelOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            //bookIdInp.setDisable(false);
            disableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            //authorIdInp.setDisable(false);
            authorNameInp.setDisable(true);
        }

        System.out.println("Operation Delete is selected.");
    }

    //todo select in controller
    public void selectSearchOperation(ActionEvent event) {
        if (selectBook.isSelected()) {
            //bookIdInp.setDisable(true);
            enableBookMainInfo();
        } else if (selectAuthor.isSelected()) {
            //authorIdInp.setDisable(false);
            authorNameInp.setDisable(true);
        }
        anyAuthorTextField.setVisible(true);


        System.out.println("Operation Search is selected.");
    }

    //Очистка всех полей
    public void clearAll(ActionEvent event) {
        clearBookForm();
        clearAuthorForm();
    }

    private void clearBookForm() {
        bookIdInp.clear();
        bookTitleInp.clear();
        bookPublisherInp.clear();
        bookYearInp.clear();
        bookBriefInp.clear();
        bookAuthorInp.getSelectionModel().selectFirst();
    }

    private void clearAuthorForm() {
        authorIdInp.clear();
        authorNameInp.clear();
    }
}
