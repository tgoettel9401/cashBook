package tobias.chess.cashBook.business.cashBookEntry;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CashBookEntryDto {

    private Long id;
    private Integer position;
    private String incomeExpensePosition;
    private String title;
    private BigDecimal income;
    private BigDecimal expense;
    private String receiverSender;
    private LocalDate valueDate;

    private CashBookEntry entry;
    private BudgetPosition budgetPosition;

}
