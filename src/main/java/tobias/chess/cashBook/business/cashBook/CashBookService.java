package tobias.chess.cashBook.business.cashBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.business.csvImport.SparkasseCsv;

import java.util.List;
import java.util.Optional;

@Service
public class CashBookService {

    private final CashBookRepository cashBookRepository;

    @Autowired
    public CashBookService(CashBookRepository cashBookRepository) {
        this.cashBookRepository = cashBookRepository;
    }

    public List<CashBook> findAll() {
        return cashBookRepository.findAll();
    }

    public CashBook createFromCsv(SparkasseCsv csv) {
        CashBook cashBook = new CashBook();
        cashBook.setAccountNumber(csv.getAccount());
        cashBook.setName(csv.getAccount());
        return this.save(cashBook);
    }

    public CashBook save(CashBook cashBook) {
        return cashBookRepository.save(cashBook);
    }

    public Optional<CashBook> findByAccountNumber(String accountNumber) {
        return cashBookRepository.findByAccountNumber(accountNumber);
    }

}
