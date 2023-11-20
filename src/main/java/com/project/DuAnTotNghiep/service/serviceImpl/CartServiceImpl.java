package com.project.DuAnTotNghiep.service.serviceImpl;

import com.project.DuAnTotNghiep.dto.CartDto;
import com.project.DuAnTotNghiep.dto.OrderDetailDto;
import com.project.DuAnTotNghiep.dto.OrderDto;
import com.project.DuAnTotNghiep.dto.Product.ProductDetailDto;
import com.project.DuAnTotNghiep.dto.ProductCart;
import com.project.DuAnTotNghiep.entity.*;
import com.project.DuAnTotNghiep.entity.enumClass.BillStatus;
import com.project.DuAnTotNghiep.entity.enumClass.InvoiceType;
import com.project.DuAnTotNghiep.exception.NotFoundException;
import com.project.DuAnTotNghiep.exception.ShopApiException;
import com.project.DuAnTotNghiep.repository.*;
import com.project.DuAnTotNghiep.service.CartService;
import com.project.DuAnTotNghiep.utils.UserLoginUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final BillRepository billRepository;
    private final DiscountCodeRepository discountCodeRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private AtomicLong invoiceCounter = new AtomicLong(1);

    public CartServiceImpl(CartRepository cartRepository, CustomerRepository customerRepository, AccountRepository accountRepository, ProductRepository productRepository, ProductDetailRepository productDetailRepository, BillRepository billRepository, DiscountCodeRepository discountCodeRepository, PaymentMethodRepository paymentMethodRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.billRepository = billRepository;
        this.discountCodeRepository = discountCodeRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<CartDto> getAllCart() {
        List<Cart> carts = cartRepository.findAll();
        List<CartDto> cartDtos = new ArrayList<>();
        carts.forEach(cart -> {
            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setCreateDate(cart.getCreateDate());
        });
        return cartDtos;
    }

    @Override
    public List<CartDto> getAllCartByAccountId() {
        Account account = UserLoginUtil.getCurrentLogin();
        List<Cart> cartList = cartRepository.findAllByAccount_Id(account.getId());
        List<CartDto> cartDtos = new ArrayList<>();
        cartList.forEach(cart -> {
            Product product = productRepository.findById(cart.getProductDetail().getProduct().getId()).orElseThrow();
            ProductCart productCart = new ProductCart();
            productCart.setProductId(product.getId());
            productCart.setName(product.getName());
            productCart.setCode(product.getCode());
            productCart.setDescribe(product.getDescribe());
            productCart.setImageUrl(product.getImage().get(0).getLink());
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setId(cart.getProductDetail().getId());
            productDetailDto.setProductId(product.getId());
            productDetailDto.setPrice(cart.getProductDetail().getPrice());
            productDetailDto.setSize(cart.getProductDetail().getSize());
            productDetailDto.setQuantity(cart.getProductDetail().getQuantity());
            productDetailDto.setColor(cart.getProductDetail().getColor());

            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setQuantity(cart.getQuantity());
            cartDto.setCreateDate(cart.getCreateDate());
            cartDto.setAccountId(Long.parseLong("2"));
            cartDto.setProduct(productCart);
            cartDto.setDetail(productDetailDto);
            cartDtos.add(cartDto);
        });
        return cartDtos;
    }

    @Override
    public void addToCart(CartDto cartDto) throws NotFoundException {
        Cart cart = new Cart();
        Account account = UserLoginUtil.getCurrentLogin();
        cart.setAccount(account);

        ProductDetail productDetail = productDetailRepository.findById(cartDto.getDetail().getId()).orElseThrow(() -> new NotFoundException("Product not found") );

        cart.setProductDetail(productDetail);
        int quantityAdding = cartDto.getQuantity();
        int quantityRemaining = productDetail.getQuantity();

        if (cartRepository.existsByProductDetail_IdAndAccount_Id(productDetail.getId(), account.getId())) {
            Cart existsCart = cartRepository.findByProductDetail_IdAndAccount_Id(productDetail.getId(), account.getId());
            int currentQuantity = existsCart.getQuantity();
            int quantityNeedToAdd = currentQuantity + quantityAdding;

            existsCart.setQuantity(quantityNeedToAdd);
            existsCart.setUpdateDate(LocalDateTime.now());
            if(quantityRemaining < quantityNeedToAdd) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số lượng thêm vào giỏ hàng lớn hơn số lượng tồn");
            }
            cartRepository.save(existsCart);
        }else {
            if(quantityRemaining < quantityAdding) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số lượng thêm vào giỏ hàng lớn hơn số lượng tồn");
            }

            cart.setQuantity(quantityAdding);
            cart.setCreateDate(LocalDateTime.now());
            cart.setUpdateDate(LocalDateTime.now());
            cartRepository.save(cart);
        }

    }

    @Override
    public void updateCart(CartDto cartDto) throws NotFoundException {
        Cart cart = cartRepository.findById(cartDto.getId()).orElseThrow( () -> new NotFoundException("Cart not found") );
        int quantityAdding = cartDto.getQuantity();
        int quantityRemaining = cart.getProductDetail().getQuantity();
        if(quantityAdding > quantityRemaining) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Số lượng trong giỏ hàng phải nhỏ hơn số lượng còn: " + quantityRemaining);
        }
        cart.setQuantity(cartDto.getQuantity());
        cartRepository.save(cart);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void orderUser(OrderDto orderDto) {
        Bill billCurrent = billRepository.findTopByOrderByIdDesc();
        int nextCode = (billCurrent == null) ? 1 : Integer.parseInt(billCurrent.getCode().substring(2)) + 1;
        String billCode = "HD" + String.format("%03d", nextCode);

        Bill bill = new Bill();
        bill.setBillingAddress(orderDto.getBillingAddress());
        bill.setCreateDate(LocalDateTime.now());
        bill.setUpdateDate(LocalDateTime.now());
        bill.setCode(billCode);
        bill.setInvoiceType(InvoiceType.ONLINE);
        bill.setStatus(BillStatus.DANG_XU_LY);
        bill.setPromotionPrice(orderDto.getPromotionPrice());
        if (UserLoginUtil.getCurrentLogin() != null) {
            Account account = UserLoginUtil.getCurrentLogin();
            bill.setCustomer(account.getCustomer());
        }
        Double total = Double.valueOf(0);
        List<BillDetail> billDetailList = new ArrayList<>();
        for (OrderDetailDto item:
             orderDto.getOrderDetailDtos()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setQuantity(item.getQuantity());
            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetailId()).orElseThrow(() -> new NotFoundException("Product not found"));
            billDetail.setProductDetail(productDetail);
            if(productDetail.getQuantity() - item.getQuantity() < 0) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Sản phẩm " + productDetail.getProduct().getName() + "-" + productDetail.getSize().getName() +  "-" + productDetail.getColor().getName()  + " chỉ còn lại " + productDetail.getQuantity());
            }
            productDetail.setQuantity(productDetail.getQuantity() - item.getQuantity());
            productDetailRepository.save(productDetail);
            billDetailList.add(billDetail);
            total+=productDetail.getPrice() * item.getQuantity();
        }

        DiscountCode discountCode = discountCodeRepository.findById(orderDto.getVoucherId()).orElseThrow(() -> new ShopApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy voucher"));
        Integer currentQuaCode = discountCode.getMaximumUsage();
        discountCode.setMaximumUsage(currentQuaCode - 1);
        discountCodeRepository.save(discountCode);


        bill.setAmount(total);
        bill.setBillDetail(billDetailList);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId()).orElseThrow(() -> new NotFoundException("Payment not found"));
        bill.setPaymentMethod(paymentMethod);
        billRepository.save(bill);
        cartRepository.deleteAllByAccount_Id(UserLoginUtil.getCurrentLogin().getId());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrderDto orderAdmin(OrderDto orderDto) {
        Bill billCurrent = billRepository.findTopByOrderByIdDesc();
        int nextCode = (billCurrent == null) ? 1 : Integer.parseInt(billCurrent.getCode().substring(2)) + 1;
        String billCode = "HD" + String.format("%03d", nextCode);

        Bill bill = new Bill();
        bill.setBillingAddress(orderDto.getBillingAddress());
        bill.setCreateDate(LocalDateTime.now());
        bill.setUpdateDate(LocalDateTime.now());
        bill.setCode(billCode);
        bill.setInvoiceType(InvoiceType.OFFLINE);
        bill.setStatus(BillStatus.HOAN_THANH);
        bill.setPromotionPrice(orderDto.getPromotionPrice());
        Customer customer = null;
        if(orderDto.getCustomer().getId() != null) {
             customer = customerRepository.findById(orderDto.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        }
        bill.setCustomer(customer);
        Double total = Double.valueOf(0);
        List<BillDetail> billDetailList = new ArrayList<>();
        for (OrderDetailDto item:
                orderDto.getOrderDetailDtos()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setQuantity(item.getQuantity());
            ProductDetail productDetail = productDetailRepository.findById(item.getProductDetailId()).orElseThrow(() -> new NotFoundException("Product not found"));
            billDetail.setProductDetail(productDetail);
            if(productDetail.getQuantity() - item.getQuantity() < -1) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Sản phẩm " + productDetail.getProduct().getName() + "-" + productDetail.getSize().getName() +  "-" + productDetail.getColor().getName()  + " chỉ còn lại " + productDetail.getQuantity());
            }
            int beforeQuantity = productDetail.getQuantity();
            productDetail.setQuantity(beforeQuantity - item.getQuantity());
            productDetailRepository.save(productDetail);
            billDetailList.add(billDetail);
            total+=productDetail.getPrice() * item.getQuantity();
        }

        DiscountCode discountCode = discountCodeRepository.findById(orderDto.getVoucherId()).orElseThrow(() -> new ShopApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy voucher"));
        Integer currentQuaCode = discountCode.getMaximumUsage();
        discountCode.setMaximumUsage(currentQuaCode - 1);
        discountCodeRepository.save(discountCode);

        bill.setAmount(total);
        bill.setBillDetail(billDetailList);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto.getPaymentMethodId()).orElseThrow(() -> new NotFoundException("Payment not found"));
        bill.setPaymentMethod(paymentMethod);
        Bill billNew = billRepository.save(bill);
        return new OrderDto(billNew.getId().toString(), orderDto.getCustomer(), billNew.getInvoiceType(), billNew.getStatus(), billNew.getPaymentMethod().getId(), billNew.getBillingAddress(), billNew.getPromotionPrice(), null, null);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }


//    @Autowired
//    private CartRepository cartRepository;
//    @Override
//    public Page<Cart> carts(Pageable pageable) {
//        return cartRepository.findAll(pageable);
//    }
}
