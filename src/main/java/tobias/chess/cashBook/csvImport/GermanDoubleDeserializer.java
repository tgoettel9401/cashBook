package tobias.chess.cashBook.csvImport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class GermanDoubleDeserializer extends StdDeserializer<Double> {

    public GermanDoubleDeserializer() {
        this(null);
    }

    public GermanDoubleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String toParse = jsonParser.getValueAsString();
        if (StringUtils.isEmpty(toParse)) {
            return null;
        }

        return Double.parseDouble(toParse.replaceAll(",", "\\."));
    }
}
