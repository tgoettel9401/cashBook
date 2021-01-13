package tobias.chess.cashBook.ui.budgetPosition;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.*;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.ui.MainLayout;

import java.util.List;

@Route(value = "budgetPosition", layout = MainLayout.class)
@PageTitle("Budget-Positions")
@CssImport(value = "./styles/shared-styles.css", themeFor = "vaadin-grid")
public class BudgetPositionView extends VerticalLayout implements HasUrlParameter<Long> {

    private CashBookDtoService cashBookService;
    private final BudgetPositionService budgetPositionService;

    private Select<CashBookDto> cashBookSelect = new Select<>();

    private Grid<BudgetPosition> grid = new Grid<>();

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
        return new HorizontalLayout(addBudgetPosition);
    }

	private void configureGrid() {
        grid.addClassName("cash-book-entry-grid");
        grid.setSizeFull();
        grid.addColumn(BudgetPosition::getPosition).setHeader("Position");
        grid.addColumn(BudgetPosition::getHeaderString).setHeader("Header");
        grid.addColumn(BudgetPosition::getTitleString).setHeader("Title");
        grid.addColumn(BudgetPosition::getPointString).setHeader("Point");
        grid.addColumn(BudgetPosition::getTagsString).setHeader("Tags");
        grid.setHeightByRows(true);

        Binder<BudgetPosition> binder = new Binder<>(BudgetPosition.class);
        Editor<BudgetPosition> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
    }

    private void updateList(CashBookDto cashBook) {
        grid.setItems(budgetPositionService.findAllByCashBookDto(cashBook));
        grid.setClassNameGenerator(budgetPosition -> {
            switch (budgetPosition.getLevel()) {
                case HEADER:
                    return "budget-position-header";
                case TITLE:
                    return "budget-position-title";
                case POINT:
                    return "budget-position-point";
                default:
                    return "";
            }
        });
    }
    
    private void handleSaveBudgetPosition(BudgetPositionDialog.SaveEvent event) {
    	BudgetPosition budgetPosition = event.getBudgetPosition();
    	updateList(budgetPosition.getCashBookDto());
    }

    private void handleSaveBudgetPositionFailed(BudgetPositionDialog.SaveFailedEvent event) {
        List<ValidationResult> errors = event.getErrors();

        Notification notification = new Notification();
        notification.setPosition(Notification.Position.MIDDLE);
        errors.forEach(error -> notification.add(new Paragraph(error.getErrorMessage())));
        Button closeButton = new Button("Close");
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.addClickListener(innerEvent -> {
            notification.close();
            openAddBudgetPositionDialog();
        });
        closeButton.addClickShortcut(Key.ESCAPE);
        closeButton.addClickShortcut(Key.ENTER);
        notification.add(closeButton);
        notification.open();

    }
    
    private void openAddBudgetPositionDialog() {

        if (cashBookSelect.isEmpty())
            Notification.show("No Cash-Book has been selected, please do that first!", 2000,
                    Notification.Position.MIDDLE);

        else {
            BudgetPositionDialog dialog = new BudgetPositionDialog(budgetPositionService);
            dialog.setCashBook(cashBookSelect.getValue());
            dialog.setHeaderItems(budgetPositionService.findAllHeaders());
            dialog.setTitleItems(budgetPositionService.findAllTitles());
            dialog.setPointItems(budgetPositionService.findAllPoints());
            dialog.addListener(BudgetPositionDialog.SaveEvent.class, this::handleSaveBudgetPosition);
            dialog.addListener(BudgetPositionDialog.SaveFailedEvent.class, this::handleSaveBudgetPositionFailed);
        }

	}

}
