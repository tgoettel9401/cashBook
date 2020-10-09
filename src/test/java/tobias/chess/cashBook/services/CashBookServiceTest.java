package tobias.chess.cashBook.services;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tobias.chess.cashBook.business.cashBook.CashBookRepository;
import tobias.chess.cashBook.business.cashBook.CashBookService;
import tobias.chess.cashBook.business.csvImport.SparkasseCsv;

@DataJpaTest
class CashBookServiceTest {

    @InjectMocks
    private CashBookService cashBookService;

    @Mock
    private CashBookRepository cashBookRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void createCashBook_createsCashBook() {
        // Initialize CSV.
        SparkasseCsv csv = new SparkasseCsv();
        String accountName = "DE73828167328";
        csv.setAccount(accountName);

        // Create the CashBook
        cashBookService.createFromCsv(csv);
       
    }

}