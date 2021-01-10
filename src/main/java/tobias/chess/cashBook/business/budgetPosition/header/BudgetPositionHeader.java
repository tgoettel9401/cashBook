package tobias.chess.cashBook.business.budgetPosition.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBook;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetPositionHeader)) return false;
        BudgetPositionHeader that = (BudgetPositionHeader) o;
        return getId().equals(that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getCashBook(), that.getCashBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPosition(), getCashBook());
    }
}
