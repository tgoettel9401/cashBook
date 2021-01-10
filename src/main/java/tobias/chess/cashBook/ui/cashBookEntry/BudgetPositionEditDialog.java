package tobias.chess.cashBook.ui.cashBookEntry;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionLevel;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionNotFoundException;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

import java.util.List;
import java.util.stream.Collectors;

public class BudgetPositionEditDialog extends FormLayout {

    private final BudgetPositionService budgetPositionService;
    private final CashBookEntryService cashBookEntryService;

    TextField positionKeyField = new TextField("Position Key");

    private CashBookEntryDto cashBookEntry;
    Binder<CashBookEntryDto> binder = new Binder<>(CashBookEntryDto.class);

    Dialog dialog;
    Button findBudgetPositionButton = new Button(VaadinIcon.SEARCH.create());
    private CashBookDto cashBook;
    Select<BudgetPosition> budgetPositionSelect = new Select<>();
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Cancel");

    public BudgetPositionEditDialog(BudgetPositionService budgetPositionService, CashBookEntryService cashBookEntryService,
                                    CashBookEntryDto entry, CashBookDto cashBookDto) {
        this.budgetPositionService = budgetPositionService;
        this.cashBookEntryService = cashBookEntryService;
        this.cashBookEntry = entry;
        cashBook = cashBookDto;
        loadView();
    }

    private void loadView() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.END);

        budgetPositionSelect.setLabel("Budget Position");
        budgetPositionSelect.setItems(getBudgetPositions(cashBook));
        budgetPositionSelect.setItemLabelGenerator(
                budgetPosition -> budgetPosition.getName() + " (" + budgetPosition.getPosition() + ")"
        );
        layout.add(positionKeyField, findBudgetPositionButton, budgetPositionSelect);

        positionKeyField.setLabel("Position Key");
        findBudgetPositionButton.addClickListener(event ->
                findBudgetPosition(positionKeyField.getValue())
        );
        findBudgetPositionButton.addClickShortcut(Key.ENTER);

        binder.forField(budgetPositionSelect).bind("budgetPosition");

        dialog = new Dialog();
        dialog.add(layout, createButtonLayout());
        dialog.open();

    }

    private HorizontalLayout createButtonLayout() {

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> {
            validateAndSave();
            dialog.close();
        });
        closeButton.addClickListener(event -> dialog.close());

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, closeButton);

    }

    private List<BudgetPosition> getBudgetPositions(CashBookDto cashBookDto) {
        return this.budgetPositionService.findAllByCashBookDto(cashBookDto)
                .stream()
                .filter(budgetPosition -> budgetPosition.getLevel().equals(BudgetPositionLevel.POINT))
                .collect(Collectors.toList());
    }

    private void validateAndSave() {
        try {
            binder.writeBean(cashBookEntry);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        cashBookEntryService.save(cashBookEntry);
        fireEvent(new SaveEvent(this, cashBookEntry));
    }

    private void findBudgetPosition(String position) {
        try {
            BudgetPosition budgetPosition = budgetPositionService.findByCashBookDtoAndPosition(cashBook, position);
            budgetPositionSelect.setValue(budgetPosition);
        } catch (BudgetPositionNotFoundException ex) {
            Notification.show(ex.getMessage(), 1000, Notification.Position.MIDDLE);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static class SaveEvent extends ComponentEvent<BudgetPositionEditDialog> {
        private CashBookEntryDto cashBookEntryDto;

        protected SaveEvent(BudgetPositionEditDialog source, CashBookEntryDto cashBookEntryDto) {
            super(source, false);
            this.cashBookEntryDto = cashBookEntryDto;
        }

        public CashBookEntryDto getCashBookEntry() {
            return cashBookEntryDto;
        }
    }

}
