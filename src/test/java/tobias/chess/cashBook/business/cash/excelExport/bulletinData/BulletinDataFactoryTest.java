package tobias.chess.cashBook.business.cash.excelExport.bulletinData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinData;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinDataFactory;
import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinHeaderLine;

public class BulletinDataFactoryTest {
	
	@InjectMocks
	private BulletinDataFactory bulletinDataFactory;

	@Mock
	private CashBookEntryService cashBookEntryService;

	@BeforeEach
	void setUp() {
		initMocks(this);
	}
	
	@Test
	public void testCreateData() {
		
		CashBook cashBook = mock(CashBook.class);
		
		BulletinData bulletinData = bulletinDataFactory.createData(cashBook);
		
		BulletinHeaderLine headerLine = bulletinData.getHeaderLine();
		assertThat(headerLine.getPosition()).isEqualTo("Position");
		assertThat(headerLine.getIncomeExpenseCounter()).isEqualTo("A/E");
		assertThat(headerLine.getTitle()).isEqualTo("Titel");
		assertThat(headerLine.getIncome()).isEqualTo("Einnahmen");
		assertThat(headerLine.getExpense()).isEqualTo("Ausgaben");
		assertThat(headerLine.getReceiverSender()).isEqualTo("Empfänger / Einzahler");
		assertThat(headerLine.getBudgetPosition()).isEqualTo("Haushaltsposten");
		
	}
	
}
