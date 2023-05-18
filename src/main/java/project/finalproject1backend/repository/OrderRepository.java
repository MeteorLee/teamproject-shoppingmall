package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.finalproject1backend.domain.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}