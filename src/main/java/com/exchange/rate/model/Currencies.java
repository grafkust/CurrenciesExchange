package com.exchange.rate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Currencies")
@Getter @Setter
public class Currencies  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Code")
    private String code;

    @Column(name = "Name")
    private String fullName;

    @Column(name = "Sign")
    private String sign;


}
