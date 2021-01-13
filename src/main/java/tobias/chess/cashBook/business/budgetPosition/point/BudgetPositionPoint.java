package tobias.chess.cashBook.business.budgetPosition.point;

import lombok.Getter;
import lombok.Setter;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.util.ListToStringConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class BudgetPositionPoint {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @Convert(converter = ListToStringConverter.class)
    private List<String> tags;

    @ManyToOne
    private BudgetPositionTitle title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetPositionPoint)) return false;
        BudgetPositionPoint that = (BudgetPositionPoint) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, title);
    }
}
