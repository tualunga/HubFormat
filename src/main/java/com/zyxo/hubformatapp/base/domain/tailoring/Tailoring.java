package com.zyxo.hubformatapp.base.domain.tailoring;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
public class Tailoring {

    private Torso torso;
    private Arm arm;
    private Leg leg;
}
