package tobias.chess.cashBook.ui.cashBookEntry;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

import java.util.List;

public class BudgetPositionEditDialog extends FormLayout {

    private final BudgetPositionService budgetPositionService;
    private final CashBookEntryService cashBookEntryService;

    private CashBookEntryDto cashBookEntry;
    Binder<CashBookEntryDto> binder = new Binder<>(CashBookEntryDto.class);

    Dialog dialog;
    Select<BudgetPosition> budgetPositionSelect = new Select<>();
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Cancel");

    public BudgetPositionEditDialog(BudgetPositionService budgetPositionService, CashBookEntryService cashBookEntryService,
                                    CashBookEntryDto entry, CashBookDto cashBookDto) {
        this.budgetPositionService = budgetPositionService;
        this.cashBookEntryService = cashBookEntryService;
        this.cashBookEntry = entry;
        loadView(cashBookDto);
    }

    private void loadView(CashBookDto cashBookDto) {

        budgetPositionSelect.setLabel("Budget Position");
        budgetPositionSelect.setItems(getBudgetPositions(cashBookDto));
        budgetPositionSelect.setItemLabelGenerator(BudgetPosition::getName);
        binder.forField(budgetPositionSelect).bind("budgetPosition");

        dialog = new Dialog();
        dialog.add(budgetPositionSelect, createButtonLayout());
        dialog.open();

    }

    private HorizontalLayout createButtonLayout() {

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
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
        return this.budgetPositionService.findAllByCashBookDto(cashBookDto);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(cashBookEntry);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        cashBookEntryService.save(cashBookEntry);
    }

}
