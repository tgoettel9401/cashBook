package tobias.chess.cashBook.business.budgetPosition;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

@Data
public class BudgetPosition {

    private CashBookDto cashBookDto;
    private BudgetPositionHeader header;
    private BudgetPositionTitle title;
    private BudgetPositionPoint point;

    public String getName() {
        String name = "";
        switch (getLevel()) {
            case 1: name = header.getName(); break;
            case 2: name = title.getName(); break;
            case 3: name = point.getName(); break;
        }
        return name;
    }

    public String getPosition() {
        StringBuilder stringBuilder = new StringBuilder();
        if (header != null)
            stringBuilder.append(header.getPosition());
        else
            stringBuilder.append(0);
        if (title != null)
            stringBuilder.append(title.getPosition());
        else
            stringBuilder.append(0);
        if (point != null)
            stringBuilder.append(point.getPosition());
        else
            stringBuilder.append(0);
        return stringBuilder.toString();
    }

    public String getHeaderString() {
        if (header != null)
            return header.getName();
        else
            return "";
    }

    public String getTitleString() {
        if (title != null)
            return title.getName();
        else
            return "";
    }

    public String getPointString() {
        if (point != null)
            return point.getName();
        else
            return "";
    }

    private Integer getLevel() {
        Integer level = 0;
        if (header != null)
            level++;
        if (title != null)
            level++;
        if (point != null)
            level++;
        return level;
    }

}
