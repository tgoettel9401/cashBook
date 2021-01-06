package tobias.chess.cashBook.business.budgetPosition.header;

import org.springframework.data.jpa.repository.JpaRepository;
import tobias.chess.cashBook.business.cashBook.CashBook;

import java.util.List;

public interface BudgetPositionHeaderRepository extends JpaRepository<BudgetPositionHeader, Long>  {
    List<BudgetPositionHeader> findAllByCashBook(CashBook cashBook);
}
