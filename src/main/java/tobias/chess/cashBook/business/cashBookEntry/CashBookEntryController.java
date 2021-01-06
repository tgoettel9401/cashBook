package tobias.chess.cashBook.business.cashBookEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookNotFoundException;
import tobias.chess.cashBook.business.cashBook.CashBookService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@BasePathAwareController
public class CashBookEntryController {

    private final Logger logger = LoggerFactory.getLogger(CashBookEntryController.class);

    private CashBookEntryService cashBookEntryService;
    private CashBookService cashBookService;

    @Autowired
    public CashBookEntryController(CashBookEntryService cashBookEntryService, CashBookService cashBookService) {
        this.cashBookEntryService = cashBookEntryService;
        this.cashBookService = cashBookService;
    }

    @GetMapping("cashBooks/{id}/cashBookEntriesForView")
    public List<CashBookEntryDto> getCashBookEntryDtosForCashBookId(@PathVariable("id") Long cashBookId)
            throws CashBookNotFoundException {
        logger.info("Received request on cashBookEntryDtos for CashBookId " + cashBookId);
        // Find CashBook first.
        CashBook cashBook = cashBookService.findById(cashBookId).orElseThrow(CashBookNotFoundException::new);
        // Return all Dtos for this CashBook.
        return cashBookEntryService.createCashBookEntryDtosForCashBook(cashBook);
    }

}
