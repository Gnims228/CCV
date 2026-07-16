package ism.gnims.coutcyclevie;

import ism.gnims.coutcyclevie.dto.EntretienMaj;
import ism.gnims.coutcyclevie.dto.Request;
import ism.gnims.coutcyclevie.dto.TauxAnnee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/home")
public class HomeController {

    @PostMapping("")
    public ResponseEntity<?> Home(@RequestBody @Valid Request request){

        //Liste des taux d'actualisation et de l'annee correspondante
        List<TauxAnnee> data = this.TauxAnnee(request.getAnnees(), request.getTaux());
        Map<?,?> data1 = this
                .coutCycleDeVie(
                        request.getChargeOps(),
                        request.getEntretienReg(),
                        request.getEntretienMaj(),
                        data);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data1);
    }

    private List<TauxAnnee> TauxAnnee(int nbreAnnees, int taux){

        //Conversion du taux d'actualisation
        double tauxx = (double) taux /100;

        //Creation du tableau regroupant les annees et leur taux d'actualisation
        List<TauxAnnee> data = new ArrayList<>();

        for (int i = 1; i < nbreAnnees + 1; i++) {

            double t = (1/(Math.pow(1+tauxx, i)));
            //Arrondissement de la valeur double
            BigDecimal bd = new BigDecimal(t);
            bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
            data.add(new TauxAnnee(i,bd.doubleValue()));
        }

        return data;
    }

    private Map<?,?> coutCycleDeVie(
            double valeurOps,
            double valeurMainReg,
            List<EntretienMaj> entretienMajs,
            List<TauxAnnee> tauxAnnee){

        double sommeOps = 0;
        double sommeMainReg = 0;
        double sommeMainMaj = 0;

        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double ops = valeurOps * (tauxAnnee.get(i-1).getTaux());
            sommeOps = sommeOps + ops;
        }

        for (int i = 1; i < tauxAnnee.size() + 1; i++) {
            double mainReg = valeurMainReg * (tauxAnnee.get(i-1).getTaux());
            sommeMainReg = sommeMainReg + mainReg;
        }

        for (EntretienMaj em : entretienMajs) {
            sommeMainMaj = sommeMainMaj + (em.getValeurEntretien() * tauxAnnee.get(em.getAnnee()-1).getTaux());
        }

        Map<String, Integer> somme = new HashMap<>();
        somme.put("ops", (int) Math.round(sommeOps));
        somme.put("mainReg", (int) Math.round(sommeMainReg));
        somme.put("mainMaj", (int) Math.round(sommeMainMaj));

        return somme;
    }

}

