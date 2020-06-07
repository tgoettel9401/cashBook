package tobias.chess.cashBook.services;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.chess.cashBook.csvImport.SparkasseCsv;
import tobias.chess.cashBook.exception.CashBookNotFoundException;
import tobias.chess.cashBook.model.CashBook;
import tobias.chess.cashBook.model.CashBookEntry;
import tobias.chess.cashBook.repository.CashBookEntryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        sparkasseCsv.setValue(cashBookEntry.getValue());
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
        cashBookEntry.setValue(csvEntry.getValue());
        cashBookEntry.setCreatedAt(LocalDateTime.now());
        return cashBookEntry;
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
