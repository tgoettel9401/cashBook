package tobias.chess.cashBook.business.budgetPosition.point;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntry;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class BudgetPositionPoint {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @ManyToOne
    private BudgetPositionTitle title;

    @ToString.Exclude
    @OneToMany(mappedBy = "point", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BudgetPositionEntry> entries;

}
