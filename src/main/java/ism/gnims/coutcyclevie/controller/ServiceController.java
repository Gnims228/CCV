package ism.gnims.coutcyclevie.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ism.gnims.coutcyclevie.dto.Request;
import ism.gnims.coutcyclevie.dto.Response;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import ism.gnims.coutcyclevie.service.HomeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/ccv")
@Tag(name = "Gestion des services",
        description = "Endpoints pour calculer le cout de cycle de vie d'un equipement informatique a partir de divers informations")
public class ServiceController {

    private final HomeService service;

    public ServiceController(HomeService service) {
        this.service = service;
    }

    @Operation(summary = "Calcul du Cout de Cycle de Vie", description = "Retourne une liste d'informations sur les couts annuels selon les taux d'actualisation")
    @PostMapping("")
    public ResponseEntity<?> CCV(@RequestBody @Valid Request request){


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
                        request.getEntretienMajs(),
                        data
        );

        return ResponseEntity.status(HttpStatus.OK).body(data1);
    }

}

