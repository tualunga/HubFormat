package com.zyxo.hubformatapp.base.domain;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
public class Hbdf {

    private static final long serialVersionUID = 1L;

    String TypeOfDevice;

    PersonalInformation PersonalInformation;

    TypeOfMeasurement TypeOfMeasurement;

}
