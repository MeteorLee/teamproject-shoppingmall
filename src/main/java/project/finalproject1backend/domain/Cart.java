package project.finalproject1backend.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User cartUser;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public void setUser(User user) {
        this.cartUser = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
