package tobias.chess.cashBook.business.excelExport;

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinData;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinDataFactory;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinDataLine;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinHeaderLine;

@Service
public class BulletinSheetService {
	
	private final BulletinDataFactory dataFactory; 
	
	private DataFormat dataFormat; 
	private short currencyFormat;
	
	private CellStyle headerCellStyle; 
	private CellStyle subHeaderCellStyle;
	private CellStyle subHeaderCurrencyCellStyle; 
	private CellStyle dataTitleCellStyle; 
	private CellStyle dataTextCellStyle; 
	private CellStyle dataCurrencyCellStyle; 
	private CellStyle dataDateCellStyle; 
	
	public BulletinSheetService(BulletinDataFactory dataFactory) {
		this.dataFactory = dataFactory; 
	}

	public Sheet generateSheet(Workbook workbook, CashBook cashBook) {
		
		BulletinData bulletinData = dataFactory.createData(cashBook);
		
		Sheet sheet = workbook.createSheet("Bulletin");
		
		setCellStyles(sheet);
        
        setColumnWidths(sheet);
        
        // Create a Row with cells
        createHeaderRow(sheet, bulletinData.getHeaderLine());
        
        // Create SubHeaderRows with cells
        createSubHeaderRows(sheet, bulletinData);
        
        // Create DataRows with cells
        createDataRows(sheet, bulletinData);
        
        return sheet;
        
	}

	private void setColumnWidths(Sheet sheet) {
		sheet.setColumnWidth(0, (int) (11*256)); // Position, width is provided in 1/256 of a single character. 
		sheet.setColumnWidth(1, (int) (6.5*256));// IncomeExpenseCounter
		sheet.setColumnWidth(2, (int) (78*256));// Title	
		sheet.setColumnWidth(3, (int) (14*256));// Income
		sheet.setColumnWidth(4, (int) (14*256));// Expense
		sheet.setColumnWidth(5, (int) (35*256));// ReceiverSender	
		sheet.setColumnWidth(6, (int) (11*256));// Date
		sheet.setColumnWidth(7, (int) (21.5*256));// BudgetPosition
	}

	private void createHeaderRow(Sheet sheet, BulletinHeaderLine headerLine) {
		
		// Create row
		Row row = sheet.createRow(0);
		
		// Create ColumnCounter
		int columnCounter = 0;
		
		// Position cell
		Cell cellPosition = row.createCell(columnCounter); 
		cellPosition.setCellValue(headerLine.getPosition());
		cellPosition.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// IncomeExpenseCounter cell
		Cell cellIncomeExpenseCounter = row.createCell(columnCounter);
		cellIncomeExpenseCounter.setCellValue(headerLine.getIncomeExpenseCounter());
		cellIncomeExpenseCounter.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// Title cell
		Cell cellTitle = row.createCell(columnCounter);
		cellTitle.setCellValue(headerLine.getTitle());
		cellTitle.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// Income cell
		Cell cellIncome = row.createCell(columnCounter);
		cellIncome.setCellValue(headerLine.getIncome());
		cellIncome.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// Expense cell
		Cell cellExpense = row.createCell(columnCounter);
		cellExpense.setCellValue(headerLine.getExpense());
		cellExpense.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// ReceiverSender cell
		Cell cellReceiverSender = row.createCell(columnCounter);
		cellReceiverSender.setCellValue(headerLine.getReceiverSender());
		cellReceiverSender.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// Date cell
		Cell cellDate = row.createCell(columnCounter);
		cellDate.setCellValue(headerLine.getDate());
		cellDate.setCellStyle(headerCellStyle);
		columnCounter++;
		
		// BudgetPosition cell
		Cell cellBudgetPosition = row.createCell(columnCounter);
		cellBudgetPosition.setCellValue(headerLine.getBudgetPosition());
		cellBudgetPosition.setCellStyle(headerCellStyle);
		columnCounter++;

	}
	
	private void createSubHeaderRows(Sheet sheet, BulletinData bulletinData) {
		
		// Create Row for InitialWealth and CalculatedInitialWealth
		Row rowInitialWealth = sheet.createRow(1);
		Row rowCalculatedInitialWealth = sheet.createRow(2);
		
		// Initialize cells with CellStyle
		for (int i = 0; i <= 7; i++) {
			Cell cell1 = rowInitialWealth.createCell(i);
			cell1.setCellStyle(subHeaderCellStyle);
			Cell cell2 = rowCalculatedInitialWealth.createCell(i);
			cell2.setCellStyle(subHeaderCellStyle);
		}
		
		// Create cells for description
		Cell cellInitialWealthDescription = rowInitialWealth.createCell(2);
		cellInitialWealthDescription.setCellValue(bulletinData.getSubHeaderLineInitialWealth().getDescription());
		cellInitialWealthDescription.setCellStyle(subHeaderCellStyle);
		Cell cellCalculatedInitialWealthDescription = rowCalculatedInitialWealth.createCell(2);
		cellCalculatedInitialWealthDescription.setCellValue(bulletinData.getSubHeaderLineCalculatedInitialWealth().getDescription());
		cellCalculatedInitialWealthDescription.setCellStyle(subHeaderCellStyle);
		
		// Create cells for content
		Cell cellInitialWealthContent = rowInitialWealth.createCell(3);
		cellInitialWealthContent.setCellValue(bulletinData.getSubHeaderLineInitialWealth().getInitialWealth().doubleValue());
		cellInitialWealthContent.setCellStyle(subHeaderCurrencyCellStyle);
		Cell cellCalculatedInitialWealthContent = rowCalculatedInitialWealth.createCell(3);
		cellCalculatedInitialWealthContent.setCellValue(bulletinData.getSubHeaderLineCalculatedInitialWealth().getCalculatedInitialWealth().doubleValue());
		cellCalculatedInitialWealthContent.setCellStyle(subHeaderCurrencyCellStyle);
		
	}
	
