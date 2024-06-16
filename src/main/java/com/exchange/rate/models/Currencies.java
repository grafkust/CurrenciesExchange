package com.exchange.rate.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Currencies")
public class Currencies  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @Column(name = "Code")
    @Getter @Setter
    private String code;

    @Column(name = "Name")
    @Getter @Setter
    private String fullName;

    @Column(name = "Sign")
    @Getter @Setter
    private String sign;


}
