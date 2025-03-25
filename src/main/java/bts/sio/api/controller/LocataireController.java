package bts.sio.api.controller;

import bts.sio.api.model.Locataire;
import bts.sio.api.service.LocataireService;
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
@RequestMapping("/locataires")
@Tag(name = "Locataires", description = "Gestion des locataires")
public class LocataireController {

    private final LocataireService locataireService;

    public LocataireController(LocataireService locataireService) {
        this.locataireService = locataireService;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les locataires", description = "Retourne la liste de tous les locataires")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des locataires récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Locataire.class)))
    })
    public ResponseEntity<Iterable<Locataire>> getAllLocataires() {
        return ResponseEntity.ok(locataireService.getLocataires());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un locataire par son ID", description = "Retourne les détails d'un locataire spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locataire trouvé",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Locataire.class))),
            @ApiResponse(responseCode = "404", description = "Locataire non trouvé")
    })
    public ResponseEntity<Locataire> getLocataire(
            @Parameter(description = "ID du locataire", required = true)
            @PathVariable Integer id) {
        Optional<Locataire> locataire = locataireService.getLocataireById(id);
        return locataire.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouveau locataire", description = "Crée et enregistre un nouveau locataire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locataire ajouté avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Locataire.class)))
    })
    public ResponseEntity<Locataire> addLocataire(
            @Parameter(description = "Détails du locataire à ajouter", required = true)
            @RequestBody Locataire locataire) {
        Locataire savedLocataire = locataireService.saveLocataire(locataire);
        return ResponseEntity.ok(savedLocataire);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un locataire", description = "Met à jour les informations d'un locataire existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locataire mis à jour avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Locataire.class))),
            @ApiResponse(responseCode = "404", description = "Locataire non trouvé")
    })
    public ResponseEntity<Locataire> updateLocataire(
            @Parameter(description = "ID du locataire à mettre à jour", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Nouvelles informations du locataire", required = true)
            @RequestBody Locataire locataire) {
        Optional<Locataire> existingLocataireOpt = locataireService.getLocataireById(id);

        if (existingLocataireOpt.isPresent()) {
            Locataire currentLocataire = existingLocataireOpt.get();

            // Update fields only if provided
            if (locataire.getPrenom() != null) {
                currentLocataire.setPrenom(locataire.getPrenom());
            }
            if (locataire.getNom() != null) {
                currentLocataire.setNom(locataire.getNom());
            }
            if (locataire.getDateNaissance() != null) {
                currentLocataire.setDateNaissance(locataire.getDateNaissance());
            }

            Locataire updatedLocataire = locataireService.saveLocataire(currentLocataire);
            return ResponseEntity.ok(updatedLocataire);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un locataire", description = "Supprime un locataire par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Locataire supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Locataire non trouvé")
    })
    public ResponseEntity<Void> deleteLocataire(
            @Parameter(description = "ID du locataire à supprimer", required = true)
            @PathVariable Integer id) {
        if (locataireService.getLocataireById(id).isPresent()) {
            locataireService.deleteLocataire(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}