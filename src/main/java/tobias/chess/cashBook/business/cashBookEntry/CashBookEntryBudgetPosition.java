package tobias.chess.cashBook.business.cashBookEntry;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

@Embeddable
@Data
public class CashBookEntryBudgetPosition {

    @ManyToOne
    private BudgetPositionHeader budgetPositionHeader;

    @ManyToOne
    private BudgetPositionTitle budgetPositionTitle;

    @ManyToOne
    private BudgetPositionPoint budgetPositionPoint;

}
