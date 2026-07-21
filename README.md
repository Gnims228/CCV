# Calcul du Coût de Cycle de Vie (CCV)

Cet outil permet d'établir le calcul du **coût de cycle de vie** (CCV) d'un actif — informatique ou non — sur la base :

- du nombre d'années d'utilisation,
- du nombre d'années de maintenance et des coûts associés,
- du coût du capital,
- et du **facteur d'actualisation**, dont la formule est détaillée ci-dessous.

Le coût de cycle de vie permet de comparer plusieurs offres d'équipement sur toute leur durée de vie utile, et pas seulement sur leur prix d'achat initial — en tenant compte des coûts d'exploitation, de maintenance, et de la valeur résiduelle, tous ramenés à leur valeur actualisée.

## Paramètres

| Paramètre | Description |
|---|---|
| **Offre** | Prix de l'offre du soumissionnaire de l'équipement |
| **Ops** | Coût annuel d'exploitation |
| **Main. Rég** | Coût annuel de maintenance régulière |
| **Main. Maj** | Coût de la maintenance ponctuelle (majeure), rattaché à une année précise |
| **Val. Rés** | Coût de la valeur résiduelle de l'équipement en fin de cycle de vie |
| **Fact. Actual** | Valeur du facteur d'actualisation appliqué pour une année donnée |
| **Taux d'actualisation** | Taux annuel utilisé pour actualiser les coûts futurs (ex. : `0,05` pour 5 %) |

## Le facteur d'actualisation

Le facteur d'actualisation ramène un coût futur à sa valeur équivalente aujourd'hui : plus une dépense est lointaine dans le temps, moins elle pèse dans le calcul, car l'argent a un coût (inflation, coût d'opportunité du capital).

Pour une année *n* et un taux d'actualisation *t* (exprimé en décimal, ex. `0,05`) :

```
F(n) = 1 / (1 + t)^n
```

Chaque facteur est arrondi à 6 décimales.

**Exemple** avec un taux de 5 % sur 3 ans :

| Année (n) | Calcul | Facteur F(n) |
|---|---|---|
| 1 | 1 / (1,05)¹ | 0,952381 |
| 2 | 1 / (1,05)² | 0,907029 |
| 3 | 1 / (1,05)³ | 0,863838 |

## Calcul du coût de cycle de vie (CCV)

Le CCV total agrège, pour chaque année du cycle de vie, les coûts actualisés :

```
CCV = Offre
    + Σ (Ops × F(n))                    pour n = 1 à N
    + Σ (Main. Rég × F(n))              pour n = 1 à N
    + Σ (Main. Maj(n) × F(n))           pour chaque année n où une maintenance majeure a lieu
    − (Val. Rés × F(N))
```

où :
- **N** est le nombre total d'années du cycle de vie,
- **Main. Maj(n)** n'est comptabilisée que sur les années où une maintenance ponctuelle est effectivement planifiée (les autres années valent 0),
- la **valeur résiduelle** est actualisée avec le facteur de la **dernière** année du cycle (F(N)), puisqu'elle est perçue en fin de vie de l'équipement.

## Structure du projet

- `ActualisationParAnnee(nbreAnnees, taux)` — génère la liste des facteurs d'actualisation, année par année.
- `CCV(offre, valeurResiduel, valeurOps, valeurMainReg, entretienMajs, tauxAnnee)` — calcule le coût de cycle de vie complet et retourne le détail (coûts d'exploitation, de maintenance régulière, de maintenance majeure, valeur résiduelle actualisée, et le CCV final).

## Prérequis

- Java 21+
- Maven

## Lancer le projet

```bash
mvn spring-boot:run
```

## Lancer les tests

```bash
mvn test
```

Les tests unitaires couvrent le calcul des facteurs d'actualisation (arrondi, cas limites) et le calcul du CCV (cas nominal, absence de maintenance majeure, taux non nul).

## Intégration continue

Un workflow GitHub Actions (`.github/workflows/ci.yml`) exécute automatiquement la suite de tests à chaque `push` et `pull request` sur la branche `main`.