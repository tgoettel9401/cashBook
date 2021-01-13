package tobias.chess.cashBook.business.budgetPosition.title;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.util.ListToStringConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class BudgetPositionTitle {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer position;

    @ManyToOne
    private BudgetPositionHeader header;

    @Convert(converter = ListToStringConverter.class)
    private List<String> tags;

    @ToString.Exclude
    @OneToMany(mappedBy = "title", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BudgetPositionPoint> points;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetPositionTitle)) return false;
        BudgetPositionTitle that = (BudgetPositionTitle) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, header);
    }
}
