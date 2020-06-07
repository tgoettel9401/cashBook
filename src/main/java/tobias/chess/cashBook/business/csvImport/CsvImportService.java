package tobias.chess.cashBook.business.csvImport;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.business.cashBookFile.CashBookFileEntity;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CsvImportService {

    private final CashBookEntryService cashBookEntryService;

    public CsvImportService(CashBookEntryService cashBookEntryService) {
        this.cashBookEntryService = cashBookEntryService;
    }

    public CashBookFileEntity saveFile(MultipartFile file) {
        CashBookFileEntity cashBookFileEntity = new CashBookFileEntity();
        cashBookFileEntity.setCreatedAt(LocalDateTime.now());
        cashBookFileEntity.setFileName(file.getName());
        return cashBookFileEntity;
    }

    public List<SparkasseCsv> createSparkasseCsvs(InputStream inputStream) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader objectReader = csvMapper.readerFor(SparkasseCsv.class).with(csvSchema);
        return objectReader.<SparkasseCsv>readValues(inputStream)
                .readAll();
    }

}
