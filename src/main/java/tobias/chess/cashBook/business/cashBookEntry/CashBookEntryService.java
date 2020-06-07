package tobias.chess.cashBook.business.cashBookEntry;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.chess.cashBook.business.cashBook.CashBookEntity;
import tobias.chess.cashBook.business.csvImport.SparkasseCsv;
import tobias.chess.cashBook.business.cashBook.CashBookService;

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
     * @param cashBookEntryEntity: Entry to convert to CSV
     * @return CashBookEntryCsv
     */
    public SparkasseCsv createCsvFromCashBookEntry(CashBookEntryEntity cashBookEntryEntity) {
        SparkasseCsv sparkasseCsv = new SparkasseCsv();
        sparkasseCsv.setBookingDate(cashBookEntryEntity.getBookingDate());
        sparkasseCsv.setValueDate(cashBookEntryEntity.getValueDate());
        sparkasseCsv.setBookingText(cashBookEntryEntity.getBookingText());
        sparkasseCsv.setPurpose(cashBookEntryEntity.getPurpose());
        sparkasseCsv.setCashPartnerName(cashBookEntryEntity.getCashPartnerName());
        sparkasseCsv.setCashPartnerAccountNumber(cashBookEntryEntity.getCashPartnerAccountNumber());
        sparkasseCsv.setCashPartnerBankCode(cashBookEntryEntity.getCashPartnerBankCode());
        sparkasseCsv.setValue(cashBookEntryEntity.getValue());
        return sparkasseCsv;
    }

    public List<SparkasseCsv> filterCsvsToAdd(List<SparkasseCsv> csvs) {

        // Get the already existing Entries from Database and transform them to CSVs.
        List<CashBookEntryEntity> cashBookEntriesFromDatabase = findAll();
        List<SparkasseCsv> csvsFromDatabase = transformCashBookEntriesToCsvs(cashBookEntriesFromDatabase);

        // Filter the CSVs that do not already exist and return only them.
        List<SparkasseCsv> tempCsvs = Lists.newArrayList(csvs);
        return tempCsvs.stream().filter(csv -> !csvsFromDatabase.contains(csv)).collect(Collectors.toList());
    }

    public CashBookEntryEntity createCashBookEntryFromCsv(SparkasseCsv csvEntry) {

        // First find the CashBook for the csv
        CashBookEntity cashBookEntity;
        Optional<CashBookEntity> cashBookOptional = cashBookService.findByAccountNumber(csvEntry.getAccount());

        // Either get the already existing CashBook or create a new one.
        cashBookEntity = cashBookOptional
                .orElseGet(() -> cashBookService.createFromCsv(csvEntry));

        // Add the CashBookEntry to the CashBook.
        CashBookEntryEntity cashBookEntryEntity = new CashBookEntryEntity();
        cashBookEntryEntity.setCashBookEntity(cashBookEntity);
        cashBookEntryEntity.setBookingDate(csvEntry.getBookingDate());
        cashBookEntryEntity.setValueDate(csvEntry.getValueDate());
        cashBookEntryEntity.setBookingText(csvEntry.getBookingText());
        cashBookEntryEntity.setPurpose(csvEntry.getPurpose());
        cashBookEntryEntity.setCashPartnerName(csvEntry.getCashPartnerName());
        cashBookEntryEntity.setCashPartnerAccountNumber(csvEntry.getCashPartnerAccountNumber());
        cashBookEntryEntity.setCashPartnerBankCode(csvEntry.getCashPartnerBankCode());
        cashBookEntryEntity.setValue(csvEntry.getValue());
        cashBookEntryEntity.setCreatedAt(LocalDateTime.now());
        return cashBookEntryEntity;
    }

    @Transactional
    public List<CashBookEntryEntity> transformCsvsToCashBookEntries(List<SparkasseCsv> csvs) {
        List<CashBookEntryEntity> cashBookEntries = Lists.newArrayList();
        csvs.forEach(csv -> cashBookEntries.add(createCashBookEntryFromCsv(csv)));
        return cashBookEntries;
    }

    public List<SparkasseCsv> transformCashBookEntriesToCsvs(List<CashBookEntryEntity> entries) {
        List<SparkasseCsv> csvs = Lists.newArrayList();
        entries.forEach(entry -> csvs.add(createCsvFromCashBookEntry(entry)));
        return csvs;
    }

    public List<CashBookEntryEntity> findAll() {
        return cashBookEntryRepository.findAll();
    }

    public List<CashBookEntryEntity> saveAll(List<CashBookEntryEntity> cashBookEntries) {
        return cashBookEntryRepository.saveAll(cashBookEntries);
    }
}
