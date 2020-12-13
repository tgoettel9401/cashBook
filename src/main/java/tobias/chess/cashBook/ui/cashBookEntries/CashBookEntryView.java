package tobias.chess.cashBook.ui.cashBookEntries;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.ui.MainLayout;

@Route(value = "cashBookEntry", layout = MainLayout.class)
@PageTitle("Cash Book Entries")
public class CashBookEntryView extends VerticalLayout implements HasUrlParameter<Long> {

    private CashBookDtoService cashBookService;
    private CashBookEntryService cashBookEntryService;

    private Select<CashBookDto> cashBookSelect = new Select<>();
    Grid<CashBookEntryDto> cashBookEntryGrid = new Grid<>(CashBookEntryDto.class);

    public CashBookEntryView(CashBookEntryService cashBookEntryService, CashBookDtoService cashBookService) {
        this.cashBookEntryService = cashBookEntryService;
        this.cashBookService = cashBookService;
        addClassName("cash-book-entry-view");

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
        H1 header = new H1("Cash Book Entry Overview");

        configureFilter();
        configureGrid();

        add(header, cashBookSelect, cashBookEntryGrid);

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
        cashBookEntryGrid.addClassName("cash-book-entry-grid");
        cashBookEntryGrid.setSizeFull();
        cashBookEntryGrid.setColumns("id", "position", "incomeExpensePosition", "title", "income", "expense",
                "receiverSender", "valueDate");
        cashBookEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        cashBookEntryGrid.setHeightByRows(true);
    }

    private void updateList(CashBookDto cashBook) {
        cashBookEntryGrid.setItems(cashBookEntryService.findAllDtosByCashBookDto(cashBook));
    }

}
