package tobias.chess.cashBook.ui.cashBook;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Cash-Books")
public class CashBookView extends VerticalLayout {

    private CashBookDtoService cashBookDtoService;

    Grid<CashBookDto> cashBookGrid = new Grid<>(CashBookDto.class);
    private CashBookForm cashBookForm;

    public CashBookView(CashBookDtoService cashBookDtoService) {
        this.cashBookDtoService = cashBookDtoService;
        addClassName("cash-book-view");
        setSizeFull();

        loadView();
    }

    public void editCashBook(CashBookDto cashBook) {
        if (cashBook == null) {
            closeEditor();
        } else {
            cashBookForm.setCashBook(cashBook);
            cashBookForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void loadView() {
        H1 header = new H1("Cash Book Overview");

        Button addCashBookButton = new Button("Add cash book");
        addCashBookButton.addClickListener(click -> addCashBook());

        configureGrid();

        cashBookForm = new CashBookForm();
        cashBookForm.addListener(CashBookForm.SaveEvent.class, this::saveCashBook);
        cashBookForm.addListener(CashBookForm.DeleteEvent.class, this::deleteCashBook);
        cashBookForm.addListener(CashBookForm.CloseEvent.class, e -> closeEditor());
        cashBookForm.addListener(CashBookForm.LoadEntriesEvent.class, e -> loadEntries());

        Div content = new Div(cashBookGrid, cashBookForm);
        content.addClassName("content");
        content.setSizeFull();

        add(header, addCashBookButton, content);
        updateList();
        closeEditor();
    }

    private void addCashBook() {
        cashBookGrid.asSingleSelect().clear();
        editCashBook(new CashBookDto());
    }

    private void configureGrid() {
        cashBookGrid.addClassName("cash-book-grid");
        cashBookGrid.setSizeFull();
        cashBookGrid.setColumns("id", "accountNumber", "name", "initialWealth", "calculatedInitialWealth",
                "finalWealth", "calculatedFinalWealth", "result", "calculatedResult");
        cashBookGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        cashBookGrid.setHeightByRows(true);

        cashBookGrid.asSingleSelect().addValueChangeListener(event -> editCashBook(event.getValue()));
    }

    private void updateList() {
        cashBookGrid.setItems(cashBookDtoService.findAllDtos());
    }

    private void saveCashBook(CashBookForm.SaveEvent event) {
        cashBookDtoService.save(event.getCashBook());
        updateList();
        closeEditor();
    }

    private void deleteCashBook(CashBookForm.DeleteEvent event) {
        cashBookDtoService.delete(event.getCashBook());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        cashBookForm.setCashBook(null);
        cashBookForm.setVisible(false);
        removeClassName("editing");
    }

    private void loadEntries() {

    }

}