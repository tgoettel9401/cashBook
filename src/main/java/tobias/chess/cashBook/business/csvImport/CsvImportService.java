package tobias.chess.cashBook.business.csvImport;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.business.cashBookFile.CashBookFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvImportService {

    private final CashBookEntryService cashBookEntryService;

    public CsvImportService(CashBookEntryService cashBookEntryService) {
        this.cashBookEntryService = cashBookEntryService;
    }

    public CashBookFile saveFile(MultipartFile file) {
        CashBookFile cashBookFile = new CashBookFile();
        cashBookFile.setCreatedAt(LocalDateTime.now());
        cashBookFile.setFileName(file.getName());
        return cashBookFile;
    }

    public List<CashBookEntry> importSparkasseCsvsFromInputStream(InputStream inputStream) throws MultipleCashBooksException, IOException {

        // Extract CashBookEntryDTOs first from file.
        List<SparkasseCsv> cashBookEntryDto = createSparkasseCsvs(inputStream);

        // Check that the entries all have the same Account-Number.
        List<String> accountNumbers = cashBookEntryDto.stream().map(SparkasseCsv::getAccount).distinct().collect(Collectors.toList());
        if (accountNumbers.size() > 1) {
            throw new MultipleCashBooksException();
        }

        // Then transform them to CashBookEntries.
        List<CashBookEntry> cashBookEntries =
                cashBookEntryService.transformCsvsToCashBookEntries(cashBookEntryDto);

        // Add the new Entries to the database and return the final entities.
        return cashBookEntryService.saveAll(cashBookEntries);
    }

    public List<SparkasseCsv> createSparkasseCsvs(InputStream inputStream) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader objectReader = csvMapper.readerFor(SparkasseCsv.class).with(csvSchema);
        return objectReader.<SparkasseCsv>readValues(convertLatin1InputStreamToUtf8(inputStream))
                .readAll();
    }

    private InputStreamReader convertLatin1InputStreamToUtf8(InputStream latin1Stream) {
        return new InputStreamReader(latin1Stream, StandardCharsets.ISO_8859_1);
    }

}
