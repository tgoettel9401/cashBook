package tobias.chess.cashBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tobias.chess.cashBook.model.CashBookFile;

@Repository
public interface CashBookFileRepository extends JpaRepository<CashBookFile, Long> {
}
