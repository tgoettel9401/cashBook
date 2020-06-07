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

    public List<CashBookEntity> findAll() {
        return cashBookRepository.findAll();
    }

    public CashBookEntity createFromCsv(SparkasseCsv csv) {
        CashBookEntity cashBookEntity = new CashBookEntity();
        cashBookEntity.setAccountNumber(csv.getAccount());
        cashBookEntity.setName(csv.getAccount());
        return this.save(cashBookEntity);
    }

    public CashBookEntity save(CashBookEntity cashBookEntity) {
        return cashBookRepository.save(cashBookEntity);
    }

    public Optional<CashBookEntity> findByAccountNumber(String accountNumber) {
        return cashBookRepository.findByAccountNumber(accountNumber);
    }

}
