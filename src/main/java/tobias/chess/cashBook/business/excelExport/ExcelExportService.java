package tobias.chess.cashBook.business.excelExport;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import tobias.chess.cashBook.business.cashBook.CashBook;

@Service
public class ExcelExportService {
	
	private final BulletinSheetService bulletinSheetService;
	
	public ExcelExportService(BulletinSheetService bulletinSheetService) {
		this.bulletinSheetService = bulletinSheetService;
	}

	public Workbook generateCashBookWorkbook(CashBook cashBook) {
		
		Workbook workbook = new XSSFWorkbook();
		
		bulletinSheetService.generateSheet(workbook, cashBook);
		generateBudgetSheet(workbook, cashBook);
		
		return workbook;
	}
	
	private void generateBudgetSheet(Workbook workbook, CashBook cashBook) {
		workbook.createSheet("Budget");
	}

}
