package tobias.chess.cashBook.business.cashBook;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CashBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String name;

    private Double initialWealth;
    private Double calculatedInitialWealth;

    @ToString.Exclude
    @OneToMany(mappedBy = "cashBook")
    private List<CashBookEntry> cashBookEntries;

    /**
     * Calculates the final wealth of the current year.
     * @return Double
     */
    public Double getFinalWealth() {
        Double wealth = initialWealth;
        for (CashBookEntry entry : cashBookEntries) {
            wealth += entry.getValue();
        }
        return wealth;
    }

    /**
     * Calculates the calculated final wealth of the current year.
     * @return Double
     */
    public Double getCalculatedFinalWealth() {
        return 0.0;
    }

}
