package tobias.chess.cashBook.business.csvImport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (HttpStatus.BAD_REQUEST)
public class EmptyFileException extends Exception {
    public EmptyFileException() {
        super("Uploading an empty file is not allowed. Please replace by a correct file.");
    }
}
