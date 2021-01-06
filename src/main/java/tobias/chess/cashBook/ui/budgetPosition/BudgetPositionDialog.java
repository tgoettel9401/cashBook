package tobias.chess.cashBook.ui.budgetPosition;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.shared.Registration;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;

import java.util.List;

public class BudgetPositionDialog extends FormLayout {
	
	private final BudgetPositionService budgetPositionService;
	
	public BudgetPositionDialog(BudgetPositionService budgetPositionService) {
		this.budgetPositionService = budgetPositionService;
		loadView();
	}
	
	Dialog dialog;
	Select<CashBookDto> cashBookSelect = new Select<>();
	Binder<BudgetPositionDialogValue> binder = new Binder<>(BudgetPositionDialogValue.class);
	
	// Header
	Checkbox headerAddCheckbox = new Checkbox();
	Checkbox headerSkipCheckbox = new Checkbox();
	Select<BudgetPositionHeader> headerSelect = new Select<>();
	TextField headerTextField = new TextField();
	IntegerField headerNumberField = new IntegerField();
	
	// Title
	Checkbox titleAddCheckbox = new Checkbox();
	Checkbox titleSkipCheckbox = new Checkbox();
	Select<BudgetPositionTitle> titleSelect = new Select<>();
	TextField titleTextField = new TextField();
	IntegerField titleNumberField = new IntegerField();
	
	// Point
	Checkbox pointAddCheckbox = new Checkbox();
	Checkbox pointSkipCheckbox = new Checkbox();
	Select<BudgetPositionPoint> pointSelect = new Select<>();
	TextField pointTextField = new TextField();
	IntegerField pointNumberField = new IntegerField();
    
	// Buttons
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Cancel");
    
    public void setCashBook(CashBookDto cashBook) {
    	cashBookSelect.setItems(List.of(cashBook));
    	cashBookSelect.setValue(cashBook);
    }
    
    public void setHeaderItems(List<BudgetPositionHeader> headers) {
    	headerSelect.setItems(headers);
    }
    
    public void setTitleItems(List<BudgetPositionTitle> titles) {
    	titleSelect.setItems(titles);
    }
    
    public void setPointItems(List<BudgetPositionPoint> points) {
    	pointSelect.setItems(points);
    }

    private void loadView() {
        dialog = new Dialog();
        dialog.add(createFormLayout(), createButtonLayout());
        dialog.open();
    }
    
    private Component createFormLayout() {

        VerticalLayout layout = new VerticalLayout();

        cashBookSelect.setLabel("Cash-Book");
        cashBookSelect.setItemLabelGenerator(CashBookDto::getName);
        cashBookSelect.setEnabled(false);
        layout.add(cashBookSelect);

        HorizontalLayout headerLayout = new HorizontalLayout(headerAddCheckbox, headerSkipCheckbox,
                headerSelect, headerTextField, headerNumberField);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        headerAddCheckbox.setLabel("New");
        headerAddCheckbox.addValueChangeListener(event -> handleHeaderCheckbox());
        headerSkipCheckbox.setLabel("Skip");
        headerSelect.setLabel("Headers");
        headerSelect.setItemLabelGenerator(BudgetPositionHeader::getName);
        headerTextField.setVisible(false);
        headerTextField.setLabel("Header-Name");
        headerNumberField.setVisible(false);
        headerNumberField.setLabel("Header-Position");
        layout.add(headerLayout);

        HorizontalLayout titleLayout = new HorizontalLayout(titleAddCheckbox, titleSkipCheckbox,
                titleSelect, titleTextField, titleNumberField);
        titleLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        titleAddCheckbox.setLabel("New");
        titleAddCheckbox.addValueChangeListener(event -> handleTitleCheckbox());
        titleSkipCheckbox.setLabel("Skip");
        titleSelect.setLabel("Titles");
        titleSelect.setItemLabelGenerator(BudgetPositionTitle::getName);
        titleTextField.setVisible(false);
        titleTextField.setLabel("Title-Name");
        titleNumberField.setVisible(false);
        titleNumberField.setLabel("Title-Position");
        layout.add(titleLayout);

        HorizontalLayout pointLayout = new HorizontalLayout(pointAddCheckbox, pointSkipCheckbox,
                pointSelect, pointTextField, pointNumberField);
        pointLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        pointAddCheckbox.setLabel("New");
        pointAddCheckbox.addValueChangeListener(event -> handlePointCheckbox());
        pointSkipCheckbox.setLabel("Skip");
        pointSelect.setLabel("Points");
        pointSelect.setItemLabelGenerator(BudgetPositionPoint::getName);
        pointTextField.setLabel("Point-Name");
        pointTextField.setVisible(false);
        pointNumberField.setLabel("Point-Position");
        pointNumberField.setVisible(false);
        layout.add(pointLayout);

        binder.forField(cashBookSelect).bind("cashBook");
        binder.forField(headerAddCheckbox).bind("addHeader");
        binder.forField(titleAddCheckbox).bind("addTitle");
        binder.forField(pointAddCheckbox).bind("addPoint");
        binder.forField(headerSkipCheckbox).bind("skipHeader");
        binder.forField(titleSkipCheckbox).bind("skipTitle");
        binder.forField(pointSkipCheckbox).bind("skipPoint");
        binder.forField(headerSelect).bind("headerFromSelect");
        binder.forField(titleSelect).bind("titleFromSelect");
        binder.forField(pointSelect).bind("pointFromSelect");
        binder.forField(headerTextField).bind("headerName");
        binder.forField(titleTextField).bind("titleName");
        binder.forField(pointTextField).bind("pointName");
        binder.forField(headerNumberField).bind("headerPosition");
        binder.forField(titleNumberField).bind("titlePosition");
        binder.forField(pointNumberField).bind("pointPosition");

        addBinderValidation();

        return layout;

    }

