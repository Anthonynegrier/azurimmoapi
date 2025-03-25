package bts.sio.api.repository;

import bts.sio.api.model.Contrat;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

    List<Contrat> findByLocataireId(Long locataireId);
    List<Contrat> findByAppartementId(Long appartementId);
    List<Contrat> findByLocatairePrenomAndLocataireNom(String prenom, String nom);
}