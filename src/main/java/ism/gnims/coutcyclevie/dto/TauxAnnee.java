package ism.gnims.coutcyclevie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TauxAnnee {
    private int annee;
    private double taux;
}
