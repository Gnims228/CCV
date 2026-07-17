package ism.gnims.coutcyclevie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TauxActualisation {
    private int annee;
    private double taux;
}
