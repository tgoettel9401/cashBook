package tobias.chess.cashBook.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class CashBookFile {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private LocalDateTime createdAt;

}
