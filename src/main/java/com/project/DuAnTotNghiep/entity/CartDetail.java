package com.project.DuAnTotNghiep.entity;//package com.project.DuAnTotNghiep.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@Entity
//@Table(name = "CartDetail")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class CartDetail implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private int quantity;
//    private LocalDateTime createDate;
//    private LocalDateTime updateDate;
//    private Double price;
//    @ManyToOne
//    @JoinColumn(name = "productDetailId")
//    private ProductDetail productDetail;
//
//    @ManyToOne
//    @JoinColumn(name = "cartId")
//    private Cart cart;
//}