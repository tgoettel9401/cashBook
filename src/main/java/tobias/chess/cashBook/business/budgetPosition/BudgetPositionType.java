package tobias.chess.cashBook.business.budgetPosition;

import lombok.Getter;

@Getter
public enum BudgetPositionType {
	HEADER(1), TITLE(2), POINT(3);
	
	private Integer level;
	
	BudgetPositionType(Integer level) {
		this.level = level;
	}
	
	public static BudgetPositionType of(Integer level) {
		for (BudgetPositionType type : BudgetPositionType.values()) {
			if (level.equals(type.getLevel())) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("Level " + level + " is not in allowed range 1-4");
	}
}
