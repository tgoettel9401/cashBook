package tobias.chess.cashBook.business.budgetPosition.entry;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class BudgetPositionEntry {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @ManyToOne
    private BudgetPositionPoint point;


}
