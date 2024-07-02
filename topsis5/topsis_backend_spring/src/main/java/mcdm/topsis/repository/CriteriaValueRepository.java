package mcdm.topsis.repository;

import mcdm.topsis.entity.CriteriaValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaValueRepository extends JpaRepository<CriteriaValue, Long> {
}