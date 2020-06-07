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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String name;

    @ToString.Exclude
    @OneToMany (mappedBy = "cashBook")
    private List<CashBookEntry> cashBookEntries;

}
