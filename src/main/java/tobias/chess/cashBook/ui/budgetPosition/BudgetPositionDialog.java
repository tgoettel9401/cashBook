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

    	binder.withValidator((value, valueContext) -> {
    	    // Cash-Book may not be empty.
            if (value.getCashBook() == null)
                return ValidationResult.error("Empty Cash-Book is not allowed!");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // At least one element (header, title or point) should be set to new.
            if (!(value.isAddHeader() || value.isAddTitle() || value.isAddPoint()))
                return ValidationResult.error("At least one element needs to be added!");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Header cannot be New and Skipped at the same time.
            if (value.isAddHeader() && value.isSkipHeader())
                return ValidationResult.error("Header is set with New and Skip. Only one checkbox can be set!");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Title cannot be New and Skipped at the same time.
            if (value.isAddTitle() && value.isSkipTitle())
                return ValidationResult.error("Title is set with New and Skip. Only one checkbox can be set!");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Point cannot be New and Skipped at the same time.
            if (value.isAddPoint() && value.isSkipPoint())
                return ValidationResult.error("Point is set with New and Skip. Only one checkbox can be set!");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having Add-Point true and either Point-Name or Point-Position null is not allowed.
            if (value.isAddPoint() && (value.getPointPosition() == null || value.getPointName() == null))
                return ValidationResult.error("Point is set with New, then both Point-Name and Point-Position must be set");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having Add-Title true and either Title-Name or Title-Position null is not allowed.
            if (value.isAddTitle() && (value.getTitlePosition() == null || value.getTitleName() == null))
                return ValidationResult.error("Title is set with New, then both Title-Name and Title-Position must be set");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having Add-Header true and either Header-Name or Header-Position null is not allowed.
            if (value.isAddHeader() && (value.getHeaderPosition() == null || value.getHeaderName() == null))
                return ValidationResult.error("Header is set with New, then both Header-Name and Header-Position must be set");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Adding new title but skipping header is not allowed.
            if (value.isAddTitle() && value.isSkipHeader())
                return ValidationResult.error("New Title should be added, then skipping Header is not allowed");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Adding new point but skipping either header or title is not allowed.
            if (value.isAddPoint() && (value.isSkipHeader() || value.isSkipTitle()))
                return ValidationResult.error("New Point should be added, then skipping Header or Title is not allowed");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having neither Add-Header nor Skip-Header, then Select must not be empty.
            if (!value.isAddHeader() && !value.isSkipHeader() && (value.getHeaderFromSelect() == null))
                return ValidationResult.error("Header is neither set as New nor as Skip, so Select must not be empty");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having neither Add-Title nor Skip-Title, then Select must not be empty.
            if (!value.isAddTitle() && !value.isSkipTitle() && (value.getTitleFromSelect() == null))
                return ValidationResult.error("Title is neither set as New nor as Skip, so Select must not be empty");
            else
                return ValidationResult.ok();
        }).withValidator((value, valueContext) -> {
            // Having neither Add-Point nor Skip-Point, then Select must not be empty.
            if (!value.isAddPoint() && !value.isSkipPoint() && (value.getPointFromSelect() == null))
                return ValidationResult.error("Point is neither set as New nor as Skip, so Select must not be empty");
            else
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
