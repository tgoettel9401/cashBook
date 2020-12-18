package tobias.chess.cashBook.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntry;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntryRepository;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeaderRepository;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPointRepository;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitleRepository;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookRepository;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryRepository;

@Component
public class Initialization implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(Initialization.class);

    @Autowired private Environment environment;

    @Autowired private CashBookRepository cashBookRepository;
    @Autowired private CashBookEntryRepository cashBookEntryRepository;
    @Autowired private BudgetPositionHeaderRepository budgetPositionHeaderRepository;
    @Autowired private BudgetPositionTitleRepository budgetPositionTitleRepository;
    @Autowired private BudgetPositionPointRepository budgetPositionPointRepository;
    @Autowired private BudgetPositionEntryRepository budgetPositionEntryRepository;

    @Autowired private BudgetPositionService budgetPositionService;

    @Override
    @Transactional
    public void afterPropertiesSet() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

        if (activeProfiles.contains("db-init")) {
            List<CashBook> cashBookEntities = getInitialCashBooks();
            List<CashBookEntry> cashBookEntries = getInitialCashBookEntries(cashBookEntities.get(0));
            List<BudgetPosition> budgetPositions = getInitialBudgetPositions(cashBookEntities.get(0));
            addBudgetPositionsToCashBookEntries(cashBookEntries, budgetPositions);
            logger.info(
                    "Database-Initialization has been performed. Inserted "
                            + cashBookEntities.size()
                            + " Cash-Books and "
                            + cashBookEntries.size()
                            + " entries (all for Cash-Book "
                            + cashBookEntities.get(0).getName()
                            + ")"
                            + " and "
                            + budgetPositions.size()
                            + " Budget-Positions"
            );
        }
    }

    private List<CashBook> getInitialCashBooks() {
        CashBook cashBook1 = new CashBook();
        cashBook1.setAccountNumber("DE48293837817");
        cashBook1.setName("CashBook-No.1");
        cashBook1.setInitialWealth(new BigDecimal("738.99"));
        cashBook1.setCalculatedInitialWealth(new BigDecimal("287.10"));
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
        cashBookEntry1.setBookingDate(LocalDate.of(2020, 6, 20));
        cashBookEntry1.setBookingText("This is the booking-text!");
        cashBookEntry1.setValueDate(LocalDate.of(2020, 6, 17));
        cashBookEntry1.setCashPartnerName("CashPartnerName");
        cashBookEntry1.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntry1.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntry1.setPurpose("Important stuff");
        cashBookEntry1.setValue(new BigDecimal("17.20"));
        cashBookEntry1.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntry1);

        CashBookEntry cashBookEntry2 = new CashBookEntry();
        cashBookEntry2.setCashBook(cashBook);
        cashBookEntry2.setBookingDate(LocalDate.of(2020, 6, 20));
        cashBookEntry2.setBookingText("Another text added to the booking");
        cashBookEntry2.setValueDate(LocalDate.of(2020, 5, 9));
        cashBookEntry2.setCashPartnerName("CashPartnerName");
        cashBookEntry2.setCashPartnerAccountNumber("DE83912938347");
        cashBookEntry2.setCashPartnerBankCode("GENODE761DZE");
        cashBookEntry2.setPurpose("This is much more important than the other one!");
        cashBookEntry2.setValue(new BigDecimal("39.55"));
        cashBookEntry2.setCreatedAt(LocalDateTime.now());
        cashBookEntryRepository.save(cashBookEntry2);

        return Lists.newArrayList(cashBookEntry1, cashBookEntry2);
    }

    private List<BudgetPosition> getInitialBudgetPositions(CashBook cashBook) {

        BudgetPositionHeader header = new BudgetPositionHeader();
        header.setCashBook(cashBook);
        header.setName("FIRSTHEADER");
        header.setPosition(1);
        header = budgetPositionHeaderRepository.save(header);

        BudgetPositionTitle title = new BudgetPositionTitle();
        title.setName("TITLEONE");
        title.setPosition(1);
        title.setHeader(header);
        title = budgetPositionTitleRepository.save(title);

        BudgetPositionPoint point = new BudgetPositionPoint();
        point.setName("POINTONE");
        point.setPosition(1);
        point.setTitle(title);
        point = budgetPositionPointRepository.save(point);

        BudgetPositionEntry entryOne = new BudgetPositionEntry();
        entryOne.setName("ENTRYONE");
        entryOne.setPosition(1);
        entryOne.setPoint(point);
        entryOne = budgetPositionEntryRepository.save(entryOne);

        BudgetPositionEntry entryTwo = new BudgetPositionEntry();
        entryTwo.setName("ENTRYTWO");
        entryTwo.setPosition(2);
        entryTwo.setPoint(point);
        entryTwo = budgetPositionEntryRepository.save(entryTwo);

        BudgetPosition budgetPositionOne = new BudgetPosition();
        budgetPositionOne.setHeader(header);
        budgetPositionOne.setTitle(title);
        budgetPositionOne.setPoint(point);
        budgetPositionOne.setEntry(entryOne);

        BudgetPosition budgetPositionTwo = new BudgetPosition();
        budgetPositionTwo.setHeader(header);
        budgetPositionTwo.setTitle(title);
        budgetPositionTwo.setPoint(point);
        budgetPositionTwo.setEntry(entryTwo);

        return Lists.newArrayList(budgetPositionOne, budgetPositionTwo);

    }

    private void addBudgetPositionsToCashBookEntries(List<CashBookEntry> cashBookEntries, List<BudgetPosition> budgetPositions) {
        for (CashBookEntry entry : cashBookEntries) {
            entry.setBudgetPosition(budgetPositionService.createCashBookEntryBudgetPosition(budgetPositions.get(0)));
            cashBookEntryRepository.save(entry);
        }
    }

}
