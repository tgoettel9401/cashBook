package tobias.chess.cashBook.business.cashBook;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
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

    public CashBook save(CashBookDto cashBookDto) {
        Optional<CashBook> cashBookOptional = findById(cashBookDto.getId());
        CashBook cashBook;
        if (cashBookOptional.isEmpty()) {
            cashBook = new CashBook();
        }
        else {
            cashBook = cashBookOptional.get();
        }
        cashBook.setAccountNumber(cashBookDto.getAccountNumber());
        cashBook.setName(cashBookDto.getName());
        cashBook.setInitialWealth(cashBookDto.getInitialWealth());
        cashBook.setCalculatedInitialWealth(cashBookDto.getCalculatedInitialWealth());
        return this.save(cashBook);
    }

    public void delete(CashBookDto cashBookDto) {
        Optional<CashBook> cashBookOptional = findById(cashBookDto.getId());
        cashBookOptional.ifPresent(cashBook -> delete(cashBook));
    }

    public void delete(CashBook cashBook) {
        cashBookRepository.delete(cashBook);
    }

    public Optional<CashBook> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return cashBookRepository.findById(id);
    }

    public Optional<CashBook> findByAccountNumber(String accountNumber) {
        return cashBookRepository.findByAccountNumber(accountNumber);
    }

}
