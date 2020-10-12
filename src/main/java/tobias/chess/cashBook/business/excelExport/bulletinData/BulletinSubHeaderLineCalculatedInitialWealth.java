package tobias.chess.cashBook.business.excelExport.bulletinData;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BulletinSubHeaderLineCalculatedInitialWealth {
	private String description; 
	private BigDecimal calculatedInitialWealth; 
}
