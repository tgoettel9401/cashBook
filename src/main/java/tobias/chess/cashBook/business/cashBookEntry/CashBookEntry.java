package tobias.chess.cashBook.business.cashBookEntry;

import lombok.Data;
import tobias.chess.cashBook.business.cashBook.CashBook;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class CashBookEntry {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CashBook cashBook;

    private LocalDate bookingDate;
    private LocalDate valueDate;
    private String bookingText;
    private String purpose;
    private String cashPartnerName;
    private String cashPartnerAccountNumber;
    private String cashPartnerBankCode;
    private Double value;

    private LocalDateTime createdAt;

}
