package tobias.chess.cashBook.business.budgetPosition.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

import java.util.List;

public interface BudgetPositionEntryRepository extends JpaRepository<BudgetPositionEntry, Long> {
    List<BudgetPositionEntry> findAllByPoint(BudgetPositionPoint point);
}
