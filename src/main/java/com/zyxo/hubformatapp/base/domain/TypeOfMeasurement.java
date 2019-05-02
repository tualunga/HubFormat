package com.zyxo.hubformatapp.base.domain;

import com.zyxo.hubformatapp.base.domain.iso7250.Iso7250;
import com.zyxo.hubformatapp.base.domain.tailoring.Tailoring;
import lombok.*;

import javax.persistence.*;

//lombok
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
public class TypeOfMeasurement {

    private Iso7250 iso_7250;

    private Tailoring tailoring;


}
