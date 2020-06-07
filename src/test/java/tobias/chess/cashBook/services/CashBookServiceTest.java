package tobias.chess.cashBook.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookService;
import tobias.chess.cashBook.business.csvImport.SparkasseCsv;
import tobias.chess.cashBook.business.cashBook.CashBookRepository;

import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

@DataJpaTest
class CashBookServiceTest {

    private final Logger logger = LoggerFactory.getLogger(CashBookServiceTest.class);

    @InjectMocks
    private CashBookService cashBookService;

    @Mock
    private CashBookRepository cashBookRepository;

    private List<SparkasseCsv> csvs;
    private List<CashBook> correctCashBookEntities;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void createCashBook_createsCorrectCashBook() {
        // Initialize CSV.
        SparkasseCsv csv = new SparkasseCsv();
        csv.setAccount("DE73828167328");

        // Create the CashBook
        CashBook cashBook = cashBookService.createFromCsv(csv);
    }

}