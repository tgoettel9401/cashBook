package tobias.chess.cashBook.business.csvImport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleCashBooksException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4354345582023569491L;

	public MultipleCashBooksException() {
        super("The uploaded file consists of multiple account-numbers. Please insert a correct file that only contains a single CashBook!");
    }
}
