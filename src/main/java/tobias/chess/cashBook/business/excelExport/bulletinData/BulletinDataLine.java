package tobias.chess.cashBook.business.excelExport.bulletinData;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class BulletinDataLine {
	private int position;
	private String incomeExpenseCounter; 
	private String title; 
	private BigDecimal income; 
	private BigDecimal expense; 
	private String receiverSender; 
	private LocalDate date; 
	private String budgetPosition; 
}
