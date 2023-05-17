package project.finalproject1backend.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.finalproject1backend.domain.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}