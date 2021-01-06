package tobias.chess.cashBook.business.budgetPosition.point;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;

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

}
