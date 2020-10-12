package tobias.chess.cashBook.business.excelExport.bulletinData;

import lombok.Data;

@Data
public class BulletinHeaderLine {
	private String position;
	private String incomeExpenseCounter; 
	private String title; 
	private String income; 
	private String expense; 
	private String receiverSender; 
	private String date; 
	private String budgetPosition; 
}
