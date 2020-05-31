package tobias.chess.cashBook.services;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.csvImport.CashBookEntryCsv;
import tobias.chess.cashBook.model.CashBookFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CsvImportService {

    public CashBookFile saveFile(MultipartFile file) {
        CashBookFile cashBookFile = new CashBookFile();
        cashBookFile.setCreatedAt(LocalDateTime.now());
        cashBookFile.setFileName(file.getName());
        return cashBookFile;
    }

    public List<CashBookEntryCsv> createCashBookEntryCsvs(MultipartFile file) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
        ObjectReader objectReader = csvMapper.readerFor(CashBookEntryCsv.class).with(csvSchema);
        return objectReader.<CashBookEntryCsv>readValues(file.getInputStream())
                .readAll();
    }
}
