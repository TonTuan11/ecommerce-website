package com.tihuz.indentity_service.service;

import com.tihuz.indentity_service.dto.request.CartCreationRequest;
import com.tihuz.indentity_service.dto.request.CartUpdateRequest;

import com.tihuz.indentity_service.dto.response.CartItemResponse;
import com.tihuz.indentity_service.dto.response.CartResponse;
import com.tihuz.indentity_service.entity.Cart;
import com.tihuz.indentity_service.entity.CartItem;
import com.tihuz.indentity_service.entity.Product;
import com.tihuz.indentity_service.enums.ProductStatus;
import com.tihuz.indentity_service.exception.AppException;
import com.tihuz.indentity_service.exception.ErrorCode;
import com.tihuz.indentity_service.mapper.CartMapper;
import com.tihuz.indentity_service.repository.CartRepository;
import com.tihuz.indentity_service.repository.ProductRepository;
import com.tihuz.indentity_service.repository.UserRepository;

import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartMapper cartMapper;

    // lấy ra user hiện tại
    private String getCurrentUserId() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public CartResponse addToCart(CartCreationRequest request)
//    public void addToCart(Long productId,int quantity)
    {
        // lấy ra id của user
        String userId=getCurrentUserId();

        //-check sản phẩm
        // + tồn tại không
        // + status phải ACTIVE không
        // + đủ stock không
        Product product=productRepository.findById(request.getProductId())
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOTEXISTED));

        if(product.getStatus()!= ProductStatus.ACTIVE)
        {
            throw  new AppException(ErrorCode.PRODUCT_NOTEXISTED);
        }

        if(product.getQuantity()<request.getQuantity())
        {
            throw new AppException(ErrorCode.OUT_OF_STOCK);
        }


        // tìm cart của user, chưa có thì tạo
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).orElseThrow());
                    return cartRepository.save(newCart);
                });


        // kiểm tra sản phẩm có trong cart chưa
        Optional<CartItem> existingItem=cart.getItems()             // lấy ra danh sách
                .stream()                       // chuyển list sang stream
                .filter(item->item.getProduct().getId().equals(request.getProductId()))   // dùng để lọc ra CartItem nào có cùng productid với sản phẩm đang thêm
                .findFirst();         //nếu có trả về có giá trị, không có thì empty



        // nếu sản phẩm đã có trong giỏ hàng
        if(existingItem.isPresent())
        {
            CartItem item=existingItem.get();          // lấy ra
            int newQuantity=item.getQuantity() + request.getQuantity();    // tăng thêm số lượng

            if( product.getQuantity()<newQuantity)
            {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            item.setQuantity(newQuantity); //gán lại giá trị mới

        }
        else
        {
            // nếu chưa có trong giỏ thì thêm vào
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());

             cart.getItems().add(item);   // thêm item vào giỏ

        }
        // lưu db
        cartRepository.save(cart);

// map sang response
        CartResponse response = cartMapper.toCartResponse(cart);
        // tính sub và total
        calculateCart(response);

        return response;



//        cartRepository.save(cart);
//        return cartMapper.toCartResponse(cart);

    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<CartResponse> getAll()
    {
        return cartRepository.findAll()
                .stream()
                .map(cart -> {
                    CartResponse response=cartMapper.toCartResponse(cart);
                    calculateCart(response);
                    return  response;
                })
                .toList();

    }


    public CartResponse getForUser()
    {
        String userId = getCurrentUserId();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).orElseThrow());
                    return cartRepository.save(newCart);
                });

        CartResponse response = cartMapper.toCartResponse(cart);

        calculateCart(response);

        return response;

    }



    public CartResponse updateItem(CartUpdateRequest request)
    {

        // kiểm sa số lượng đầu vào
        if( request.getQuantity()<=0)
        {
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }

        // id user login
        String userId=getCurrentUserId();

        // tìm cart của user
        Cart cart=cartRepository.findByUserId(userId)
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND));


        //lấy ra cartItem trong cart ( )
        CartItem item=cart.getItems()
                .stream()
                .filter(i->i.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(()->new AppException(ErrorCode.ITEM_NOT_FOUND));


        // lấy ra sản phẩm từ cartItem
        Product product=item.getProduct();

        // kiểm tra tồn kho
        if(product.getQuantity()<request.getQuantity())
        {
            throw new AppException(ErrorCode.OUT_OF_STOCK);
        }

        // đặt lại số lượng cho cartItem
        item.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        // map sang CartResponse và tính tiền
        CartResponse response=cartMapper.toCartResponse(cart);
        calculateCart(response);
        return response;

    }




    public  CartResponse deleteItem( Long productId)
    {
        String userId=getCurrentUserId();

        Cart cart=cartRepository.findByUserId(userId)
                .orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));

        cart.getItems().removeIf(
                item->item.getProduct().getId().equals(productId)
        );

        cartRepository.save(cart);
        CartResponse response=cartMapper.toCartResponse(cart);
        calculateCart(response);
        return response;
    }



    public void clearCartItem()
    {
        String userId= getCurrentUserId();

        Cart cart= cartRepository.findByUserId(userId)
                .orElseThrow(()->new AppException(ErrorCode.CART_NOT_FOUND));

        cart.getItems().clear();
        cartRepository.save(cart);
    }


    public void calculateCart(CartResponse response)
    {
        response.getItems().forEach(item->
        {

            BigDecimal price =item.getPriceSale()!=null
                    ?item.getPriceSale()
                    :item.getPrice();

            item.setSubTotal(
                    price.multiply(BigDecimal.valueOf(item.getQuantity()))
            );


        });



        BigDecimal total=BigDecimal.ZERO;
        for( CartItemResponse item: response.getItems())
        {
            total=total.add(item.getSubTotal());
        }

        response.setTotalPrice(total);

//        BigDecimal total1=response.getItems()
//                .stream()
//                .map(CartItemResponse::getSubTotal)
//                .reduce(BigDecimal.ZERO,BigDecimal::add);

    }

}
