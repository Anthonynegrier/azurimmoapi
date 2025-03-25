package bts.sio.api.controller;

import bts.sio.api.model.Contrat;
import bts.sio.api.service.ContratService;
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
@RequestMapping("/contrats")
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final ContratService contratService;

    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les contrats", description = "Retourne la liste de tous les contrats de location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class)))
    })
    public ResponseEntity<Iterable<Contrat>> getAllContrats() {
        return ResponseEntity.ok(contratService.getContrats());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un contrat par son ID", description = "Retourne les détails d'un contrat spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrat trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class))),
            @ApiResponse(responseCode = "404", description = "Contrat non trouvé")
    })
    public ResponseEntity<Contrat> getContrat(
            @Parameter(description = "ID du contrat", required = true)
            @PathVariable Integer id) {
        Optional<Contrat> contrat = contratService.getContratById(id);
        return contrat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouveau contrat", description = "Crée et enregistre un nouveau contrat de location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrat ajouté avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class)))
    })
    public ResponseEntity<Contrat> addContrat(
            @Parameter(description = "Détails du contrat à ajouter", required = true)
            @RequestBody Contrat contrat) {
        Contrat savedContrat = contratService.saveContrat(contrat);
        return ResponseEntity.ok(savedContrat);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un contrat", description = "Met à jour les informations d'un contrat existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrat mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class))),
            @ApiResponse(responseCode = "404", description = "Contrat non trouvé")
    })
    public ResponseEntity<Contrat> updateContrat(
            @Parameter(description = "ID du contrat à mettre à jour", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Nouvelles informations du contrat", required = true)
            @RequestBody Contrat contrat) {
        Optional<Contrat> existingContratOpt = contratService.getContratById(id);

        if (existingContratOpt.isPresent()) {
            Contrat currentContrat = existingContratOpt.get();

            // Update fields only if provided
            if (contrat.getDateDebut() != null) {
                currentContrat.setDateDebut(contrat.getDateDebut());
            }
            if (contrat.getDateFin() != null) {
                currentContrat.setDateFin(contrat.getDateFin());
            }
            if (contrat.getMontantLoyer() != null) {
                currentContrat.setMontantLoyer(contrat.getMontantLoyer());
            }
            if (contrat.getLocataire() != null) {
                currentContrat.setLocataire(contrat.getLocataire());
            }
            if (contrat.getAppartement() != null) {
                currentContrat.setAppartement(contrat.getAppartement());
            }

            Contrat updatedContrat = contratService.saveContrat(currentContrat);
            return ResponseEntity.ok(updatedContrat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat", description = "Supprime un contrat par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat non trouvé")
    })
    public ResponseEntity<Void> deleteContrat(
            @Parameter(description = "ID du contrat à supprimer", required = true)
            @PathVariable Integer id) {
        if (contratService.getContratById(id).isPresent()) {
            contratService.deleteContrat(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/locataire/{locataireId}")
    @Operation(summary = "Récupérer les contrats d'un locataire", description = "Retourne tous les contrats associés à un locataire spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrats du locataire récupérés avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class)))
    })
    public ResponseEntity<List<Contrat>> getContratsByLocataireId(
            @Parameter(description = "ID du locataire", required = true)
            @PathVariable Long locataireId) {
        List<Contrat> contrats = contratService.getContratsByLocataireId(locataireId);
        return ResponseEntity.ok(contrats);
    }

    @GetMapping("/appartement/{appartementId}")
    @Operation(summary = "Récupérer les contrats d'un appartement", description = "Retourne tous les contrats associés à un appartement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrats de l'appartement récupérés avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class)))
    })
    public ResponseEntity<List<Contrat>> getContratsByAppartementId(
            @Parameter(description = "ID de l'appartement", required = true)
            @PathVariable Long appartementId) {
        List<Contrat> contrats = contratService.getContratsByAppartementId(appartementId);
        return ResponseEntity.ok(contrats);
    }

    @GetMapping("/locataire/{prenom}/{nom}")
    @Operation(summary = "Récupérer les contrats par nom et prénom du locataire", description = "Retourne tous les contrats d'un locataire identifié par son prénom et son nom")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrats du locataire récupérés avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contrat.class)))
    })
    public ResponseEntity<List<Contrat>> getContratsByLocatairePrenomAndNom(
            @Parameter(description = "Prénom du locataire", required = true)
            @PathVariable String prenom,
            @Parameter(description = "Nom du locataire", required = true)
            @PathVariable String nom) {
        List<Contrat> contrats = contratService.getContratsByLocatairePrenomAndNom(prenom, nom);
        return ResponseEntity.ok(contrats);
    }
}