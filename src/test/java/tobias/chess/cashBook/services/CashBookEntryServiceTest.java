package tobias.chess.cashBook.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import tobias.chess.cashBook.csvImport.CashBookEntryCsv;
import tobias.chess.cashBook.model.CashBookEntry;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

class CashBookEntryServiceTest {

    @InjectMocks
    private CashBookEntryService cashBookEntryService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void createCashBookEntryFromCsv_returnsCorrectCashBookEntry() {

        // Values
        LocalDate bookingDate = LocalDate.of(2020,1,1);
        LocalDate valueDate = LocalDate.of(2020,1,2);
        String bookingText = "Booking text";
        String purpose = "Some purpose";
        String cashPartnerName = "Partner";
        String cashPartnerAccountNumber = "DE4787678473";
        String cashPartnerBankCode = "BAILDE0587";
        Double value = 18.37;
        CurrencyUnit currency = Monetary.getCurrency("EUR");

        CashBookEntryCsv csv = new CashBookEntryCsv();
        csv.setBookingDate(bookingDate);
        csv.setValueDate(valueDate);
        csv.setBookingText(bookingText);
        csv.setPurpose(purpose);
        csv.setCashPartnerName(cashPartnerName);
        csv.setCashPartnerAccountNumber(cashPartnerAccountNumber);
        csv.setCashPartnerBankCode(cashPartnerBankCode);
        csv.setValue(value);

        CashBookEntry cashBookEntry = cashBookEntryService.createCashBookEntryFromCsv(csv);

        assertThat(cashBookEntry.getBookingDate()).isEqualTo(bookingDate);
        assertThat(cashBookEntry.getValueDate()).isEqualTo(valueDate);
        assertThat(cashBookEntry.getBookingText()).isEqualTo(bookingText);
        assertThat(cashBookEntry.getPurpose()).isEqualTo(purpose);
        assertThat(cashBookEntry.getCashPartnerName()).isEqualTo(cashPartnerName);
        assertThat(cashBookEntry.getCashPartnerAccountNumber()).isEqualTo(cashPartnerAccountNumber);
        assertThat(cashBookEntry.getCashPartnerBankCode()).isEqualTo(cashPartnerBankCode);
        assertThat(cashBookEntry.getValue()).isEqualTo(value);
        assertThat(cashBookEntry.getCreatedAt()).isNotNull();

    }

}