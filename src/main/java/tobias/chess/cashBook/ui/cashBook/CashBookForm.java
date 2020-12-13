package tobias.chess.cashBook.ui.cashBook;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.shared.Registration;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

public class CashBookForm extends FormLayout {

    private CashBookDto cashBook;

    Binder<CashBookDto> binder = new Binder<>(CashBookDto.class);

    TextField accountNumber = new TextField("Account Number");
    TextField name = new TextField("Name");
    TextField initialWealth = new TextField("Initial Wealth");
    TextField calculatedInitialWealth = new TextField("Calcualted Initial Wealth");

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button closeButton = new Button("Cancel");
    Button getToEntriesButton = new Button("Get to entries");

    public CashBookForm() {
        addClassName("cash-book-form");
        initializeBinder();
        add(accountNumber, name, initialWealth, calculatedInitialWealth, createButtonsLayout());
    }

    public void setCashBook(CashBookDto cashBook) {
        this.cashBook = cashBook;
        binder.readBean(cashBook);
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, cashBook)));
        closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        getToEntriesButton.addClickListener(event -> fireEvent(new LoadEntriesEvent(this, cashBook)));

        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, deleteButton, closeButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(cashBook);
            fireEvent(new SaveEvent(this, cashBook));
        } catch (ValidationException ex) {
            ex.printStackTrace();
        }
    }

    // Set the BigDecimal fields manually, but the rest as Instance Fields.
    private void initializeBinder() {
        binder.forField(initialWealth).withConverter(new StringToBigDecimalConverter(""))
                .bind(CashBookDto::getInitialWealth, CashBookDto::setInitialWealth);
        binder.forField(calculatedInitialWealth).withConverter(new StringToBigDecimalConverter(""))
                .bind(CashBookDto::getCalculatedInitialWealth, CashBookDto::setCalculatedInitialWealth);
        binder.bindInstanceFields(this);
    }

    // Events
    public static abstract class CashBookFormEvent extends ComponentEvent<CashBookForm> {
        private CashBookDto cashBook;

        protected CashBookFormEvent(CashBookForm source, CashBookDto cashBook) {
            super(source, false);
            this.cashBook = cashBook;
        }

        public CashBookDto getCashBook() {
            return cashBook;
        }
    }

    public static class SaveEvent extends CashBookFormEvent {
        SaveEvent(CashBookForm source, CashBookDto cashBook) {
            super(source, cashBook);
        }
    }

    public static class DeleteEvent extends CashBookFormEvent {
        DeleteEvent(CashBookForm source, CashBookDto cashBook) {
            super(source, cashBook);
        }

    }

    public static class CloseEvent extends CashBookFormEvent {
        CloseEvent(CashBookForm source) {
            super(source, null);
        }
    }

    public static class LoadEntriesEvent extends CashBookFormEvent {
        LoadEntriesEvent(CashBookForm source, CashBookDto cashBook) {
            super (source, cashBook);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

