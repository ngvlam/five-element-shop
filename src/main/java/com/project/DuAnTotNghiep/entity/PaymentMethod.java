package com.project.DuAnTotNghiep.entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PaymentMethod")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    @Enumerated(EnumType.STRING)
    private PaymentMethodName name;
    private int status;
}