package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.domain.CartItem;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.Order.OrderCartRequestDTO;
import project.finalproject1backend.dto.Order.OrderRequestDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.repository.OrderRepository;
import project.finalproject1backend.repository.ProductRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.repository.cart.CartItemRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static project.finalproject1backend.domain.OrderStatus.PURCHASING;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    //바로 구매(개별 주문)
    public ResponseEntity<ResponseDTO> purchaseOne(PrincipalDTO principal, OrderRequestDTO orderRequestDTO){

        String userId = principal.getUserId();
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<Product> optionalProduct = productRepository.findById(orderRequestDTO.getProductId());
        int price;

        if(optionalProduct.isPresent()){ // 상품 확인
        price = optionalProduct.get().getConsumerPrice() * orderRequestDTO.getCount();}
        else{
       throw new IllegalArgumentException("User ID");
       }

        if(price <= 0){throw new IllegalArgumentException("Price is Equal or Under 0");} // 결제 금액이 0원 이하인 경우

       if(optionalUser.isPresent()){ // 유저 확인
       }else{
           throw new IllegalArgumentException("product ID");
       }

           optionalProduct.get().removeStock(orderRequestDTO.getCount()); // 재고 차감 (유효성 검사 겸용)
           createOrder(principal, orderRequestDTO, price); // 주문 생성


        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    //장바구니 구매
    public ResponseEntity<ResponseDTO> purchaseInCart(PrincipalDTO principal, OrderCartRequestDTO orderCartRequestDTO){

        Optional<User> optionalUser = userRepository.findByUserId(principal.getUserId());
        List<Long> requsetItems = orderCartRequestDTO.getCartItems();
        List<CartItem> cartItems = new ArrayList<>();
        int price = 0;

        if(optionalUser.isPresent()){ // 유저 확인
        }else{
            throw new IllegalArgumentException("User ID");
        }

        for (int i = 0; i < requsetItems.size(); i++) { // 가독성을 위해 ID로 받은 리스트를 CartItem 리스트로 전환
            cartItems.add(cartItemRepository.findById(requsetItems.get(i)).get());

            if(productRepository.findById(cartItems.get(i).getProduct().getId()).isEmpty()){ // 상품 확인
                throw new IllegalArgumentException("Product ID");
            }
            if(cartItems.get(i).getCount() > cartItems.get(i).getProduct().getStockNumber()){// 장바구니의 주문량이 재고보다 많은 경우
                throw new IllegalArgumentException("Not Enough Stock");
            }
        }


        for (int i = 0; i < cartItems.size(); i++) { // 최종 결제 금액 계산
            price += cartItems.get(i).getProduct().getConsumerPrice() * cartItems.get(i).getCount();
        }

        if(price <= 0){throw new IllegalArgumentException("Price is Equal or Under 0");} // 결제 금액이 0원 이하인 경우

        for (int i = 0; i < cartItems.size(); i++) { // 재고 차감
            cartItems.get(i).getProduct().removeStock(cartItems.get(i).getCount());
        }

        createCartOrder(principal,orderCartRequestDTO,price); // 오더 생성

        for (int i = 0; i < cartItems.size(); i++) { // 구매한 장바구니 아이템 삭제
            cartItemRepository.deleteById(cartItems.get(i).getId());
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
    private void createCartOrder(PrincipalDTO principal, OrderCartRequestDTO orderCartRequestDTO, int price) { // 장바구니 상품에 대한 오더 생성 ( 리팩토링 필요 )
        String userId = principal.getUserId();
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        String paymentMethod = orderCartRequestDTO.getPaymentMethod();
        String deliveryName = orderCartRequestDTO.getDeliveryName();
        String deliveryPhoneNumber = orderCartRequestDTO.getDeliveryPhoneNumber();
        String deliveryMemo = orderCartRequestDTO.getDeliveryMemo();
        String deliveryZipcode = orderCartRequestDTO.getDeliveryZipcode();
        String deliveryAddress = orderCartRequestDTO.getDeliveryAddress();
        String deliveryDetailedAddress = orderCartRequestDTO.getDeliveryDetailedAddress();


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
