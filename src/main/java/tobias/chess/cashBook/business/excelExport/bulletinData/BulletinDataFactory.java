package tobias.chess.cashBook.business.excelExport.bulletinData;

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryDto;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

@Service
public class BulletinDataFactory {
	
	private final CashBookEntryService cashBookEntryService;
	
	public BulletinDataFactory(CashBookEntryService cashBookEntryService) {
		this.cashBookEntryService = cashBookEntryService;
	}
	
	public BulletinData createData(CashBook cashBook) {
		
		// HeaderLine
		BulletinHeaderLine headerLine = createHeaderLine();
		
		// SubHeaderLineInitialWealth and SubHeaderLineCalculatedInitialWealth
		BulletinSubHeaderLineInitialWealth initialWealthLine = createInitialWealthLine(cashBook);
		BulletinSubHeaderLineCalculatedInitialWealth calculatedInitialWealthLine = createCalculatedInitialWealthLine(cashBook); 
		
		// DataLines
		List<BulletinDataLine> dataLines = createDataLines(cashBook);
		
		// BulletinData
		BulletinData bulletinData = new BulletinData(); 
		bulletinData.setHeaderLine(headerLine);
		bulletinData.setSubHeaderLineInitialWealth(initialWealthLine);
		bulletinData.setSubHeaderLineCalculatedInitialWealth(calculatedInitialWealthLine);
		bulletinData.setDataLines(dataLines);
		
		return bulletinData; 
	}
	
	private BulletinHeaderLine createHeaderLine() {
		BulletinHeaderLine headerLine = new BulletinHeaderLine(); 
		headerLine.setPosition("Position");
		headerLine.setIncomeExpenseCounter("A/E");
		headerLine.setTitle("Titel");
		headerLine.setIncome("Einnahmen");
		headerLine.setExpense("Ausgaben");
		headerLine.setDate("Datum");
		headerLine.setReceiverSender("Empfänger / Einzahler");
		headerLine.setBudgetPosition("Haushaltsposten");
		return headerLine; 
	}
	
	private BulletinSubHeaderLineInitialWealth createInitialWealthLine(CashBook cashBook) {
		BulletinSubHeaderLineInitialWealth line = new BulletinSubHeaderLineInitialWealth();
		line.setDescription("Ist-Vermögen (01.01.2020):");
		line.setInitialWealth(cashBook.getInitialWealth());
		return line;
	}
	
	private BulletinSubHeaderLineCalculatedInitialWealth createCalculatedInitialWealthLine(CashBook cashBook) {
		BulletinSubHeaderLineCalculatedInitialWealth line = new BulletinSubHeaderLineCalculatedInitialWealth();
		line.setDescription("Kalkulatorischer Vermögensstand (01.01.2020):");
		line.setCalculatedInitialWealth(cashBook.getCalculatedInitialWealth());
		return line;
	}
	
	private List<BulletinDataLine> createDataLines(CashBook cashBook) {
		List<BulletinDataLine> dataLines = Lists.newArrayList();
		
		List<CashBookEntryDto> cashBookEntries = cashBookEntryService.createCashBookEntryDtosForCashBook(cashBook);
		
		for (CashBookEntryDto entry : cashBookEntries) {
			BulletinDataLine dataLine = new BulletinDataLine(); 
			dataLine.setPosition(entry.getPosition());
			dataLine.setIncomeExpenseCounter(entry.getIncomeExpensePosition());
			dataLine.setTitle(entry.getTitle());
			dataLine.setIncome(entry.getIncome());
			dataLine.setExpense(entry.getExpense());
			dataLine.setIncome(entry.getIncome());
			dataLine.setReceiverSender(entry.getReceiverSender());
			dataLine.setDate(entry.getValueDate());
			dataLine.setBudgetPosition(null);
			dataLines.add(dataLine);
		}
		
		return dataLines; 
	}
	
}
