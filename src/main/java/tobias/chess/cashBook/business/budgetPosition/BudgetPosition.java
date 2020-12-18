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

    public Integer getLevel() {
        Integer level = 0;
        if (header != null)
            level++;
        if (title != null)
            level++;
        if (point != null)
            level++;
        if (entry != null)
            level++;
        return level;
    }

    public String getName() {
        String name = "";
        switch (getLevel()) {
            case 1: name = header.getName(); break;
            case 2: name = title.getName(); break;
            case 3: name = point.getName(); break;
            case 4: name = entry.getName(); break;
        }
        return name;
    }

}
