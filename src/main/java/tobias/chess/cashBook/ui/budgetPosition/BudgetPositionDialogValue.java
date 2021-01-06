package tobias.chess.cashBook.ui.budgetPosition;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

@Data
public class BudgetPositionDialogValue {
	
	private CashBookDto cashBook;
	
	// Add-Checkboxes
	private boolean addHeader;
	private boolean addTitle;
	private boolean addPoint;
	
	// Skip-Checkboxes
	private boolean skipHeader;
	private boolean skipTitle;
	private boolean skipPoint;
	
	// Selects
	private BudgetPositionHeader headerFromSelect;
	private BudgetPositionTitle titleFromSelect;
	private BudgetPositionPoint pointFromSelect;
	
	// Text-Fields
	private String headerName;
	private String titleName;
	private String pointName;
	
	// Number-Fields
	private Integer headerPosition;
	private Integer titlePosition;
	private Integer pointPosition;

}
