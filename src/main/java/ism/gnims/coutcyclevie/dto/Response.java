package ism.gnims.coutcyclevie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int offre;
    private int years;
    private List<Double> percentage;
    private List<Integer> OperationsCost;
    private List<Integer> LowMaintenances;
    private List<Integer> HighMaintenances;
    private int residualCost;
    private int ccv;
}
