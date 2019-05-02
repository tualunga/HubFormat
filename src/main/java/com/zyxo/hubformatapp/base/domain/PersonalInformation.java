package com.zyxo.hubformatapp.base.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

//lombok
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class PersonalInformation {

    private String name;

    private BigDecimal weight;

    private BigDecimal height;

    private int age;

    private Date date;
}
