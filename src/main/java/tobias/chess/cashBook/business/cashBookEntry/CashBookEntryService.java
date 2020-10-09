package tobias.chess.cashBook.business.cashBookEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookService;
import tobias.chess.cashBook.business.csvImport.SparkasseCsv;

@Service
public class CashBookEntryService {

    private final CashBookEntryRepository cashBookEntryRepository;
    private final CashBookService cashBookService;

    @Autowired
    public CashBookEntryService(CashBookEntryRepository cashBookEntryRepository, CashBookService cashBookService) {
        this.cashBookEntryRepository = cashBookEntryRepository;
        this.cashBookService = cashBookService;
    }

    /**
     * Creates a DTO for CSV-communication for a given CashBookEntry.
     *
     * @param cashBookEntry: Entry to convert to CSV
     * @return CashBookEntryCsv
     */
    public SparkasseCsv createCsvFromCashBookEntry(CashBookEntry cashBookEntry) {
        SparkasseCsv sparkasseCsv = new SparkasseCsv();
        sparkasseCsv.setBookingDate(cashBookEntry.getBookingDate());
        sparkasseCsv.setValueDate(cashBookEntry.getValueDate());
        sparkasseCsv.setBookingText(cashBookEntry.getBookingText());
        sparkasseCsv.setPurpose(cashBookEntry.getPurpose());
        sparkasseCsv.setCashPartnerName(cashBookEntry.getCashPartnerName());
        sparkasseCsv.setCashPartnerAccountNumber(cashBookEntry.getCashPartnerAccountNumber());
        sparkasseCsv.setCashPartnerBankCode(cashBookEntry.getCashPartnerBankCode());
        sparkasseCsv.setValue(cashBookEntry.getValue().doubleValue());
        return sparkasseCsv;
    }

    public List<SparkasseCsv> filterCsvsToAdd(List<SparkasseCsv> csvs) {

        // Get the already existing Entries from Database and transform them to CSVs.
        List<CashBookEntry> cashBookEntriesFromDatabase = findAll();
        List<SparkasseCsv> csvsFromDatabase = transformCashBookEntriesToCsvs(cashBookEntriesFromDatabase);

        // Filter the CSVs that do not already exist and return only them.
        List<SparkasseCsv> tempCsvs = Lists.newArrayList(csvs);
        return tempCsvs.stream().filter(csv -> !csvsFromDatabase.contains(csv)).collect(Collectors.toList());
    }

    public CashBookEntry createCashBookEntryFromCsv(SparkasseCsv csvEntry) {

        // First find the CashBook for the csv
        CashBook cashBook;
        Optional<CashBook> cashBookOptional = cashBookService.findByAccountNumber(csvEntry.getAccount());

        // Either get the already existing CashBook or create a new one.
        cashBook = cashBookOptional
                .orElseGet(() -> cashBookService.createFromCsv(csvEntry));

        // Remove the SVWZ+ from the purpose.
        if (csvEntry.getPurpose().startsWith("SVWZ+")) {
            csvEntry.setPurpose(csvEntry.getPurpose().replace("SVWZ+", ""));
        }

        // Add the CashBookEntry to the CashBook.
        CashBookEntry cashBookEntry = new CashBookEntry();
        cashBookEntry.setCashBook(cashBook);
        cashBookEntry.setBookingDate(csvEntry.getBookingDate());
        cashBookEntry.setValueDate(csvEntry.getValueDate());
        cashBookEntry.setBookingText(csvEntry.getBookingText());
        cashBookEntry.setPurpose(csvEntry.getPurpose());
        cashBookEntry.setCashPartnerName(csvEntry.getCashPartnerName());
        cashBookEntry.setCashPartnerAccountNumber(csvEntry.getCashPartnerAccountNumber());
        cashBookEntry.setCashPartnerBankCode(csvEntry.getCashPartnerBankCode());
        cashBookEntry.setValue(new BigDecimal(csvEntry.getValue()));
        cashBookEntry.setCreatedAt(LocalDateTime.now());
        return cashBookEntry;
    }

    public List<CashBookEntryDto> createCashBookEntryDtosForCashBook(CashBook cashBook) {

        // First find all CashBookEntries for this CashBook
        List<CashBookEntry> cashBookEntries = findAllByCashBook(cashBook);

        // Sort the CashBookEntries by valueDate first, then bookingDate and finally descending ID (descending because
        // the original CashBookEntries have been delivered in incorrect order.
        cashBookEntries.sort(Comparator
                .comparing(CashBookEntry::getValueDate)
                .thenComparing(CashBookEntry::getBookingDate)
                .thenComparing(CashBookEntry::getId)
        );

        // Create position-sequence as well as income-sequence and expense-sequence.
        List<CashBookEntryDto> dtos = Lists.newArrayList();
        int positionNumber = 1;
        int incomeNumber = 1;
        int expenseNumber = 1;
        for (CashBookEntry entry : cashBookEntries) {
            CashBookEntryDto dto = new CashBookEntryDto();
            dto.setId(entry.getId());
            dto.setTitle(entry.getPurpose());
            dto.setValueDate(entry.getValueDate());
            if (entry.getValue().doubleValue() >= 0) { // Counts as Income.
                dto.setIncome(entry.getValue().abs());
                dto.setExpense(new BigDecimal(0.00));
                dto.setIncomeExpensePosition("E" + Strings.padStart(Integer.toString(incomeNumber), 3, '0'));
                incomeNumber++;
            }
            else { // Counts as Expense.
                dto.setIncome(new BigDecimal(0.00));
                dto.setExpense(entry.getValue().abs());
                dto.setIncomeExpensePosition("A" + Strings.padStart(Integer.toString(expenseNumber), 3, '0'));
                expenseNumber++;
            }
            dto.setPosition(positionNumber);
            dto.setReceiverSender(entry.getCashPartnerName());
            positionNumber++;
            dtos.add(dto);
        }

        return dtos;

    }

    private List<CashBookEntry> findAllByCashBook(CashBook cashBook) {
        return cashBookEntryRepository.findAllByCashBook(cashBook);
    }

    @Transactional
    public List<CashBookEntry> transformCsvsToCashBookEntries(List<SparkasseCsv> csvs) {
        List<CashBookEntry> cashBookEntries = Lists.newArrayList();
        csvs.forEach(csv -> cashBookEntries.add(createCashBookEntryFromCsv(csv)));
        return cashBookEntries;
    }

    public List<SparkasseCsv> transformCashBookEntriesToCsvs(List<CashBookEntry> entries) {
        List<SparkasseCsv> csvs = Lists.newArrayList();
        entries.forEach(entry -> csvs.add(createCsvFromCashBookEntry(entry)));
        return csvs;
    }

    public List<CashBookEntry> findAll() {
        return cashBookEntryRepository.findAll();
    }

    public List<CashBookEntry> saveAll(List<CashBookEntry> cashBookEntries) {
        return cashBookEntryRepository.saveAll(cashBookEntries);
    }
}
