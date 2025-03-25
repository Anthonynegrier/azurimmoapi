package bts.sio.api.service;

import bts.sio.api.model.Appartement;
import bts.sio.api.repository.AppartementRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class AppartementService {

    @Autowired
    private AppartementRepository appartementRepository;

    public Iterable<Appartement> getAppartements() {
        return appartementRepository.findAll();
    }

    public Optional<Appartement> getAppartementById(final Long id) {
        return appartementRepository.findById(id);
    }

    public void deleteAppartement(final Long id) {
        appartementRepository.deleteById(id);
    }

    public Appartement saveAppartement(Appartement appartement) {
        Appartement saved = appartementRepository.save(appartement);
        return saved;
    }

    public List<Appartement> getAppartementsByVille(String ville) {
        return appartementRepository.findByBatimentVille(ville);
    }

    public List<Appartement> getAppartementsParBatiment(long id) {
        return appartementRepository.findByBatimentId(id);
    }
}
