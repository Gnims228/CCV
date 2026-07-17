package ism.gnims.coutcyclevie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MEntretien {
    private int annee;
    private double valeurEntretien;
}
