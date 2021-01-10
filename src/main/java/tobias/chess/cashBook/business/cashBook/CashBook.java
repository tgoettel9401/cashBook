package tobias.chess.cashBook.business.cashBook;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CashBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String name;

    private BigDecimal initialWealth = new BigDecimal(0);
    private BigDecimal calculatedInitialWealth = new BigDecimal(0);

    @ToString.Exclude
    @OneToMany(mappedBy = "cashBook", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CashBookEntry> cashBookEntries = Lists.newArrayList();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashBook)) return false;
        CashBook cashBook = (CashBook) o;
        return getId().equals(cashBook.getId()) && Objects.equals(getAccountNumber(), cashBook.getAccountNumber()) && Objects.equals(getName(), cashBook.getName()) && Objects.equals(getInitialWealth(), cashBook.getInitialWealth()) && Objects.equals(getCalculatedInitialWealth(), cashBook.getCalculatedInitialWealth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAccountNumber(), getName(), getInitialWealth(), getCalculatedInitialWealth());
    }
}
