package tobias.chess.cashBook.business.budgetPosition;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

@Data
public class BudgetPositionDto {
	
	private CashBookDto cashBookDto;
	private BudgetPosition budgetPosition;
	private BudgetPositionType type; // TODO: Check if this is really necessary.
	
	private BudgetPositionHeader headerObject;
	private BudgetPositionTitle titleObject;
	private BudgetPositionPoint pointObject;
	
	// TODO: Refactor by removing these Strings here.
	private String positionString;
	private String header; 
	private String title; 
	private String point;

}
