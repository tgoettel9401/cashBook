package tobias.chess.cashBook.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class CashBookFile {

    @Id
    @GeneratedValue (generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private String fileName;

    private LocalDateTime createdAt;

}
