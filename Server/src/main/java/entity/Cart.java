package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@IdClass(Cart.CartKey.class)
@Table(name = "\"cart\"", schema = "public", catalog = "postgres")
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_cart")
    @SequenceGenerator(name = "auto_increment_cart", sequenceName = "\"auto_increment_cart\"", allocationSize = 1)
    @Column(name = "\"cartID\"")
    private int cartID;

    @ManyToOne
    @JoinColumn(name = "\"userid\"", referencedColumnName = "\"user_id\"")
    private User user;

    @ManyToOne
    @JoinColumn(name = "\"bookid\"")
    private Book book;

    @Column(name = "\"count\"")
    private int count;

    public Cart() {
    }

    public Cart(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public Cart(User user, Book book, int count) {
        this.user = user;
        this.book = book;
        this.count = count;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /*public static class CartKey implements Serializable {
        static final long serialVersionUID = 1L;

        private int userID;
        private int bookID;

        public CartKey() {
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public int getBookID() {
            return bookID;
        }

        public void setBookID(int bookID) {
            this.bookID = bookID;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj instanceof CartKey) {
                CartKey key = (CartKey) obj;
                return bookID == key.bookID && userID == key.userID;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }*/
}
