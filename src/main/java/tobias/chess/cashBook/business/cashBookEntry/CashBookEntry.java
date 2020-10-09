package tobias.chess.cashBook.business.cashBookEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import tobias.chess.cashBook.business.cashBook.CashBook;

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
    private BigDecimal value = new BigDecimal(0);

    private LocalDateTime createdAt;

}
