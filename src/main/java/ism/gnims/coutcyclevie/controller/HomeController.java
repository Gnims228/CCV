package ism.gnims.coutcyclevie.controller;

import ism.gnims.coutcyclevie.dto.Request;
import ism.gnims.coutcyclevie.dto.Response;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import ism.gnims.coutcyclevie.service.HomeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class HomeController {


    @GetMapping("")
    public String HomePage(){
        return "home";
    }

}

