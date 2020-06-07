package tobias.chess.cashBook.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryRepository;
import tobias.chess.cashBook.business.cashBook.CashBookRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class Initialization implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(Initialization.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CashBookRepository cashBookRepository;

    @Autowired
    private CashBookEntryRepository cashBookEntryRepository;

    @Override
    @Transactional
    public void afterPropertiesSet() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

        if (activeProfiles.contains("db-init")) {
            List<CashBook> cashBookEntities = getInitialCashBooks();
            List<CashBookEntry> cashBookEntries = getInitialCashBookEntries(cashBookEntities.get(0));
            logger.info("Database-Initialization has been performed. Inserted " + cashBookEntities.size() + " Cash-Books" +
                    "and " + cashBookEntries.size() + " entries (all for Cash-Book " + cashBookEntities.get(0).getName() + ")");
        }

    }

    private List<CashBook> getInitialCashBooks() {
        CashBook cashBook1 = new CashBook();
        cashBook1.setAccountNumber("DE48293837817");
        cashBook1.setName("CashBook-No.1");
        cashBookRepository.save(cashBook1);

        CashBook cashBook2 = new CashBook();
        cashBook2.setAccountNumber("DE85795687954");
        cashBook2.setName("CashBook-No.2");
        cashBookRepository.save(cashBook2);

        return Lists.newArrayList(cashBook1, cashBook2);
    }

    private List<CashBookEntry> getInitialCashBookEntries(CashBook cashBook) {
        CashBookEntry cashBookEntry1 = new CashBookEntry();
        cashBookEntry1.setCashBook(cashBook);
        cashBookEntry1.setBookingDate(LocalDate.of(2020, 6,20));
        cashBookEntry1.setBookingText("This is the booking-text!");
        cashBookEntry1.setValueDate(LocalDate.of(2020,6,17));
        cashBookEntry1.setCashPartnerName("CashPartnerName");
        cashBookEntry1.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntry1.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntry1.setPurpose("Important stuff");
        cashBookEntry1.setValue(17.20);
        cashBookEntry1.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntry1);

        CashBookEntry cashBookEntry2 = new CashBookEntry();
        cashBookEntry2.setCashBook(cashBook);
        cashBookEntry2.setBookingDate(LocalDate.of(2020, 6,20));
        cashBookEntry2.setBookingText("Another text added to the booking");
        cashBookEntry2.setValueDate(LocalDate.of(2020,5,9));
        cashBookEntry2.setCashPartnerName("CashPartnerName");
        cashBookEntry2.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntry2.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntry2.setPurpose("This is much more important than the other one!");
        cashBookEntry2.setValue(39.55);
        cashBookEntry2.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntry2);

        return Lists.newArrayList(cashBookEntry1, cashBookEntry2);
    }
}
