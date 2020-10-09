package tobias.chess.cashBook.business.csvImport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class GermanDoubleDeserializer extends StdDeserializer<Double> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6665876458172019077L;

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
