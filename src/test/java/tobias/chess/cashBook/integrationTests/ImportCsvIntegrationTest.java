package tobias.chess.cashBook.integrationTests;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tobias.chess.cashBook.model.CashBook;
import tobias.chess.cashBook.model.CashBookEntry;
import tobias.chess.cashBook.services.CashBookEntryService;
import tobias.chess.cashBook.services.CashBookService;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ImportCsvIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CashBookService cashBookService;

    @Autowired
    private CashBookEntryService cashBookEntryService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesCsvImportController() {
        ServletContext servletContext = wac.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext).isInstanceOf(MockServletContext.class);
        assertThat(wac.getBean("csvImportController"));
    }

    @Test
    public void uploadCorrectCsvFile_returnsOKAndImportsFile() throws Exception {

        // Get initial sizes.
        int initialCashBookSize = cashBookService.findAll().size();
        int initialCashBookEntriesSize = cashBookEntryService.findAll().size();

        // The loaded csv-file contains two CashBookEntries with the same CashBook.
        InputStream inputStream = new ClassPathResource("sparkasseCsv.csv").getInputStream();
        MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);

        // Import the file.
        this.mockMvc.perform(multipart("/files").file(multipartFile))
                .andExpect(status().isOk());

        // The size of CashBooks should be increased by 1.
        List<CashBook> cashBooks = cashBookService.findAll();
        assertThat(cashBooks).hasSize(initialCashBookSize + 1);

        // The size of CashBookEntries should be increased by 2.
        List<CashBookEntry> cashBookEntries = cashBookEntryService.findAll();
        assertThat(cashBookEntries).hasSize(initialCashBookEntriesSize + 2);

        // It is possible to access the Getter-endpoints of CashBooks.
        Long cashBookId = cashBooks.get(1).getId();
        this.mockMvc.perform(get("/cashBooks"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/cashBooks/" + cashBookId))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/cashBooks/" + cashBookId + "/cashBookEntries"))
                .andExpect(status().isOk());

        // It is possible to access the Getter-endpoints of CashBooks.
        Long cashBookEntryId = cashBookEntries.get(2).getId();
        this.mockMvc.perform(get("/cashBookEntries"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/cashBookEntries/" + cashBookEntryId))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/cashBookEntries/" + cashBookEntryId + "/cashBook"))
                .andExpect(status().isOk());

    }

}
