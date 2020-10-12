package tobias.chess.cashBook.business.csvImport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;

class GermanDoubleDeserializerTest {

    private GermanDoubleDeserializer deserializer;

    @Mock
    private JsonParser jsonParser;

    @BeforeEach
    void setUp() {
        initMocks(this);
        deserializer = new GermanDoubleDeserializer();
    }

    @Test
    public void deserialize_internationalDouble() throws IOException {
        String internationalDouble = "14.32";

        when(jsonParser.getValueAsString()).thenReturn(internationalDouble);

        Double returnedDouble = deserializer.deserialize(jsonParser, null);
        assertThat(returnedDouble).isEqualTo(14.32);
    }

    @Test
    public void deserialize_germanDouble() throws IOException {
        String germanDouble = "14,32";

        when(jsonParser.getValueAsString()).thenReturn(germanDouble);

        Double returnedDouble = deserializer.deserialize(jsonParser, null);
        assertThat(returnedDouble).isEqualTo(14.32);
    }


}