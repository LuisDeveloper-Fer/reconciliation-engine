package dev.portfolio.infrastructure;
import dev.portfolio.domain.ReconciliationRun;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RunRepository extends JpaRepository<ReconciliationRun,String>{}
