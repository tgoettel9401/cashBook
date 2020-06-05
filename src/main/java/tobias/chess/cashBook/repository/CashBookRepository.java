package tobias.chess.cashBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tobias.chess.cashBook.model.CashBook;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long> {
    CashBook findByAccountNumber(String accountNumber);
}
