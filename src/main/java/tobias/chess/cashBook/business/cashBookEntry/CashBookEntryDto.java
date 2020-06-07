package tobias.chess.cashBook.business.cashBookEntry;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CashBookEntryDto {

    private Long id;
    private Integer position;
    private String incomeExpensePosition;
    private String title;
    private Double income;
    private Double expense;
    private String receiverSender;
    private LocalDate valueDate;

}
