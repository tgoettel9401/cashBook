package tobias.chess.cashBook.business.cashBook;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CashBookNotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CashBookNotFoundException() {
        super("So far no CashBook exists for this request!");
    }
}
