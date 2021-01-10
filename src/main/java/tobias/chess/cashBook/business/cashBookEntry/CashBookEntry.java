package tobias.chess.cashBook.business.cashBookEntry;

import lombok.Getter;
import lombok.Setter;
import tobias.chess.cashBook.business.cashBook.CashBook;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
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

    @Embedded
    private CashBookEntryBudgetPosition budgetPosition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashBookEntry)) return false;
        CashBookEntry that = (CashBookEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(cashBook, that.cashBook) && Objects.equals(bookingDate, that.bookingDate) && Objects.equals(valueDate, that.valueDate) && Objects.equals(bookingText, that.bookingText) && Objects.equals(purpose, that.purpose) && Objects.equals(cashPartnerName, that.cashPartnerName) && Objects.equals(cashPartnerAccountNumber, that.cashPartnerAccountNumber) && Objects.equals(cashPartnerBankCode, that.cashPartnerBankCode) && Objects.equals(value, that.value) && Objects.equals(createdAt, that.createdAt) && Objects.equals(budgetPosition, that.budgetPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cashBook, bookingDate, valueDate, bookingText, purpose, cashPartnerName, cashPartnerAccountNumber, cashPartnerBankCode, value, createdAt, budgetPosition);
    }
}