	private void createDataRows(Sheet sheet, BulletinData bulletinData) {
		
		List<Row> dataRows = Lists.newArrayList();
		
		List<BulletinDataLine> dataLines = bulletinData.getDataLines();

		int rowCounter = 3;
		for (BulletinDataLine dataLine : dataLines) {
			
			Row row = sheet.createRow(rowCounter);
			
			int columnCounter = 0;
			
			// Position cell
			Cell cellPosition = row.createCell(columnCounter); 
			cellPosition.setCellValue(dataLine.getPosition());
			cellPosition.setCellStyle(dataTextCellStyle);
			columnCounter++;
			
			// IncomeExpenseCounter cell
			Cell cellIncomeExpenseCounter = row.createCell(columnCounter);
			cellIncomeExpenseCounter.setCellValue(dataLine.getIncomeExpenseCounter());
			cellIncomeExpenseCounter.setCellStyle(dataTextCellStyle);
			columnCounter++;
			
			// Title cell
			Cell cellTitle = row.createCell(columnCounter);
			cellTitle.setCellValue(dataLine.getTitle());
			cellTitle.setCellStyle(dataTitleCellStyle);
			columnCounter++;
			
			// Income cell
			Cell cellIncome = row.createCell(columnCounter);
			cellIncome.setCellValue(dataLine.getIncome().doubleValue());
			cellIncome.setCellStyle(dataCurrencyCellStyle);
			columnCounter++;
			
			// Expense cell
			Cell cellExpense = row.createCell(columnCounter);
			cellExpense.setCellValue(dataLine.getExpense().doubleValue());
			cellExpense.setCellStyle(dataCurrencyCellStyle);
			columnCounter++;
			
			// ReceiverSender cell
			Cell cellReceiverSender = row.createCell(columnCounter);
			cellReceiverSender.setCellValue(dataLine.getReceiverSender());
			cellReceiverSender.setCellStyle(dataTextCellStyle);
			columnCounter++;
			
			// Date cell
			Cell cellDate = row.createCell(columnCounter);
			cellDate.setCellValue(dataLine.getDate());
			cellDate.setCellStyle(dataDateCellStyle);
			
			columnCounter++;
			
			// BudgetPosition cell
			Cell cellBudgetPosition = row.createCell(columnCounter);
			cellBudgetPosition.setCellValue(dataLine.getBudgetPosition());
			cellBudgetPosition.setCellStyle(dataTextCellStyle);
			columnCounter++;
			
			dataRows.add(row);
			rowCounter++;
			
		}
		
	}
	
	private void setCellStyles(Sheet sheet) {
		
		dataFormat = sheet.getWorkbook().createDataFormat();
		currencyFormat = dataFormat.getFormat("#,##0.00 [$€-de-DE]");
		
		setHeaderStyles(sheet);
		setSubHeaderStyles(sheet);
		setDataStyles(sheet);
		
	}

	private void setHeaderStyles(Sheet sheet) {
		
		// Create a Font for styling header cells
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
		
        // Create a CellStyle for the header
        headerCellStyle = sheet.getWorkbook().createCellStyle();
		headerCellStyle.setFont(font);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		
	}
	
	private void setSubHeaderStyles(Sheet sheet) {
		
		// Create a Font for styling sub header cells
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
		
        // Create a CellStyle for the SubHeader text cells
		subHeaderCellStyle = sheet.getWorkbook().createCellStyle();
		subHeaderCellStyle.setFont(font);
		subHeaderCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		subHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		subHeaderCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		
		// Create a CellStyle for the SubHeader currency cells
		subHeaderCurrencyCellStyle = sheet.getWorkbook().createCellStyle();
		subHeaderCurrencyCellStyle.setFont(font);
		subHeaderCurrencyCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		subHeaderCurrencyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		subHeaderCurrencyCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		subHeaderCurrencyCellStyle.setDataFormat(currencyFormat);
		
	}
	
	private void setDataStyles(Sheet sheet) {
		
		// Create a Font for styling data cells
        Font font10Points = sheet.getWorkbook().createFont();
        font10Points.setFontHeightInPoints((short) 10);
        
        // Create a Font for styling the title data cells. 
        Font font8Points = sheet.getWorkbook().createFont();
        font8Points.setFontHeightInPoints((short) 8);
		
        // Create a CellStyle for the text cells
		dataTextCellStyle = sheet.getWorkbook().createCellStyle();
		dataTextCellStyle.setFont(font10Points);
		dataTextCellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		// Create a CellStyle for the title cells
		dataTitleCellStyle = sheet.getWorkbook().createCellStyle();
		dataTitleCellStyle.setFont(font8Points);
		dataTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		
		// Create a CellStyle for the currency cells
		dataCurrencyCellStyle = sheet.getWorkbook().createCellStyle();
		dataCurrencyCellStyle.setFont(font10Points);
		dataCurrencyCellStyle.setAlignment(HorizontalAlignment.CENTER);
		DataFormat currencyFormat = sheet.getWorkbook().createDataFormat();
		dataCurrencyCellStyle.setDataFormat(currencyFormat.getFormat("#,##0.00 [$€-de-DE]"));
		
		// Create a CellStyle for the date cells
		dataDateCellStyle = sheet.getWorkbook().createCellStyle();
		dataDateCellStyle.setFont(font10Points);
		dataDateCellStyle.setAlignment(HorizontalAlignment.CENTER);
		dataDateCellStyle.setDataFormat((short)14);
		
	}

}
