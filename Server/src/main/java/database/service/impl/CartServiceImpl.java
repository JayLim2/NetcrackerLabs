package database.service.impl;

import database.repository.CartRepository;
import database.service.BookService;
import database.service.CartService;
import database.service.UserService;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @Override
    public List<Cart> getCartByUserId(int userId) {
        return cartRepository.getCartByUserId(userService.findUserById(userId));
    }

    @Override
    public void addToCart(int userId, int bookId) {
        Cart cart = cartRepository.getCartByKey(userService.findUserById(userId), bookService.getByID(bookId));

        if (cart != null) {
            cart.setCount(cart.getCount() + 1);
        } else {
            cart = new Cart(userService.findUserById(userId), bookService.getByID(bookId), 1);
        }

        cartRepository.saveAndFlush(cart);
    }

    @Override
    public void deleteFromCart(int cartId) {
        cartRepository.deleteCartById(cartId);
    }
}
