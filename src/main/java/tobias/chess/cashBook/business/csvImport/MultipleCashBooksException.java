package tobias.chess.cashBook.business.csvImport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleCashBooksException extends Exception {
    public MultipleCashBooksException() {
        super("The uploaded file consists of multiple account-numbers. Please insert a correct file that only contains a single CashBook!");
    }
}
