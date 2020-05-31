package tobias.chess.cashBook.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class CashBookEntry {

    @Id
    @GeneratedValue (generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

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
