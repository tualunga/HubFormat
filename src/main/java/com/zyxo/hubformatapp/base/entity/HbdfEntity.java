package com.zyxo.hubformatapp.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "hbdf_table")
@AllArgsConstructor
@NoArgsConstructor
public class HbdfEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "height")
    private BigDecimal height;

    @Column(name = "date")
    private Date date;

    @Column(name = "xml")
    @JsonIgnore
    private String xml;

    @Column(name="type_of_device")
    private String typeOfDevice;

    @Column(name = "iso_7250")
    private Boolean iso_7250;

    @Column(name = "tailoring")
    private Boolean tailoring;
}
