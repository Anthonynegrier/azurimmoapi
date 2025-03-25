package bts.sio.api.service;

import bts.sio.api.model.Reparation;
import bts.sio.api.repository.ReparationRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
@Data
@Service

public class ReparationService {

    @Autowired
    private ReparationRepository ReparationRepository;

    public Iterable<Reparation> getReparations() {
        return ReparationRepository.findAll();
    }

    public Optional<Reparation> getReparationById(final Long id) {
        return ReparationRepository.findById(id);
    }

    public void deleteReparation(final Long id) {
        ReparationRepository.deleteById(id);
    }

    public Reparation saveReparation(Reparation Reparation) {
        Reparation saved = ReparationRepository.save(Reparation);
        return saved;
    }
}
