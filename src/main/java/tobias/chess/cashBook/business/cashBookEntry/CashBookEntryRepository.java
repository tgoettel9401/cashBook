package tobias.chess.cashBook.business.cashBookEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tobias.chess.cashBook.business.cashBook.CashBook;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface CashBookEntryRepository extends JpaRepository<CashBookEntry, Long> {
    List<CashBookEntry> findAllByCashBook(CashBook cashBook);
}
