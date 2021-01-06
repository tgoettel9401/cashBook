package tobias.chess.cashBook.business.cashBook;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;

import javax.persistence.*;
import java.math.BigDecimal;
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

    private BigDecimal initialWealth = new BigDecimal(0);
    private BigDecimal calculatedInitialWealth = new BigDecimal(0);

    @ToString.Exclude
    @OneToMany(mappedBy = "cashBook", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CashBookEntry> cashBookEntries = Lists.newArrayList();

}
