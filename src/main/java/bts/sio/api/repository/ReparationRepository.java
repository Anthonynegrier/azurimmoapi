package bts.sio.api.repository;

import bts.sio.api.model.Reparation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReparationRepository extends JpaRepository<Reparation, Long>{
}
