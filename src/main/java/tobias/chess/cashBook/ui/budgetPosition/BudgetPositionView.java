package tobias.chess.cashBook.ui.budgetPosition;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.ui.MainLayout;

@Route(value = "budgetPosition", layout = MainLayout.class)
@PageTitle("Budget-Positions")
public class BudgetPositionView extends VerticalLayout implements HasUrlParameter<Long> {

    private CashBookDtoService cashBookService;
    private final BudgetPositionService budgetPositionService;

    private Select<CashBookDto> cashBookSelect = new Select<>();

    private Grid<BudgetPosition> grid = new Grid<>(BudgetPosition.class);

    public BudgetPositionView(CashBookDtoService cashBookService, BudgetPositionService budgetPositionService) {
        this.cashBookService = cashBookService;
        this.budgetPositionService = budgetPositionService;
        addClassName("budget-position-view");

        loadView();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long cashBookId) {
        cashBookService.findById(cashBookId).ifPresent(this::setCashBook);
    }

    public void setCashBook(CashBookDto cashBook) {
        updateList(cashBook);
        cashBookSelect.setValue(cashBook);
    }

    private void loadView() {
        H1 header = new H1("Budget Position Overview");
        configureFilter();
        configureGrid();
        add(header, cashBookSelect, grid);
    }

    private void configureFilter() {
        cashBookSelect.setLabel("Cash Book");
        cashBookSelect.addValueChangeListener(event -> setCashBook(event.getValue()));
        updateSelectEntries();
    }

    private void updateSelectEntries() {
        cashBookSelect.setItemLabelGenerator(CashBookDto::getName);
        cashBookSelect.setItems(cashBookService.findAllDtos());
    }

    private void configureGrid() {
        grid.addClassName("cash-book-entry-grid");
        grid.setSizeFull();
        grid.setColumns("positionString", "header", "title", "point", "entry");
        grid.setHeightByRows(true);
        Binder<BudgetPosition> binder = new Binder<>(BudgetPosition.class);
        Editor<BudgetPosition> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
    }

    private void updateList(CashBookDto cashBook) {
        grid.setItems(budgetPositionService.findAllByCashBookDto(cashBook));
    }

}
