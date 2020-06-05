package tobias.chess.cashBook.csvImport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties (ignoreUnknown = true)
public class SparkasseCsv {

    @JsonProperty("Auftragskonto")
    private String account;

    @JsonProperty("Buchungstag")
    @JsonFormat(pattern = "dd.MM.yy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate bookingDate;

    @JsonProperty("Valutadatum")
    @JsonFormat(pattern = "dd.MM.yy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate valueDate;

    @JsonProperty("Buchungstext")
    private String bookingText;

    @JsonProperty("Verwendungszweck")
    private String purpose;

    @JsonProperty("Beguenstigter/Zahlungspflichtiger")
    private String cashPartnerName;

    @JsonProperty("Kontonummer")
    private String cashPartnerAccountNumber;

    @JsonProperty("BLZ")
    private String cashPartnerBankCode;

    @JsonProperty("Betrag")
    @JsonDeserialize(using = GermanDoubleDeserializer.class)
    private Double value;

}
