package bts.sio.api.repository;

import org.springframework.stereotype.Repository;
import bts.sio.api.model.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BatimentRepository extends  JpaRepository<Batiment, Long> {
}
