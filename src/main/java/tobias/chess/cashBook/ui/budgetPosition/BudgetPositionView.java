package tobias.chess.cashBook.ui.budgetPosition;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
        Component buttons = createButtons();
        configureGrid();
        add(header, cashBookSelect, buttons, grid);
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
    
    private Component createButtons() {
    	Button addBudgetPosition = new Button("Add new Budget-Position");
    	addBudgetPosition.addClickListener(event -> openAddBudgetPositionDialog());
    	HorizontalLayout layout = new HorizontalLayout(addBudgetPosition);
    	return layout;
    }

	private void configureGrid() {
        grid.addClassName("cash-book-entry-grid");
        grid.setSizeFull();
        grid.removeAllColumns(); // TODO: Refactor so that removing is not necessary
        grid.addColumn(BudgetPosition::getPosition).setHeader("Position");
        grid.addColumn(BudgetPosition::getHeaderString).setHeader("Header");
        grid.addColumn(BudgetPosition::getTitleString).setHeader("Title");
        grid.addColumn(BudgetPosition::getPointString).setHeader("Point");
        grid.setHeightByRows(true);
        Binder<BudgetPosition> binder = new Binder<>(BudgetPosition.class);
        Editor<BudgetPosition> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
    }

    private void updateList(CashBookDto cashBook) {
        grid.setItems(budgetPositionService.findAllByCashBookDto(cashBook));
    }
    
    private void handleSaveBudgetPosition(BudgetPositionDialog.SaveEvent event) {
    	BudgetPosition budgetPosition = event.getBudgetPosition();
    	updateList(budgetPosition.getCashBookDto());
    }
    
    private void openAddBudgetPositionDialog() {
		BudgetPositionDialog dialog = new BudgetPositionDialog(budgetPositionService);
		dialog.setCashBook(cashBookSelect.getValue());
		dialog.setHeaderItems(budgetPositionService.findAllHeaders());
		dialog.setTitleItems(budgetPositionService.findAllTitles());
		dialog.setPointItems(budgetPositionService.findAllPoints());
		dialog.addListener(BudgetPositionDialog.SaveEvent.class, this::handleSaveBudgetPosition);
	}

}
