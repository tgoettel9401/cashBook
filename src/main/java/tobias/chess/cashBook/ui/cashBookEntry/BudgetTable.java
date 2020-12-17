package tobias.chess.cashBook.ui.cashBookEntry;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

public class BudgetTable extends VerticalLayout {

    private final CashBookEntryService cashBookEntryService;

    public BudgetTable(CashBookEntryService cashBookEntryService) {
        this.cashBookEntryService = cashBookEntryService;
    }

}
