package bts.sio.api.controller;

import bts.sio.api.model.Appartement;
import bts.sio.api.service.AppartementService;
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
@RequestMapping("/appartements")
@Tag(name = "Appartements", description = "Gestion des appartements")
public class AppartementController {

    private final AppartementService appartementService;

    public AppartementController(AppartementService appartementService) {
        this.appartementService = appartementService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les appartements", description = "Retourne la liste de tous les appartements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des appartements récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class)))
    })
    public ResponseEntity<Iterable<Appartement>> getAllAppartements() {
        return ResponseEntity.ok(appartementService.getAppartements());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un appartement par son ID", description = "Retourne les détails d'un appartement spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appartement trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class))),
            @ApiResponse(responseCode = "404", description = "Appartement non trouvé")
    })
    public ResponseEntity<Appartement> getAppartement(
            @Parameter(description = "ID de l'appartement", required = true)
            @PathVariable Long id) {
        return appartementService.getAppartementById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouvel appartement", description = "Crée et enregistre un nouvel appartement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appartement ajouté avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class)))
    })
    public ResponseEntity<Appartement> addAppartement(
            @Parameter(description = "Détails de l'appartement à ajouter", required = true)
            @RequestBody Appartement appartement) {
        Appartement savedAppartement = appartementService.saveAppartement(appartement);
        return ResponseEntity.ok(savedAppartement);
    }

    @GetMapping("/batiment/{id}")
    @Operation(summary = "Récupérer tous les appartements d'un bâtiment", description = "Retourne la liste des appartements d'un bâtiment spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des appartements récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class)))
    })
    public ResponseEntity<List<Appartement>> getAppartementsByBatiment(
            @Parameter(description = "ID du bâtiment", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(appartementService.getAppartementsParBatiment(id));
    }

    @GetMapping("/ville/{ville}")
    @Operation(summary = "Récupérer tous les appartements d'une ville", description = "Retourne la liste des appartements situés dans une ville spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des appartements récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class)))
    })
    public ResponseEntity<List<Appartement>> getAppartementsByVille(
            @Parameter(description = "Nom de la ville", required = true)
            @PathVariable String ville) {
        return ResponseEntity.ok(appartementService.getAppartementsByVille(ville));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un appartement", description = "Met à jour les informations d'un appartement existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appartement mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Appartement.class))),
            @ApiResponse(responseCode = "404", description = "Appartement non trouvé")
    })
    public ResponseEntity<Appartement> updateAppartement(
            @Parameter(description = "ID de l'appartement à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations de l'appartement", required = true)
            @RequestBody Appartement appartement) {
        Optional<Appartement> existingAppartementOpt = appartementService.getAppartementById(id);

        if (existingAppartementOpt.isPresent()) {
            Appartement currentAppartement = existingAppartementOpt.get();

            if (appartement.getNumero() != null) {
                currentAppartement.setNumero(appartement.getNumero());
            }
            if (appartement.getSurface() != null) {
                currentAppartement.setSurface(appartement.getSurface());
            }
            if (appartement.getNbPiece() != null) {
                currentAppartement.setNbPiece(appartement.getNbPiece());
            }
            if (appartement.getDescription() != null) {
                currentAppartement.setDescription(appartement.getDescription());
            }
            if (appartement.getBatiment() != null) {
                currentAppartement.setBatiment(appartement.getBatiment());
            }

            Appartement updatedAppartement = appartementService.saveAppartement(currentAppartement);
            return ResponseEntity.ok(updatedAppartement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un appartement", description = "Supprime un appartement par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appartement supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Appartement non trouvé")
    })
    public ResponseEntity<Void> deleteAppartement(
            @Parameter(description = "ID de l'appartement à supprimer", required = true)
            @PathVariable Long id) {
        if (appartementService.getAppartementById(id).isPresent()) {
            appartementService.deleteAppartement(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}