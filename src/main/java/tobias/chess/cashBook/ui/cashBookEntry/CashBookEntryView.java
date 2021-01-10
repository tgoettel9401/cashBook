package tobias.chess.cashBook.ui.cashBookEntry;

import com.google.common.collect.Maps;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.*;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.business.csvImport.CsvImportService;
import tobias.chess.cashBook.business.csvImport.MultipleCashBooksException;
import tobias.chess.cashBook.ui.MainLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Route(value = "cashBookEntry", layout = MainLayout.class)
@PageTitle("Cash Book Entries")
public class CashBookEntryView extends VerticalLayout implements HasUrlParameter<Long> {

    private CashBookDtoService cashBookService;
    private CashBookEntryService cashBookEntryService;
    private BudgetPositionService budgetPositionService;
    private CsvImportService csvImportService;

    private Select<CashBookDto> cashBookSelect = new Select<>();

    private BulletinTable bulletinTable;
    private BudgetTable budgetTable;

    public CashBookEntryView(CashBookEntryService cashBookEntryService, CashBookDtoService cashBookService,
                             BudgetPositionService budgetPositionService, CsvImportService csvImportService) {
        this.cashBookEntryService = cashBookEntryService;
        this.cashBookService = cashBookService;
        this.budgetPositionService = budgetPositionService;
        this.csvImportService = csvImportService;
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

        this.bulletinTable = new BulletinTable(cashBookEntryService, budgetPositionService);
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

        HorizontalLayout filterAndButton = new HorizontalLayout(cashBookSelect, createCsvUpload());

        add(header, filterAndButton, tabs, bulletinTable, budgetTable);

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

    private Component createCsvUpload() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> handleFileUpload(buffer.getInputStream()));
        return upload;
    }

    private void handleFileUpload(InputStream inputStream) {
        try {
            List<CashBookEntry> importedEntries = csvImportService.importSparkasseCsvsFromInputStream(inputStream);
            System.out.println("Imported " + importedEntries.size() + " entries");
        } catch (IOException | MultipleCashBooksException exception) {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.add(new Span(exception.getMessage()));
            Button closeButton = new Button("Close");
            closeButton.addClickListener(event -> notification.close());
            notification.add(closeButton);
            notification.open();
        }
    }

}
