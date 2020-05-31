package tobias.chess.cashBook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.csvImport.CashBookEntryCsv;
import tobias.chess.cashBook.model.CashBookEntry;
import tobias.chess.cashBook.repository.CashBookEntryRepository;

import java.time.LocalDateTime;

@Service
public class CashBookEntryService {

    private final CashBookEntryRepository cashBookEntryRepository;

    @Autowired
    public CashBookEntryService(CashBookEntryRepository cashBookEntryRepository) {
        this.cashBookEntryRepository = cashBookEntryRepository;
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

    public CashBookEntry save(CashBookEntry cashBookEntry) {
        return cashBookEntryRepository.save(cashBookEntry);
    }

}
