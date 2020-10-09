package tobias.chess.cashBook.business.cashBook;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;

@Entity
@Data
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
    @OneToMany(mappedBy = "cashBook")
    private List<CashBookEntry> cashBookEntries = Lists.newArrayList();

    /**
     * Calculates the final wealth of the current year.
     * @return Double
     */
    public BigDecimal getFinalWealth() {
    	BigDecimal wealth = initialWealth;
        for (CashBookEntry entry : cashBookEntries) {
            wealth = wealth.add(entry.getValue());
        }
        return wealth;
    }

    /**
     * Calculates the calculated final wealth of the current year.
     * @return Double
     */
    public BigDecimal getCalculatedFinalWealth() {
        return new BigDecimal(0);
    }

    /**
     * Calculates the result of the current year.
     * @return Double
     */
    public BigDecimal getResult() {
        return getFinalWealth().subtract(getInitialWealth());
    }

    /**
     * Calculates the calculated result of the current year.
     * @return Double
     */
    public BigDecimal getCalculatedResult() {
        return getCalculatedFinalWealth().subtract(getCalculatedInitialWealth());
    }

}
