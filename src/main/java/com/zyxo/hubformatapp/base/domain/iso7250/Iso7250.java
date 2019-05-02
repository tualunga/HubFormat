package com.zyxo.hubformatapp.base.domain.iso7250;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class Iso7250 {

    private Head head;
    private Torso torso;
    private Arm Arm;
    private Leg leg;
}
