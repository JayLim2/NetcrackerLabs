package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Cart.CartKey.class)
@Table(name = "\"cart\"", schema = "public", catalog = "postgres")
public class Cart implements Serializable {

    /*@EmbeddedId
    private CartKey key;*/

    @Id
    @ManyToOne
    @JoinColumn(name = "\"userID\"", referencedColumnName = "\"user_id")
    private int userID;

    @Id
    @ManyToOne
    @JoinColumn(name = "\"bookID\"")
    private int bookID;

    @Column(name = "\"count\"")
    private int count;

    public Cart() {
    }

    public Cart(int userID, int bookID) {
        this.userID = userID;
        this.bookID = bookID;
    }

    public Cart(int userID, int bookID, int count) {
        this.userID = userID;
        this.bookID = bookID;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class CartKey implements Serializable {
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
    }
}
