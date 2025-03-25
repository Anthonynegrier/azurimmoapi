package bts.sio.api.service;

import bts.sio.api.model.Locataire;
import bts.sio.api.repository.LocataireRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class LocataireService {

    @Autowired
    private LocataireRepository locataireRepository;

    public Iterable<Locataire> getLocataires() {
        return locataireRepository.findAll();
    }

    public Optional<Locataire> getLocataireById(final Integer id) {
        return locataireRepository.findById(id);
    }

    public void deleteLocataire(final Integer id) {
        locataireRepository.deleteById(id);
    }

    public Locataire saveLocataire(Locataire locataire) {
        Locataire saved = locataireRepository.save(locataire);
        return saved;
    }

    public List<Locataire> getLocatairesByNom(String nom) {
        return locataireRepository.findByNom(nom);
    }

    public List<Locataire> getLocatairesByPrenom(String prenom) {
        return locataireRepository.findByPrenom(prenom);
    }

    public List<Locataire> getLocatairesByDateNaissance(LocalDate dateNaissance) {
        return locataireRepository.findByDateNaissance(dateNaissance);
    }
}