package ism.gnims.coutcyclevie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int annees;
    private int taux;
    private double chargeOps;
    private double entretienReg;
    private List<EntretienMaj> EntretienMaj;
    /*
        private double valeurOffre;
        private double valeurResiduel;
     */
}
