package tobias.chess.cashBook.business.budgetPosition.title;

import org.springframework.data.jpa.repository.JpaRepository;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;

import java.util.List;

public interface BudgetPositionTitleRepository extends JpaRepository<BudgetPositionTitle, Long> {
    List<BudgetPositionTitle> findAllByHeader(BudgetPositionHeader header);
}
