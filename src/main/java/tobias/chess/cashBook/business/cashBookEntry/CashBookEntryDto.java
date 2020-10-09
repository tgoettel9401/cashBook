package tobias.chess.cashBook.business.cashBookEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

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

}
