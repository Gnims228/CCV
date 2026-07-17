package ism.gnims.coutcyclevie.service;

import ism.gnims.coutcyclevie.dto.MEntretien;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<?,?> CCV(double offre, double valeurResiduel, double valeurOps, double valeurMainReg, List<MEntretien> entretienMajs, List<TauxActualisation> tauxAnnee){

        double sommeOps = 0;
        double sommeMainReg = 0;
        double sommeMainMaj = 0;

        //Calcul de la valeur residuel selon le taux de la derniere annee
        valeurResiduel = valeurResiduel*(tauxAnnee.getLast().getTaux());

        //Calcul de la somme des charges operationnelles
        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double ops = valeurOps * (tauxAnnee.get(i-1).getTaux());
            sommeOps = sommeOps + ops;
        }

        //Calcul de la somme des entretiens reguliers
        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double mainReg = valeurMainReg * (tauxAnnee.get(i-1).getTaux());
            sommeMainReg = sommeMainReg + mainReg;
        }

        //Calcul de la somme des entretiens majeurs
        for (MEntretien em : entretienMajs) {
            sommeMainMaj = sommeMainMaj + (em.getValeurEntretien() * tauxAnnee.get(em.getAnnee()-1).getTaux());
        }

        double ccv = offre+sommeOps+sommeMainReg+sommeMainMaj-valeurResiduel;

        Map<String, Integer> somme = new HashMap<>();
        somme.put("offre", (int) Math.round(offre));
        somme.put("ops", (int) Math.round(sommeOps));
        somme.put("mainReg", (int) Math.round(sommeMainReg));
        somme.put("mainMaj", (int) Math.round(sommeMainMaj));
        somme.put("valResid", (int) Math.round(valeurResiduel));
        somme.put("ccv", (int) (ccv));

        return somme;
    }

}
