package ism.gnims.coutcyclevie.controller;

import ism.gnims.coutcyclevie.dto.Request;
import ism.gnims.coutcyclevie.dto.Response;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import ism.gnims.coutcyclevie.service.HomeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/ccv")
public class HomeController {

    private final HomeService service;

    public HomeController(HomeService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<?> Home(@RequestBody @Valid Request request){

        //Liste des taux d'actualisation et de l'annee correspondante
        List<TauxActualisation> data = service.ActualisationParAnnee(
                request.getAnnees(),
                request.getTaux()
        );

        //Calcul du cout du cycle de vie
        Response data1 = service.CCV(
                        request.getValeurOffre(),
                        request.getValeurResiduel(),
                        request.getChargeOps(),
                        request.getEntretienReg(),
                        request.getEntretienMaj(),
                        data
        );

        return ResponseEntity.status(HttpStatus.OK).body(data1);
    }

}

