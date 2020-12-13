package tobias.chess.cashBook.business.excelExport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookNotFoundException;
import tobias.chess.cashBook.business.cashBook.CashBookService;

@RestController
@BasePathAwareController
public class ExcelExportController {
	
	private final ExcelExportService excelExportService;
	private final CashBookService cashBookService;
	
	private final String EXPORT_FILENAME = "cashBookExport.xlsx";
	
	public ExcelExportController(ExcelExportService excelExportService, CashBookService cashBookService) {
		this.excelExportService = excelExportService;
		this.cashBookService = cashBookService;
	}
	
	@GetMapping("cashBooks/{id}/excelExport")
	public ResponseEntity<ByteArrayResource> createExcelExport(@PathVariable("id") long cashBookId) throws IOException, CashBookNotFoundException {
		
		CashBook cashBook = cashBookService.findById(cashBookId).orElseThrow(() -> new CashBookNotFoundException());
		
		Workbook workbook = excelExportService.generateCashBookWorkbook(cashBook);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		workbook.write(stream);
		
		ByteArrayResource resource = new ByteArrayResource(stream.toByteArray());
			
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header("Content-Disposition", "attachment; filename=" + EXPORT_FILENAME)
				.body(resource);
	}

}
