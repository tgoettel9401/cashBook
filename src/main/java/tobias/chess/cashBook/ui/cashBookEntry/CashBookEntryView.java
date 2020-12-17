package tobias.chess.cashBook.ui.cashBookEntry;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.ui.MainLayout;

import java.util.Map;

@Route(value = "cashBookEntry", layout = MainLayout.class)
@PageTitle("Cash Book Entries")
public class CashBookEntryView extends VerticalLayout implements HasUrlParameter<Long> {

    private CashBookDtoService cashBookService;
    private CashBookEntryService cashBookEntryService;

    private Select<CashBookDto> cashBookSelect = new Select<>();

    private BulletinTable bulletinTable;
    private BudgetTable budgetTable;

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
        this.bulletinTable.setCashBook(cashBook);
        cashBookSelect.setValue(cashBook);
    }

    private void loadView() {
        H1 header = new H1("Cash Book Entry Overview");

        configureFilter();

        this.bulletinTable = new BulletinTable(cashBookEntryService);
        this.budgetTable = new BudgetTable(cashBookEntryService);

        Tab tabBulletin = new Tab("Bulletin");
        Tab tabBudget = new Tab("Budget");
        Tabs tabs = new Tabs(tabBulletin, tabBudget);
        Map<Tab, Component> tabsToPages = Maps.newHashMap();
        tabsToPages.put(tabBulletin, bulletinTable);
        tabsToPages.put(tabBudget, budgetTable);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        bulletinTable.setVisible(false);

        add(header, cashBookSelect, tabs, bulletinTable, budgetTable);

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

}
