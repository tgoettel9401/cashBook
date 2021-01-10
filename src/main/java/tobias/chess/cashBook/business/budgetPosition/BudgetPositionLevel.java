package tobias.chess.cashBook.business.budgetPosition;

import lombok.Getter;

@Getter
public enum BudgetPositionLevel {
    NONE(0), HEADER(1), TITLE(2), POINT(3);

    private Integer level;

    BudgetPositionLevel(Integer level) {
        this.level = level;
    }

    public static BudgetPositionLevel of(Integer level) {
        switch (level) {
            case 1:
                return HEADER;
            case 2:
                return TITLE;
            case 3:
                return POINT;
            default:
                return NONE;
        }
    }

}
