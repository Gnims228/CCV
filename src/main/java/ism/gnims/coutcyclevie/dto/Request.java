package ism.gnims.coutcyclevie.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Min(value = 2, message = "Le nombre d'annees est positif, obligatoire et doit etre >= 2 !")
    @Positive(message = "Le taux doit etre positif !")
    private int annees;
    @Min(value = 1, message = "Le taux doit etre compris entre 1 et 100 !")
    @Max(value = 100, message = "Le taux doit etre positif et compris entre 1 et 100 !")
    @Positive
    private int taux;
    @Min(value = 1, message = "La valeur doit etre positive et au moins >= 1")
    @Positive
    private double chargeOps;
    @Min(value = 1, message = "La valeur doit etre positive et au moins >= 1")
    @Positive
    private double entretienReg;
    private List<MEntretien> EntretienMaj;
    @Min(value = 1, message = "La valeur doit etre positive et au moins >= 1")
    @Positive
    private double valeurResiduel;
    @Min(value = 1, message = "La valeur doit etre positive et au moins >= 1")
    @Positive
    private double valeurOffre;
}
