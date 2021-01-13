package tobias.chess.cashBook.business.budgetPosition;

import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

import java.util.List;

@Data
public class BudgetPosition {

    private CashBookDto cashBookDto;
    private BudgetPositionHeader header;
    private BudgetPositionTitle title;
    private BudgetPositionPoint point;
    private List<String> tags = Lists.newArrayList();

    public String getName() {
        String name = "";
        switch (getLevel()) {
            case HEADER:
                name = header.getName();
                break;
            case TITLE:
                name = title.getName();
                break;
            case POINT:
                name = point.getName();
                break;
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

    public BudgetPositionLevel getLevel() {
        return BudgetPositionLevel.of(getLevelInteger());
    }

    public String getTagsString() {
        String tagString = "";
        int counter = 1;
        int size = tags.size();
        for (String tag : tags) {
            if (counter < size) {
                tagString = tagString.concat(tag + ", ");
            } else {
                tagString = tagString.concat(tag);
            }
            counter++;
        }
        return tagString;
    }

    private Integer getLevelInteger() {
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
