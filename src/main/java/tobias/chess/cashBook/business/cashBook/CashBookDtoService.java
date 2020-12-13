package tobias.chess.cashBook.business.cashBook;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntry;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CashBookDtoService {

    private final CashBookService cashBookService;
    private final CashBookEntryService cashBookEntryService;

    public CashBookDtoService(CashBookService cashBookService, CashBookEntryService cashBookEntryService) {
        this.cashBookService = cashBookService;
        this.cashBookEntryService = cashBookEntryService;
    }

    public List<CashBookDto> findAllDtos() {
        List<CashBookDto> cashBookDtos = Lists.newArrayList();
        cashBookService.findAll().forEach(
                cashBook -> cashBookDtos.add(createCashBookDtoFromCashBook(cashBook))
        );
        return cashBookDtos;
    }

    public CashBookDto createCashBookDtoFromCashBook(CashBook cashBook) {
        CashBookDto dto = new CashBookDto();

        // First set retrieved data.
        dto.setId(cashBook.getId());
        dto.setAccountNumber(cashBook.getAccountNumber());
        dto.setName(cashBook.getName());
        dto.setInitialWealth(cashBook.getInitialWealth());
        dto.setCalculatedInitialWealth(cashBook.getCalculatedInitialWealth());

        // Then calculate the additional fields.
        dto.setResult(calculateResult(cashBook));
        dto.setCalculatedResult(new BigDecimal(0));
        dto.setFinalWealth(dto.getInitialWealth().add(dto.getResult()));
        dto.setCalculatedFinalWealth(new BigDecimal(0));

        return dto;
    }

    public void delete(CashBookDto cashBookDto) {
        cashBookService.delete(cashBookDto);
    }

    public CashBookDto save(CashBookDto cashBookDto) {
        CashBook savedCashBook = cashBookService.save((cashBookDto));
        return createCashBookDtoFromCashBook(savedCashBook);
    }

    private BigDecimal calculateResult(CashBook cashBook) {
        List<CashBookEntry> entries = cashBookEntryService.findAllByCashBook(cashBook);
        List<BigDecimal> valueOfEntries = entries.stream().map(CashBookEntry::getValue).collect(Collectors.toList());
        return valueOfEntries.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
