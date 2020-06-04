package tobias.chess.cashBook.services;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.csvImport.CashBookEntryCsv;
import tobias.chess.cashBook.model.CashBookEntry;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

class CsvImportServiceTest {

    @InjectMocks
    private CsvImportService csvImportService;

    private Resource resource;
    private InputStream inputStream;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        initMocks(this);
        initTestData();
    }

    private void initTestData() throws IOException {
        resource = new ClassPathResource("sparkasseCsv.csv");
        inputStream = resource.getInputStream();
        multipartFile = new MockMultipartFile("sparkasseCsv.csv", "sparkasseCsv.csv",
                "text/csv", inputStream);
    }

    @Test
    public void createCashBookFile_returnsCashBookFile() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        assert(csvImportService.saveFile(file) != null);
    }

    @Test
    public void createCashBookEntries_returnsCorrectResults() throws IOException {

        List<CashBookEntryCsv> entriesInService = csvImportService.createCashBookEntryCsvs(multipartFile);

        /*
        Values from CSV-File:
        Auftragskonto;Buchungstag;Valutadatum;Buchungstext;Verwendungszweck;Beguenstigter/Zahlungspflichtiger;Kontonummer;BLZ;Betrag;Waehrung;Info
        xxx;02.06.20;01.06.20;Some booking text;Used for this and that purpose;PersonInDebt;3827382718;54050110;-4;;
        xxx;05.04.19;27.04.19;Booking No. 2;Another prupose;AnotherPErson;384291938;29238481;29.88;;
        */

        CashBookEntryCsv entryOne = entriesInService.get(0);
        assertThat (entryOne.getBookingDate()).isEqualTo(LocalDate.of(2020,6,2));
        assertThat (entryOne.getValueDate()).isEqualTo(LocalDate.of(2020,6,1));
        assertThat (entryOne.getBookingText()).isEqualTo("Some booking text");
        assertThat (entryOne.getPurpose()).isEqualTo("Used for this and that purpose");
        assertThat (entryOne.getCashPartnerName()).isEqualTo("PersonInDebt");
        assertThat (entryOne.getCashPartnerAccountNumber()).isEqualTo("3827382718");
        assertThat (entryOne.getCashPartnerBankCode()).isEqualTo("54050110");
        assertThat (entryOne.getValue()).isEqualTo(-4.00);

        CashBookEntryCsv entryTwo = entriesInService.get(1);
        assertThat (entryTwo.getBookingDate()).isEqualTo(LocalDate.of(2019,4,5));
        assertThat (entryTwo.getValueDate()).isEqualTo(LocalDate.of(2019,4,27));
        assertThat (entryTwo.getBookingText()).isEqualTo("Booking No. 2");
        assertThat (entryTwo.getPurpose()).isEqualTo("Another prupose");
        assertThat (entryTwo.getCashPartnerName()).isEqualTo("AnotherPErson");
        assertThat (entryTwo.getCashPartnerAccountNumber()).isEqualTo("384291938");
        assertThat (entryTwo.getCashPartnerBankCode()).isEqualTo("29238481");
        assertThat (entryTwo.getValue()).isEqualTo(29.88);

    }

}