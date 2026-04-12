    package com.tihuz.ecommerce_backend.service;

    import com.tihuz.ecommerce_backend.dto.request.OrderRequest;
    import com.tihuz.ecommerce_backend.dto.response.OrderResponse;
    import com.tihuz.ecommerce_backend.entity.*;
    import com.tihuz.ecommerce_backend.enums.OrderStatus;
    import com.tihuz.ecommerce_backend.exception.AppException;
    import com.tihuz.ecommerce_backend.exception.ErrorCode;
    import com.tihuz.ecommerce_backend.mapper.OrderMapper;
    import com.tihuz.ecommerce_backend.repository.CartRepository;
    import com.tihuz.ecommerce_backend.repository.OrderRepository;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.math.BigDecimal;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    @Transactional
    @FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
    public class OrderService
    {
        CartRepository cartRepository;
        OrderRepository orderRepository;
        OrderMapper orderMapper;

        public OrderResponse checkout(String userId,OrderRequest request)
        {
            Cart cart= cartRepository.findByUserId(userId)
                    .orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));

            // cart empty
            if( cart.getItems().isEmpty())
            {
                throw  new AppException(ErrorCode.CART_EMPTY);
            }

          Order order= createOrder(userId,request);

            createOrderItem(cart,order);
            calculateCart(order);

            // clear cart
            cart.getItems().clear();
            cartRepository.save(cart);

            Order saved = orderRepository.save(order);
            return orderMapper.toResponse(saved);
        }


        public Order createOrder(String userId, OrderRequest request)
        {
            Order order=orderMapper.toOrder(request);
            order.setUserId(userId);
            order.setStatus(OrderStatus.PENDING);
            return order;
        }


        public void createOrderItem(Cart cart,Order order) {

            for (CartItem cartItem : cart.getItems())
            {
                // lấy ra sản phẩm cụ thể trong item trong giỏ
                Product product = cartItem.getProduct();
                if (product.getQuantity() < cartItem.getQuantity())
                {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }

                //price
                BigDecimal price = product.getPriceSale() != null
                        ? product.getPriceSale()
                        : product.getPrice();

                // create orderDetail
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(product.getId());
                orderItem.setProductName(product.getName());
                orderItem.setPrice(price);
                orderItem.setQuantity(cartItem.getQuantity());

                // thêm cái chi tiết order vào Item trong order
                order.getItems().add(orderItem);


                product.setQuantity(
                        product.getQuantity() - cartItem.getQuantity()
                );
            }
        }


        public void calculateCart(Order order)
        {
            BigDecimal total = BigDecimal.ZERO;

            // chạy từng sản phẩm trong chi tiết order
            for (OrderItem item : order.getItems())
            {
                // set subTotal
                BigDecimal subTotal = item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                item.setSubTotal(subTotal);

                // set total = tổng các subtotal
                total = total.add(subTotal);
            }
            // gán total cho order
            order.setTotalPrice(total);

    //        BigDecimal total1=response.getItems()
    //                .stream()
    //                .map(CartItemResponse::getSubTotal)
    //                .reduce(BigDecimal.ZERO,BigDecimal::add);

        }


        public List<OrderResponse> getOrdersForUser(String userId)
        {
            List<Order> orders = orderRepository.findByUserId(userId);
            return orders.stream()
                    .map(orderMapper::toResponse)
                    .toList();

        }


    }
