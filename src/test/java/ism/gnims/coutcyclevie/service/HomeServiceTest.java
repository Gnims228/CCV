package ism.gnims.coutcyclevie.service;

import ism.gnims.coutcyclevie.dto.MEntretien;
import ism.gnims.coutcyclevie.dto.Response;
import ism.gnims.coutcyclevie.dto.TauxActualisation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class HomeServiceTest {

    private HomeService service;

    @BeforeEach
    void setUp() {
        service = new HomeService();
    }

    @Nested
    @DisplayName("ActualisationParAnnee")
    class ActualisationParAnneeTests {

        @Test
        @DisplayName("taux = 0% => tous les facteurs valent 1.0")
        void tauxZero_devraitRetournerFacteursUn() {
            List<TauxActualisation> result = service.ActualisationParAnnee(3, 0);

            assertEquals(3, result.size());
            for (TauxActualisation ta : result) {
                assertEquals(1.0, ta.getTaux(), 0.000001);
            }
        }

        @Test
        @DisplayName("nbreAnnees = 0 => liste vide")
        void nbreAnneesZero_devraitRetournerListeVide() {
            List<TauxActualisation> result = service.ActualisationParAnnee(0, 10);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Vérifie le calcul et l'arrondi à 6 décimales pour un taux de 10%")
        void taux10Pourcent_devraitCalculerEtArrondirCorrectement() {
            List<TauxActualisation> result = service.ActualisationParAnnee(2, 10);

            assertEquals(2, result.size());

            assertEquals(1, result.get(0).getAnnee());
            assertEquals(0.909091, result.get(0).getTaux(), 0.0000001);

            assertEquals(2, result.get(1).getAnnee());
            assertEquals(0.826446, result.get(1).getTaux(), 0.0000001);
        }

        @Test
        @DisplayName("Les années sont numérotées de 1 à nbreAnnees, dans l'ordre")
        void annees_devraientEtreOrdonneesEtSequentielles() {
            List<TauxActualisation> result = service.ActualisationParAnnee(5, 5);
            for (int i = 0; i < result.size(); i++) {
                assertEquals(i + 1, result.get(i).getAnnee());
            }
        }
    }

    @Nested
    @DisplayName("CCV")
    class CCVTests {

        @Test
        @DisplayName("Cas nominal avec taux = 0% (facteurs = 1.0) pour des chiffres ronds faciles à vérifier")
        void ccv_casNominalTauxZero() {
            List<TauxActualisation> tauxAnnee = service.ActualisationParAnnee(3, 0);
            // tauxAnnee = [(1,1.0), (2,1.0), (3,1.0)]

            List<MEntretien> entretienMajs = List.of(
                    new MEntretien(2, 300.0) // un entretien majeur en année 2
            );

            Response response = service.CCV(
                    1000,   // offre
                    500,    // valeurResiduel
                    100,    // valeurOps
                    50,     // valeurMainReg
                    entretienMajs,
                    tauxAnnee
            );

            assertEquals(1000, response.getOffre());
            assertEquals(3, response.getYears());
            assertEquals(List.of(1.0, 1.0, 1.0), response.getPercentage());
            assertEquals(List.of(100, 100, 100), response.getOperationsCost());
            assertEquals(List.of(50, 50, 50), response.getLowMaintenances());
            assertEquals(List.of(0, 300, 0), response.getHighMaintenances());
            assertEquals(500, response.getResidualCost());

            // ccv = offre(1000) + sommeOps(300) + sommeMainReg(150) + sommeMainMaj(300) - valeurResiduel(500)
            assertEquals(1250, response.getCcv());
        }

        @Test
        @DisplayName("Aucun entretien majeur => HighMaintenances rempli de 0")
        void ccv_sansEntretienMajeur_devraitAvoirHighMaintenancesAZero() {
            List<TauxActualisation> tauxAnnee = service.ActualisationParAnnee(3, 0);

            Response response = service.CCV(
                    1000, 500, 100, 50,
                    Collections.emptyList(),
                    tauxAnnee
            );

            assertEquals(List.of(0, 0, 0), response.getHighMaintenances());
            // ccv = 1000 + 300 + 150 + 0 - 500
            assertEquals(950, response.getCcv());
        }

        @Test
        @DisplayName("Cas avec un taux non nul (10%) - vérifie la propagation des facteurs d'actualisation")
        void ccv_avecTauxNonNul() {
            List<TauxActualisation> tauxAnnee = service.ActualisationParAnnee(2, 10);
            // tauxAnnee = [(1,0.909091), (2,0.826446)]

            List<MEntretien> entretienMajs = List.of(
                    new MEntretien(2, 500.0)
            );

            Response response = service.CCV(
                    1000, 5000, 200, 100,
                    entretienMajs,
                    tauxAnnee
            );

            assertEquals(List.of(181, 165), response.getOperationsCost());
            assertEquals(List.of(90, 82), response.getLowMaintenances());
            assertEquals(List.of(0, 413), response.getHighMaintenances());
            assertEquals(4132, response.getResidualCost());
            assertEquals(-2198, response.getCcv());
        }

        @Test
        @DisplayName("⚠️ Documente le comportement actuel : tauxAnnee vide lève une exception")
        void ccv_avecTauxAnneeVide_leveActuellementUneException() {
            // tauxAnnee.getLast() plante sur une liste vide (NoSuchElementException).
            // Ce test documente le comportement actuel ; à toi de voir si tu veux
            // ajouter une garde (if (tauxAnnee.isEmpty()) ...) dans le service.
            assertThrows(NoSuchElementException.class, () ->
                    service.CCV(1000, 500, 100, 50, Collections.emptyList(), Collections.emptyList())
            );
        }

        @Test
        @DisplayName("⚠️ Documente le comportement actuel : une année d'entretien hors plage lève une exception")
        void ccv_avecEntretienAnneeHorsPlage_leveActuellementUneException() {
            List<TauxActualisation> tauxAnnee = service.ActualisationParAnnee(2, 0);
            // Entretien déclaré en année 5 alors qu'il n'y a que 2 années dans tauxAnnee
            List<MEntretien> entretienMajs = List.of(new MEntretien(5, 300.0));

            assertThrows(IndexOutOfBoundsException.class, () ->
                    service.CCV(1000, 500, 100, 50, entretienMajs, tauxAnnee)
            );
        }

    }

}
