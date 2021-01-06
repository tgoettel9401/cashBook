package tobias.chess.cashBook.ui.cashBook;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
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

    private Grid<CashBookDto> cashBookGrid = new Grid<>(CashBookDto.class);
    private CashBookDialog cashBookDialog;

    public CashBookView(CashBookDtoService cashBookDtoService) {
        this.cashBookDtoService = cashBookDtoService;
        addClassName("cash-book-view");
        setSizeFull();

        loadView();
    }

    public void editCashBook(CashBookDto cashBook) {
        if (cashBook != null) {
            cashBookDialog = new CashBookDialog();
            cashBookDialog.addListener(CashBookDialog.SaveEvent.class, this::saveCashBook);
            cashBookDialog.setCashBook(cashBook);
            addClassName("editing");
        }
    }

    private void loadView() {
        H1 header = new H1("Cash Book Overview");

        Button addCashBookButton = new Button("Add cash book");
        addCashBookButton.addClickListener(click -> addCashBook());

        configureGrid();

        Div content = new Div(cashBookGrid);
        content.addClassName("content");
        content.setSizeFull();

        add(header, addCashBookButton, content);
        updateList();
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

        cashBookGrid.addItemDoubleClickListener(event -> navigateToEntries(event.getItem()));

        GridContextMenu<CashBookDto> contextMenu = cashBookGrid.addContextMenu();
        contextMenu.addItem("Open", event -> event.getItem().ifPresent(this::navigateToEntries));
        contextMenu.addItem("Edit", event -> event.getItem().ifPresent(this::editCashBook));
        contextMenu.addItem("Delete", event -> event.getItem().ifPresent(this::deleteCashBook));
    }

    private void updateList() {
        cashBookGrid.setItems(cashBookDtoService.findAllDtos());
    }

    private void saveCashBook(CashBookDialog.SaveEvent event) {
        cashBookDtoService.save(event.getCashBook());
        updateList();
    }

    private void deleteCashBook(CashBookDto cashBook) {
        cashBookDtoService.delete(cashBook);
        updateList();
    }

    private void navigateToEntries(CashBookDto cashBook) {
        this.getUI().ifPresent(ui -> ui.navigate("cashBookEntry/" + cashBook.getId()));
    }

}