package bts.sio.api.repository;

import bts.sio.api.model.Locataire;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocataireRepository extends JpaRepository<Locataire, Integer> {

    List<Locataire> findByNom(String nom);
    List<Locataire> findByPrenom(String prenom);
    List<Locataire> findByDateNaissance(LocalDate dateNaissance);
}