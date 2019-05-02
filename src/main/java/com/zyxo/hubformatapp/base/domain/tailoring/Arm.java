package com.zyxo.hubformatapp.base.domain.tailoring;

import com.zyxo.hubformatapp.base.domain.Extent;
import lombok.Data;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.ArrayList;

@Data
public class Arm {

    ArrayList<Extent> extent;
}
