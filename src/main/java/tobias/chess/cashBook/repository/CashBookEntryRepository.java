package tobias.chess.cashBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tobias.chess.cashBook.model.CashBookEntry;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface CashBookEntryRepository extends JpaRepository<CashBookEntry, Long> {
}
