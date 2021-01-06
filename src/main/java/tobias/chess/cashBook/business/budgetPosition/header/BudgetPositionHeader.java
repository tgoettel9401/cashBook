package tobias.chess.cashBook.business.budgetPosition.header;

import lombok.Data;
import lombok.ToString;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBook;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class BudgetPositionHeader {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @ManyToOne
    private CashBook cashBook;

    @ToString.Exclude
    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BudgetPositionTitle> titles;

}
