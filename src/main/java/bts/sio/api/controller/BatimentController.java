package bts.sio.api.controller;

import bts.sio.api.model.Batiment;
import bts.sio.api.service.BatimentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/batiments")
@Tag(name = "Bâtiments", description = "Gestion des bâtiments")
public class BatimentController {

    private final BatimentService batimentService;

    public BatimentController(BatimentService batimentService) {
        this.batimentService = batimentService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les bâtiments", description = "Retourne la liste de tous les bâtiments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des bâtiments récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Batiment.class)))
    })
    public ResponseEntity<Iterable<Batiment>> getAllBatiments() {
        return ResponseEntity.ok(batimentService.getBatiments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un bâtiment par son ID", description = "Retourne les détails d'un bâtiment spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bâtiment trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Batiment.class))),
            @ApiResponse(responseCode = "404", description = "Bâtiment non trouvé")
    })
    public ResponseEntity<Batiment> getBatiment(
            @Parameter(description = "ID du bâtiment", required = true)
            @PathVariable Long id) {
        return batimentService.getBatimentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouveau bâtiment", description = "Crée et enregistre un nouveau bâtiment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bâtiment ajouté avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Batiment.class)))
    })
    public ResponseEntity<Batiment> addBatiment(
            @Parameter(description = "Détails du bâtiment à ajouter", required = true)
            @RequestBody Batiment batiment) {
        Batiment savedBatiment = batimentService.saveBatiment(batiment);
        return ResponseEntity.ok(savedBatiment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un bâtiment", description = "Met à jour les informations d'un bâtiment existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bâtiment mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Batiment.class))),
            @ApiResponse(responseCode = "404", description = "Bâtiment non trouvé")
    })
    public ResponseEntity<Batiment> updateBatiment(
            @Parameter(description = "ID du bâtiment à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du bâtiment", required = true)
            @RequestBody Batiment batiment) {
        Optional<Batiment> existingBatimentOpt = batimentService.getBatimentById(id);

        if (existingBatimentOpt.isPresent()) {
            Batiment currentBatiment = existingBatimentOpt.get();

            if (batiment.getAdresse() != null) {
                currentBatiment.setAdresse(batiment.getAdresse());
            }
            if (batiment.getVille() != null) {
                currentBatiment.setVille(batiment.getVille());
            }

            Batiment updatedBatiment = batimentService.saveBatiment(currentBatiment);
            return ResponseEntity.ok(updatedBatiment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bâtiment", description = "Supprime un bâtiment par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bâtiment supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Bâtiment non trouvé")
    })
    public ResponseEntity<Void> deleteBatiment(
            @Parameter(description = "ID du bâtiment à supprimer", required = true)
            @PathVariable Long id) {
        if (batimentService.getBatimentById(id).isPresent()) {
            batimentService.deleteBatiment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}