    private Component createButtonLayout() {

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        closeButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> {
            validateAndSave();
            dialog.close();
        });
        closeButton.addClickListener(event -> dialog.close());

        return new HorizontalLayout(saveButton, closeButton);

    }
    
    private void handleHeaderCheckbox() {
    	if (headerAddCheckbox.getValue()) {
        	headerSelect.setVisible(false);
        	headerTextField.setVisible(true);
        	headerNumberField.setVisible(true);
    	}
    	else {
    		headerSelect.setVisible(true);
    		headerTextField.setVisible(false);
    		headerNumberField.setVisible(false);
    	}
    }
    
    private void handleTitleCheckbox() {
    	if (titleAddCheckbox.getValue()) {
        	titleSelect.setVisible(false);
        	titleTextField.setVisible(true);
        	titleNumberField.setVisible(true);
    	}
    	else {
    		titleSelect.setVisible(true);
    		titleTextField.setVisible(false);
    		titleNumberField.setVisible(false);
    	}
    }

    private void handlePointCheckbox() {
    	if (pointAddCheckbox.getValue()) {
    		pointSelect.setVisible(false);
        	pointTextField.setVisible(true);
        	pointNumberField.setVisible(true);
    	}
    	else {
    		pointSelect.setVisible(true);
    		pointTextField.setVisible(false);
    		pointNumberField.setVisible(false);
    	}
    }
    
    private void validateAndSave() {
    	BudgetPositionDialogValue value = new BudgetPositionDialogValue();
        try {
            binder.writeBean(value);
        } catch (ValidationException e) {
            fireEvent(new SaveFailedEvent(this, e.getValidationErrors()));
        }

        CashBookDto cashBook = cashBookSelect.getValue();
        BudgetPositionHeader header; 
        BudgetPositionTitle title;
        BudgetPositionPoint point; 
        boolean saveIsPerformed = false; 

        try {

            // Save Header if applicable. It starts with Header because this may be needed for title and point.
            if (value.isAddHeader()) {
                String name = headerTextField.getValue();
                Integer position = headerNumberField.getValue();
                header = budgetPositionService.saveHeader(cashBook, name, position);
                saveIsPerformed = true;
            }
            else {
                header = headerSelect.getValue();
            }

            // Save Title if applicable.
            if (value.isAddTitle()) {
                String name = titleTextField.getValue();
                Integer position = titleNumberField.getValue();
                title = budgetPositionService.saveTitle(header, name, position);
                saveIsPerformed = true;
            }
            else {
                title = titleSelect.getValue();
            }

            // Save Point if applicable.
            if (value.isAddPoint()) {
                String name = pointTextField.getValue();
                Integer position = pointNumberField.getValue();
                point = budgetPositionService.savePoint(title, name, position);
                saveIsPerformed = true;
            }
            else {
                point = pointSelect.getValue();
            }

            if (saveIsPerformed) {
                BudgetPosition budgetPosition = new BudgetPosition();
                budgetPosition.setCashBookDto(cashBook);
                budgetPosition.setHeader(header);
                budgetPosition.setTitle(title);
                budgetPosition.setPoint(point);
                fireEvent(new SaveEvent(this, budgetPosition));
            }

        } catch (Exception ex) {
            Notification exceptionNotification = new Notification();
            exceptionNotification.setPosition(Position.MIDDLE);
            exceptionNotification.add(new Paragraph(ex.getMessage()));
            exceptionNotification.open();
        }
        
    }

    private void addBinderValidation() {

    	// TODO: Extend with additional validators.
    	// TODO: Also add validations on field-level.
    	// TODO: Add validation that neither header, title nor point has Add & Skip since this is not possible.
        // TODO: Add validation that for each header, title, point Name and Position is set if Add is active.
    	// TODO: Add validation that nothing has an Add if a previous Skip was set. 
    	// TODO: Add validation that for an add all previous have adds as well or select is set. 
    	// TODO: Add validation that CashBook is set.
    	binder.withValidator((value, valueContext) -> {

            // At least one element (header, title or point) should be set to new.
            if (!(value.isAddHeader() || value.isAddTitle() || value.isAddPoint()))
                return ValidationResult.error("At least one element needs to be added!");

            return ValidationResult.ok();

        });

    }
    
    public static class SaveEvent extends ComponentEvent<BudgetPositionDialog> {
    	private BudgetPosition budgetPosition;
        protected SaveEvent(BudgetPositionDialog source, BudgetPosition budgetPosition) {
            super(source, false);
            this.budgetPosition = budgetPosition;
        }
        public BudgetPosition getBudgetPosition() {
        	return budgetPosition;
        }
    }

    public static class SaveFailedEvent extends ComponentEvent<BudgetPositionDialog> {
        private List<ValidationResult> errors;
        protected SaveFailedEvent(BudgetPositionDialog source, List<ValidationResult> errors) {
            super(source, false);
            this.errors = errors;
        }
        public List<ValidationResult> getErrors() {
            return errors;
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
