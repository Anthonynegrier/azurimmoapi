package bts.sio.api.controller;

import bts.sio.api.model.Reparation;
import bts.sio.api.service.ReparationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reparations")
@Tag(name = "reparations", description = "Gestion des réparations")
public class ReparationController {

    @Autowired
    private ReparationService reparationService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les réparations", description = "Retourne la liste de toutes les réparations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des réparations récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reparation.class)))
    })
    public ResponseEntity<Iterable<Reparation>> getAllReparations() {
        return ResponseEntity.ok(reparationService.getReparations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réparation par son ID", description = "Retourne les détails d'une réparation spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réparation trouvée",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reparation.class))),
            @ApiResponse(responseCode = "404", description = "Réparation non trouvée")
    })
    public ResponseEntity<Reparation> getReparation(
            @Parameter(description = "ID de la réparation", required = true)
            @PathVariable Long id) {
        Optional<Reparation> reparation = reparationService.getReparationById(id);
        return reparation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle réparation", description = "Crée et enregistre une nouvelle réparation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Réparation ajoutée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reparation.class)))
    })
    public ResponseEntity<Reparation> addReparation(
            @Parameter(description = "Détails de la réparation à ajouter", required = true)
            @RequestBody Reparation reparation) {
        Reparation savedReparation = reparationService.saveReparation(reparation);
        return ResponseEntity.status(201).body(savedReparation);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réparation", description = "Supprime une réparation par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Réparation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réparation non trouvée")
    })
    public ResponseEntity<Void> deleteReparation(
            @Parameter(description = "ID de la réparation à supprimer", required = true)
            @PathVariable("id") Long id) {
        if (reparationService.getReparationById(id).isPresent()) {
            reparationService.deleteReparation(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
