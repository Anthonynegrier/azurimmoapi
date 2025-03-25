package bts.sio.api.service;

import bts.sio.api.model.Contrat;
import bts.sio.api.repository.ContratRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class ContratService {

    @Autowired
    private ContratRepository contratRepository;

    public Iterable<Contrat> getContrats() {
        return contratRepository.findAll();
    }

    public Optional<Contrat> getContratById(final Integer id) {
        return contratRepository.findById(id);
    }

    public void deleteContrat(final Integer id) {
        contratRepository.deleteById(id);
    }

    public Contrat saveContrat(Contrat contrat) {
        Contrat saved = contratRepository.save(contrat);
        return saved;
    }

    public List<Contrat> getContratsByLocataireId(Long locataireId) {
        return contratRepository.findByLocataireId(locataireId);
    }

    public List<Contrat> getContratsByAppartementId(Long appartementId) {
        return contratRepository.findByAppartementId(appartementId);
    }

    public List<Contrat> getContratsByLocatairePrenomAndNom(String prenom, String nom) {
        return contratRepository.findByLocatairePrenomAndLocataireNom(prenom, nom);
    }
}