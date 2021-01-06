package tobias.chess.cashBook.business.budgetPosition.point;

import org.springframework.data.jpa.repository.JpaRepository;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

import java.util.List;

public interface BudgetPositionPointRepository extends JpaRepository<BudgetPositionPoint, Long> {
    List<BudgetPositionPoint> findAllByTitle(BudgetPositionTitle title);
}
