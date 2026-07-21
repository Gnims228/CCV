package ism.gnims.coutcyclevie.service;

import ism.gnims.coutcyclevie.dto.MEntretien;
import ism.gnims.coutcyclevie.dto.Response;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeService {

    public List<TauxActualisation> ActualisationParAnnee(int nbreAnnees, int taux){

        //Conversion du taux d'actualisation
        double tauxx = (double) taux /100;

        //Creation du tableau regroupant les annees et leur taux d'actualisation
        List<TauxActualisation> data = new ArrayList<>();

        for (int i = 1; i < nbreAnnees + 1; i++) {

            double t = (1/(Math.pow(1+tauxx, i)));
            //Arrondissement de la valeur double
            BigDecimal bd = new BigDecimal(t);
            bd = bd.setScale(6, RoundingMode.HALF_UP);
            data.add(new TauxActualisation(i,bd.doubleValue()));
        }

        return data;
    }

    public Response CCV(double offre, double valeurResiduel, double valeurOps, double valeurMainReg, List<MEntretien> entretienMajs, List<TauxActualisation> tauxAnnee){

        double sommeOps = 0;
        double sommeMainReg = 0;
        double sommeMainMaj = 0;

        List<Double> percentage = new ArrayList<>();
        List<Integer> OperationsCost = new ArrayList<>();
        List<Integer> LowMaintenances = new ArrayList<>();
        List<Integer> HighMaintenances = new ArrayList<>();

        for (TauxActualisation ta: tauxAnnee){
            percentage.add(ta.getTaux());
        }

        //Calcul de la valeur residuel selon le taux de la derniere annee
        valeurResiduel = valeurResiduel*(tauxAnnee.getLast().getTaux());

        //Calcul de la somme des charges operationnelles
        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double ops = valeurOps * (tauxAnnee.get(i-1).getTaux());
            OperationsCost.add((int) ops);
            sommeOps = sommeOps + ops;
        }

        //Calcul de la somme des entretiens reguliers
        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double mainReg = valeurMainReg * (tauxAnnee.get(i-1).getTaux());
            LowMaintenances.add((int) mainReg);
            sommeMainReg = sommeMainReg + mainReg;
        }

        Map<Integer, MEntretien> entretienParAnnee = entretienMajs.stream()
                .collect(Collectors.toMap(MEntretien::getAnnee, em -> em));

        for (int i = 1; i < tauxAnnee.size()+1; i++) {

            MEntretien em = entretienParAnnee.get(i);

            if (em!=null){

                double a = em.getValeurEntretien() * (tauxAnnee.get(i-1).getTaux());
                HighMaintenances.add( (int) a);

            }else{

                HighMaintenances.add(0);

            }
        }

        //Calcul de la somme des entretiens majeurs
        for (MEntretien em : entretienMajs) {
            double a = em.getValeurEntretien() * tauxAnnee.get(em.getAnnee()-1).getTaux();
            sommeMainMaj = sommeMainMaj + a;
        }

        double ccv = offre+sommeOps+sommeMainReg+sommeMainMaj-valeurResiduel;

        Response response = new Response();
        response.setOffre((int) offre);
        response.setYears(tauxAnnee.size());
        response.setPercentage(percentage);
        response.setOperationsCost(OperationsCost);
        response.setLowMaintenances(LowMaintenances);
        response.setHighMaintenances(HighMaintenances);
        response.setResidualCost((int) valeurResiduel);
        response.setCcv((int) ccv);

        return response;
    }

}
