package tobias.chess.cashBook.business.cashBook;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CashBookDto {
    private Long id;
    private String accountNumber = "";
    private String name = "";
    private BigDecimal initialWealth = new BigDecimal(0);
    private BigDecimal calculatedInitialWealth = new BigDecimal(0);
    private BigDecimal finalWealth = new BigDecimal(0);
    private BigDecimal calculatedFinalWealth = new BigDecimal(0);
    private BigDecimal result = new BigDecimal(0);
    private BigDecimal calculatedResult = new BigDecimal(0);
}
