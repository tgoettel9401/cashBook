package tobias.chess.cashBook.business.cashBook;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CashBookEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String accountNumber;

    private String name;

    @ToString.Exclude
    @OneToMany (mappedBy = "cashBook")
    private List<CashBookEntryEntity> cashBookEntries;

}
