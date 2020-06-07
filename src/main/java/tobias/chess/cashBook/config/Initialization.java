package tobias.chess.cashBook.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tobias.chess.cashBook.business.cashBook.CashBookEntity;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryEntity;
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
            List<CashBookEntity> cashBookEntities = getInitialCashBooks();
            List<CashBookEntryEntity> cashBookEntries = getInitialCashBookEntries(cashBookEntities.get(0));
            logger.info("Database-Initialization has been performed. Inserted " + cashBookEntities.size() + " Cash-Books" +
                    "and " + cashBookEntries.size() + " entries (all for Cash-Book " + cashBookEntities.get(0).getName() + ")");
        }

    }

    private List<CashBookEntity> getInitialCashBooks() {
        CashBookEntity cashBookEntity1 = new CashBookEntity();
        cashBookEntity1.setAccountNumber("DE48293837817");
        cashBookEntity1.setName("CashBook-No.1");
        cashBookRepository.save(cashBookEntity1);

        CashBookEntity cashBookEntity2 = new CashBookEntity();
        cashBookEntity2.setAccountNumber("DE85795687954");
        cashBookEntity2.setName("CashBook-No.2");
        cashBookRepository.save(cashBookEntity2);

        return Lists.newArrayList(cashBookEntity1, cashBookEntity2);
    }

    private List<CashBookEntryEntity> getInitialCashBookEntries(CashBookEntity cashBookEntity) {
        CashBookEntryEntity cashBookEntryEntity1 = new CashBookEntryEntity();
        cashBookEntryEntity1.setCashBookEntity(cashBookEntity);
        cashBookEntryEntity1.setBookingDate(LocalDate.of(2020, 6,20));
        cashBookEntryEntity1.setBookingText("This is the booking-text!");
        cashBookEntryEntity1.setValueDate(LocalDate.of(2020,6,17));
        cashBookEntryEntity1.setCashPartnerName("CashPartnerName");
        cashBookEntryEntity1.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntryEntity1.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntryEntity1.setPurpose("Important stuff");
        cashBookEntryEntity1.setValue(17.20);
        cashBookEntryEntity1.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntryEntity1);

        CashBookEntryEntity cashBookEntryEntity2 = new CashBookEntryEntity();
        cashBookEntryEntity2.setCashBookEntity(cashBookEntity);
        cashBookEntryEntity2.setBookingDate(LocalDate.of(2020, 6,20));
        cashBookEntryEntity2.setBookingText("Another text added to the booking");
        cashBookEntryEntity2.setValueDate(LocalDate.of(2020,5,9));
        cashBookEntryEntity2.setCashPartnerName("CashPartnerName");
        cashBookEntryEntity2.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntryEntity2.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntryEntity2.setPurpose("This is much more important than the other one!");
        cashBookEntryEntity2.setValue(39.55);
        cashBookEntryEntity2.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntryEntity2);

        return Lists.newArrayList(cashBookEntryEntity1, cashBookEntryEntity2);
    }
}
