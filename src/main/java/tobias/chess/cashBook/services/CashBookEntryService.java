package tobias.chess.cashBook.services;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.csvImport.CashBookEntryCsv;
import tobias.chess.cashBook.model.CashBookEntry;
import tobias.chess.cashBook.repository.CashBookEntryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashBookEntryService {

    private final CashBookEntryRepository cashBookEntryRepository;

    @Autowired
    public CashBookEntryService(CashBookEntryRepository cashBookEntryRepository) {
        this.cashBookEntryRepository = cashBookEntryRepository;
    }

    /**
     * Creates a DTO for CSV-communication for a given CashBookEntry.
     *
     * @param cashBookEntry: Entry to convert to CSV
     * @return CashBookEntryCsv
     */
    public CashBookEntryCsv createCsvFromCashBookEntry(CashBookEntry cashBookEntry) {
        CashBookEntryCsv cashBookEntryCsv = new CashBookEntryCsv();
        cashBookEntryCsv.setBookingDate(cashBookEntry.getBookingDate());
        cashBookEntryCsv.setValueDate(cashBookEntry.getValueDate());
        cashBookEntryCsv.setBookingText(cashBookEntry.getBookingText());
        cashBookEntryCsv.setPurpose(cashBookEntry.getPurpose());
        cashBookEntryCsv.setCashPartnerName(cashBookEntry.getCashPartnerName());
        cashBookEntryCsv.setCashPartnerAccountNumber(cashBookEntry.getCashPartnerAccountNumber());
        cashBookEntryCsv.setCashPartnerBankCode(cashBookEntry.getCashPartnerBankCode());
        cashBookEntryCsv.setValue(cashBookEntry.getValue());
        return cashBookEntryCsv;
    }

    public List<CashBookEntryCsv> filterCsvsToAdd(List<CashBookEntryCsv> csvs) {

        // Get the already existing Entries from Database and transform them to CSVs.
        List<CashBookEntry> cashBookEntriesFromDatabase = findAll();
        List<CashBookEntryCsv> csvsFromDatabase = transformCashBookEntriesToCsvs(cashBookEntriesFromDatabase);

        // Filter the CSVs that do not already exist and return only them.
        List<CashBookEntryCsv> tempCsvs = Lists.newArrayList(csvs);
        return tempCsvs.stream().filter(csv -> !csvsFromDatabase.contains(csv)).collect(Collectors.toList());
    }

    public CashBookEntry createCashBookEntryFromCsv(CashBookEntryCsv csvEntry) {
        CashBookEntry cashBookEntry = new CashBookEntry();
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

    public List<CashBookEntry> transformCsvsToCashBookEntries(List<CashBookEntryCsv> csvs) {
        List<CashBookEntry> cashBookEntries = Lists.newArrayList();
        csvs.forEach(csv -> cashBookEntries.add(createCashBookEntryFromCsv(csv)));
        return cashBookEntries;
    }

    public List<CashBookEntryCsv> transformCashBookEntriesToCsvs(List<CashBookEntry> entries) {
        List<CashBookEntryCsv> csvs = Lists.newArrayList();
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
