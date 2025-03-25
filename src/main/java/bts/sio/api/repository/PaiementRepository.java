package bts.sio.api.repository;

import bts.sio.api.model.Paiement;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

    List<Paiement> findByContratId(Integer contratId);
    List<Paiement> findByDatePaiement(LocalDate datePaiement);
    List<Paiement> findByMontantGreaterThan(Double montant);
}