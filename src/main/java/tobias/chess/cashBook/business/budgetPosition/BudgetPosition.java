package tobias.chess.cashBook.business.budgetPosition;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntry;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

@Data
public class BudgetPosition {

    private String positionString;
    private BudgetPositionHeader header;
    private BudgetPositionTitle title;
    private BudgetPositionPoint point;
    private BudgetPositionEntry entry;

}
