package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.Order.OrderRequestDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.repository.OrderRepository;
import project.finalproject1backend.repository.ProductRepository;
import project.finalproject1backend.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static project.finalproject1backend.domain.OrderStatus.PURCHASING;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    //바로 구매(개별 주문)
    public ResponseEntity<ResponseDTO> purchaseOne(PrincipalDTO principal, OrderRequestDTO orderRequestDTO){

        String userId = principal.getUserId();
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<Product> optionalProduct = productRepository.findById(orderRequestDTO.getProductId());

        if(optionalProduct.isPresent()){
            int price = optionalProduct.get().getConsumerPrice() * orderRequestDTO.getCount();

            if(optionalUser.isPresent()){
                createOrder(principal, orderRequestDTO, price);
            }else{
                throw new IllegalArgumentException("User ID");
            }
        }else{
            throw new IllegalArgumentException("product ID");
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }



    private void createOrder(PrincipalDTO principal, OrderRequestDTO orderRequestDTO, int price) { // 오더 생성
        String userId = principal.getUserId();
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        String paymentMethod = orderRequestDTO.getPaymentMethod();
        String deliveryName = orderRequestDTO.getDeliveryName();
        String deliveryPhoneNumber = orderRequestDTO.getDeliveryPhoneNumber();
        String deliveryMemo = orderRequestDTO.getDeliveryMemo();
        String deliveryZipcode = orderRequestDTO.getDeliveryZipcode();
        String deliveryAddress = orderRequestDTO.getDeliveryAddress();
        String deliveryDetailedAddress = orderRequestDTO.getDeliveryDetailedAddress();

        Orders order = new Orders();
        orderRepository.save(order); // 아래의 getId 사용을 위해 한번 저장
        order.setUser(optionalUser.get());
        order.setTotalPrice(price);
        order.setPaymentMethod(paymentMethod);
        order.setNumber(createNumber(optionalUser.get().getId(), order.getId()));
        order.setDeliveryName(deliveryName);
        order.setDeliveryPhoneNumber(deliveryPhoneNumber);
        order.setDeliveryMemo(deliveryMemo);
        order.setDeliveryZipCode(deliveryZipcode);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryDetailedAddress(deliveryDetailedAddress);
        order.setStatus(PURCHASING);
        orderRepository.save(order);
    }


    public static long createNumber(long userId, long orderId){ //주문 번호 생성

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

        String orderNumString = now.format(formatter) + String.format("%02d",userId) + String.format("%02d",orderId);

        return Long.parseLong(orderNumString);
    }
}
