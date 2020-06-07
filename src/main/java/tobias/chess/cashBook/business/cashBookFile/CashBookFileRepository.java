package tobias.chess.cashBook.business.cashBookFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashBookFileRepository extends JpaRepository<CashBookFile, Long> {
}
