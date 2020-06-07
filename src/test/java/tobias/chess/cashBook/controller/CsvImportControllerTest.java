package tobias.chess.cashBook.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import tobias.chess.cashBook.business.csvImport.CsvImportController;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;
import tobias.chess.cashBook.business.csvImport.CsvImportService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CsvImportController.class)
class CsvImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvImportService csvImportService;

    @MockBean
    private CashBookEntryService cashBookEntryService;

    @Test
    public void uploadFile_returnsOkForFile() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.csv", "text/csv",
                "Spring Framework".getBytes());

        this.mockMvc.perform(multipart("/files").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void uploadEmptyFile_returnsBadRequest() throws Exception {
        MockMultipartFile multipartFile = Mockito.mock(MockMultipartFile.class);
        when(multipartFile.isEmpty()).thenReturn(true);

        this.mockMvc.perform(multipart("/files").file(multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadFile_returnsBadRequestForEmptyFile() throws Exception {
        this.mockMvc.perform(post("/files"))
                .andExpect(status().isBadRequest());
    }

}