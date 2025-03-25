// First, add these dependencies to your pom.xml
/*
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
*/

package bts.sio.api.controller;

import bts.sio.api.model.Paiement;
import bts.sio.api.service.PaiementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paiements")
@Tag(name = "Paiements", description = "API pour la gestion des paiements")
public class PaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les paiements", description = "Retourne la liste de tous les paiements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paiement.class)))
    })
    public ResponseEntity<Iterable<Paiement>> getAllPaiements() {
        return ResponseEntity.ok(paiementService.getPaiements());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un paiement par son ID", description = "Retourne un paiement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paiement.class))),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    public ResponseEntity<Paiement> getPaiement(
            @Parameter(description = "ID du paiement à récupérer", required = true)
            @PathVariable Integer id) {
        Optional<Paiement> paiement = paiementService.getPaiementById(id);
        return paiement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau paiement", description = "Ajoute un nouveau paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paiement.class)))
    })
    public ResponseEntity<Paiement> addPaiement(
            @Parameter(description = "Détails du paiement à créer", required = true)
            @RequestBody Paiement paiement) {
        Paiement savedPaiement = paiementService.savePaiement(paiement);
        return ResponseEntity.ok(savedPaiement);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un paiement existant", description = "Met à jour les détails d'un paiement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paiement.class))),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    public ResponseEntity<Paiement> updatePaiement(
            @Parameter(description = "ID du paiement à mettre à jour", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Nouveaux détails du paiement", required = true)
            @RequestBody Paiement paiement) {
        Optional<Paiement> existingPaiementOpt = paiementService.getPaiementById(id);

        if (existingPaiementOpt.isPresent()) {
            Paiement currentPaiement = existingPaiementOpt.get();

            // Update fields only if provided
            if (paiement.getDatePaiement() != null) {
                currentPaiement.setDatePaiement(paiement.getDatePaiement());
            }
            if (paiement.getMontant() != null) {
                currentPaiement.setMontant(paiement.getMontant());
            }
            if (paiement.getContrat() != null) {
                currentPaiement.setContrat(paiement.getContrat());
            }

            Paiement updatedPaiement = paiementService.savePaiement(currentPaiement);
            return ResponseEntity.ok(updatedPaiement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement", description = "Supprime un paiement par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paiement supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    public ResponseEntity<Void> deletePaiement(
            @Parameter(description = "ID du paiement à supprimer", required = true)
            @PathVariable Integer id) {
        if (paiementService.getPaiementById(id).isPresent()) {
            paiementService.deletePaiement(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contrat/{contratId}")
    @Operation(summary = "Récupérer les paiements par ID de contrat", description = "Retourne tous les paiements associés à un contrat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiements du contrat récupérés",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Paiement.class))),
            @ApiResponse(responseCode = "204", description = "Aucun paiement trouvé pour ce contrat")
    })
    public ResponseEntity<List<Paiement>> getPaiementsByContratId(
            @Parameter(description = "ID du contrat", required = true)
            @PathVariable Integer contratId) {
        List<Paiement> paiements = paiementService.getPaiementsByContratId(contratId);
        return paiements.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(paiements);
    }
}