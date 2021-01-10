package tobias.chess.cashBook.ui.cashBookEntry;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

public class BulletinTable extends VerticalLayout {

    private CashBookEntryService cashBookEntryService;
    private BudgetPositionService budgetPositionService;

    private CashBookDto cashBook;

    private Grid<CashBookEntryDto> cashBookEntryGrid = new Grid<>(CashBookEntryDto.class);

    public BulletinTable(CashBookEntryService cashBookEntryService, BudgetPositionService budgetPositionService) {
        this.cashBookEntryService = cashBookEntryService;
        this.budgetPositionService = budgetPositionService;
        configureGrid();
        add(cashBookEntryGrid);
    }

    public void setCashBook(CashBookDto cashBook) {
        this.cashBook = cashBook;
        updateList(cashBook);
    }

    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
    }

    private void configureGrid() {
        cashBookEntryGrid.addClassName("cash-book-entry-grid");
        cashBookEntryGrid.setSizeFull();
        cashBookEntryGrid.setColumns("id", "position", "incomeExpensePosition", "title", "income", "expense",
                "receiverSender", "valueDate");
        cashBookEntryGrid.setHeightByRows(true);
        Binder<CashBookEntryDto> binder = new Binder<>(CashBookEntryDto.class);
        Editor<CashBookEntryDto> editor = cashBookEntryGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
        configureColumns(binder, editor);
        cashBookEntryGrid.getColumnByKey("id").setVisible(false);
        GridContextMenu<CashBookEntryDto> contextMenu = cashBookEntryGrid.addContextMenu();
        contextMenu.addItem("Edit Budget-Position", event -> event.getItem().ifPresent(this::editBudgetPosition));
    }

    private void configureColumns(Binder<CashBookEntryDto> binder, Editor<CashBookEntryDto> editor) {

        // Position Column
        TextField positionField = new TextField();
        binder.forField(positionField).withConverter(new StringToIntegerConverter("")).bind( "position");
        cashBookEntryGrid.getColumnByKey("position").setEditorComponent(positionField);

        // Income Expense Position Column
        TextField incomeExpensePositionField = new TextField();
        binder.forField(incomeExpensePositionField).bind("incomeExpensePosition");
        cashBookEntryGrid.getColumnByKey("incomeExpensePosition")
                .setEditorComponent(incomeExpensePositionField);

        // Title Column
        TextField titleField = new TextField();
        binder.forField(titleField).bind("title");
        cashBookEntryGrid.getColumnByKey("title").setEditorComponent(titleField);

        // Income Column
        TextField incomeField = new TextField();
        binder.forField(incomeField).withConverter(new StringToBigDecimalConverter("")).bind("income");
        cashBookEntryGrid.getColumnByKey("income").setEditorComponent(incomeField);

        // Expense Column
        TextField expenseField = new TextField();
        binder.forField(expenseField).withConverter(new StringToBigDecimalConverter("")).bind("expense");
        cashBookEntryGrid.getColumnByKey("expense").setEditorComponent(expenseField);

        // Receiver Sender Column
        TextField receiverSenderField = new TextField();
        binder.forField(receiverSenderField).bind("receiverSender");
        cashBookEntryGrid.getColumnByKey("receiverSender").setEditorComponent(receiverSenderField);

        // Value Date Column
        DatePicker valueDateField = new DatePicker();
        binder.forField(valueDateField).bind("valueDate");
        cashBookEntryGrid.getColumnByKey("valueDate").setEditorComponent(valueDateField);

        // Budget Position Column
        cashBookEntryGrid.addColumn(cashBookEntry -> cashBookEntry.getBudgetPosition().getName())
                .setHeader("Budget Position")
                .setKey("budgetPosition");

        // Buttons Column
        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());
        Grid.Column<CashBookEntryDto> editorColumn = cashBookEntryGrid.addComponentColumn(cashBookEntry -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(cashBookEntry);
                titleField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> editor.save());
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        cashBookEntryGrid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

        editor.addSaveListener(
                event -> {
                    saveCashBookEntry(event.getItem());
                    editor.cancel();
                    updateList(cashBook);
                });

        cashBookEntryGrid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    private void saveCashBookEntry(CashBookEntryDto cashBookEntry) {
        cashBookEntryService.save(cashBookEntry);
    }

    private void updateList(CashBookDto cashBook) {
        cashBookEntryGrid.setItems(cashBookEntryService.findAllDtosByCashBookDto(cashBook));
    }

    private void editBudgetPosition(CashBookEntryDto cashBookEntry) {
        BudgetPositionEditDialog dialog = new BudgetPositionEditDialog(budgetPositionService, cashBookEntryService,
                cashBookEntry, cashBook);
        dialog.addListener(BudgetPositionEditDialog.SaveEvent.class, this::handleSaveEvent);
    }

    private void handleSaveEvent(BudgetPositionEditDialog.SaveEvent event) {
        updateList(cashBook);
    }

}
