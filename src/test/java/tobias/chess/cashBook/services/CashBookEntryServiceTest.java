package tobias.chess.cashBook.services;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import tobias.chess.cashBook.csvImport.SparkasseCsv;
import tobias.chess.cashBook.model.CashBookEntry;
import tobias.chess.cashBook.repository.CashBookEntryRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

class CashBookEntryServiceTest {

    @InjectMocks
    @Spy
    private CashBookEntryService cashBookEntryService;

    @Mock
    private CashBookEntryRepository cashBookEntryRepository;

    private CashBookEntry cashBookEntry;
    private SparkasseCsv csv;

    @BeforeEach
    void setUp() {
        initMocks(this);
        initData();
    }

    @Test
    public void createCashBookEntryFromCsv_returnsCorrectCashBookEntry() {
        CashBookEntry createdCashBookEntry = cashBookEntryService.createCashBookEntryFromCsv(csv);
        assertThat(createdCashBookEntry.equals(cashBookEntry));
    }

    @Test
    public void createCsvFromCashBookEntry_returnsCorrectCashBookEntryCsv() {
        SparkasseCsv createdCsv = cashBookEntryService.createCsvFromCashBookEntry(cashBookEntry);
        assertThat(createdCsv.equals(csv));
    }

    @Test
    public void transformCsvToCashBookEntry_importsAllCsvs() {
        List<SparkasseCsv> csvs = Lists.newArrayList();
        csvs.add(new SparkasseCsv());
        csvs.add(new SparkasseCsv());
        csvs.add(new SparkasseCsv());

        List<CashBookEntry> cashBookEntries = cashBookEntryService.transformCsvsToCashBookEntries(csvs);

        assertThat(cashBookEntries.size()).isEqualTo(csvs.size());
    }

    @Test
    public void transformCashBookEntryToCsvs_importsAllEntries() {
        List<CashBookEntry> entries = Lists.newArrayList();
        entries.add(new CashBookEntry());
        entries.add(new CashBookEntry());
        entries.add(new CashBookEntry());

        List<SparkasseCsv> csvs = cashBookEntryService.transformCashBookEntriesToCsvs(entries);

        assertThat(csvs.size()).isEqualTo(entries.size());
    }

    @Test
    public void findAll_returnsAll() {
        List<CashBookEntry> cashBookEntries = Lists.newArrayList();
        cashBookEntries.add(new CashBookEntry());
        cashBookEntries.add(new CashBookEntry());
        Mockito.when(cashBookEntryRepository.findAll()).thenReturn(cashBookEntries);

        List<CashBookEntry> returnedCashBookEntries = cashBookEntryService.findAll();
        assertThat(returnedCashBookEntries.equals(cashBookEntries));
    }

    private void initData() {

        // Values
        LocalDate bookingDate = LocalDate.of(2020,1,1);
        LocalDate valueDate = LocalDate.of(2020,1,2);
        String bookingText = "Booking text";
        String purpose = "Some purpose";
        String cashPartnerName = "Partner";
        String cashPartnerAccountNumber = "DE4787678473";
        String cashPartnerBankCode = "BAILDE0587";
        Double value = 18.37;

        // Create Csv.
        csv = new SparkasseCsv();
        csv.setBookingDate(bookingDate);
        csv.setValueDate(valueDate);
        csv.setBookingText(bookingText);
        csv.setPurpose(purpose);
        csv.setCashPartnerName(cashPartnerName);
        csv.setCashPartnerAccountNumber(cashPartnerAccountNumber);
        csv.setCashPartnerBankCode(cashPartnerBankCode);
        csv.setValue(value);

        // Create CashBookEntry
        cashBookEntry = new CashBookEntry();
        cashBookEntry.setBookingDate(bookingDate);
        cashBookEntry.setValueDate(valueDate);
        cashBookEntry.setBookingText(bookingText);
        cashBookEntry.setPurpose(purpose);
        cashBookEntry.setCashPartnerName(cashPartnerName);
        cashBookEntry.setCashPartnerAccountNumber(cashPartnerAccountNumber);
        cashBookEntry.setCashPartnerBankCode(cashPartnerBankCode);
        cashBookEntry.setValue(value);

    }

    @Test
    public void filterCsvsToAdd_filtersCorrectCsvs() {
        List<CashBookEntry> databaseCashBookEntries = Lists.newArrayList();
        List<SparkasseCsv> givenCsvs = Lists.newArrayList();
        List<SparkasseCsv> databaseCsvs = Lists.newArrayList();

        // CSV 1 is part of both.
        SparkasseCsv csv1 = new SparkasseCsv();
        csv1.setBookingDate(LocalDate.of(2020,6,1));
        givenCsvs.add(csv1);
        databaseCsvs.add(csv1);

        // CSV 2 is only part of given Csvs
        SparkasseCsv csv2 = new SparkasseCsv();
        csv2.setBookingDate(LocalDate.of(2020,6,2));
        givenCsvs.add(csv2);

        // CSV 3 is only part of database Csvs
        SparkasseCsv csv3 = new SparkasseCsv();
        csv3.setBookingDate(LocalDate.of(2020,6,3));
        databaseCsvs.add(csv3);

        // CSV 4 is part of both.
        SparkasseCsv csv4 = new SparkasseCsv();
        csv4.setBookingDate(LocalDate.of(2020,6,4));
        givenCsvs.add(csv4);
        databaseCsvs.add(csv4);

        Mockito.doReturn(databaseCashBookEntries).when(cashBookEntryService).findAll();
        Mockito.doReturn(databaseCsvs).when(cashBookEntryService).transformCashBookEntriesToCsvs(
                databaseCashBookEntries);
        List<SparkasseCsv> returnedCsvs = cashBookEntryService.filterCsvsToAdd(givenCsvs);

        assertThat(returnedCsvs).hasSize(1);
        assertThat(returnedCsvs).contains(csv2);
        assertThat(returnedCsvs).doesNotContain(csv1, csv3, csv4);
    }

}