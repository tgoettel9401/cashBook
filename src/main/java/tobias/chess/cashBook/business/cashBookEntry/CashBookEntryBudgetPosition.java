package tobias.chess.cashBook.business.cashBookEntry;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntry;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
@Data
public class CashBookEntryBudgetPosition {

    @ManyToOne
    private BudgetPositionHeader budgetPositionHeader;

    @ManyToOne
    private BudgetPositionTitle budgetPositionTitle;

    @ManyToOne
    private BudgetPositionPoint budgetPositionPoint;

    @ManyToOne
    private BudgetPositionEntry budgetPositionEntry;

}
