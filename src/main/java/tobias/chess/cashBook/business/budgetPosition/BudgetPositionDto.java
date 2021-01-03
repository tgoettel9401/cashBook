package tobias.chess.cashBook.business.budgetPosition;

import lombok.Data;

@Data
public class BudgetPositionDto {
	
	private BudgetPosition budgetPosition;
	
	private String positionString;
	private String header; 
	private String title; 
	private String point;
	private String entry;

}
