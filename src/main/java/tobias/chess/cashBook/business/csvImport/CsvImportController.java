package tobias.chess.cashBook.business.csvImport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class CsvImportController {

    private final Logger logger = LoggerFactory.getLogger(CsvImportController.class);
    private final CashBookEntryService cashBookEntryService;
    private final CsvImportService csvImportService;

    @Autowired
    public CsvImportController(
            CsvImportService csvImportService, CashBookEntryService cashBookEntryService) {
        this.csvImportService = csvImportService;
        this.cashBookEntryService = cashBookEntryService;
    }

    @PostMapping("files")
    public List<CashBookEntry> uploadFile(@RequestParam("file") MultipartFile file)
            throws EmptyFileException, IOException {
        logger.info("Received request on /files");

        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        // Import file so that we have it later available later on.
        csvImportService.saveFile(file);

        // Extract CashBookEntryDTOs first from file.
        List<SparkasseCsv> cashBookEntryDto =
                csvImportService.createSparkasseCsvs(file.getInputStream());

        // Then transform them to CashBookEntries.
        List<CashBookEntry> cashBookEntries =
                cashBookEntryService.transformCsvsToCashBookEntries(cashBookEntryDto);

        // Add the new Entries to the database and return the final entities.
        return cashBookEntryService.saveAll(cashBookEntries);
    }

    @ExceptionHandler(MultipartException.class)
    public void handleMultipartException(HttpServletResponse response, MultipartException exception)
            throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
