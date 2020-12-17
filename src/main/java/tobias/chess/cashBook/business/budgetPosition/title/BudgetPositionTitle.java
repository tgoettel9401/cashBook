package tobias.chess.cashBook.business.budgetPosition.title;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class BudgetPositionTitle {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @ManyToOne
    private BudgetPositionHeader header;

    @ToString.Exclude
    @OneToMany(mappedBy = "title", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BudgetPositionPoint> points;

}
