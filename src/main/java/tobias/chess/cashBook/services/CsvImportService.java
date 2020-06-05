package tobias.chess.cashBook.services;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.csvImport.SparkasseCsv;
import tobias.chess.cashBook.model.CashBookFile;

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

    public CashBookFile saveFile(MultipartFile file) {
        CashBookFile cashBookFile = new CashBookFile();
        cashBookFile.setCreatedAt(LocalDateTime.now());
        cashBookFile.setFileName(file.getName());
        return cashBookFile;
    }

    public List<SparkasseCsv> createSparkasseCsvs(InputStream inputStream) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader objectReader = csvMapper.readerFor(SparkasseCsv.class).with(csvSchema);
        return objectReader.<SparkasseCsv>readValues(inputStream)
                .readAll();
    }

}
