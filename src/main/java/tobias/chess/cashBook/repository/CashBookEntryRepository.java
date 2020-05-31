package tobias.chess.cashBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tobias.chess.cashBook.model.CashBookEntry;

@Repository
public interface CashBookEntryRepository extends JpaRepository<CashBookEntry, Long> {
}
