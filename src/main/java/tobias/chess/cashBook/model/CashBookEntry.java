package tobias.chess.cashBook.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class CashBookEntry {

    @Id
    @GeneratedValue
    private Long id;

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
