package tobias.chess.cashBook.business.cashBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long> {
    Optional<CashBook> findByAccountNumber(String accountNumber);
}
