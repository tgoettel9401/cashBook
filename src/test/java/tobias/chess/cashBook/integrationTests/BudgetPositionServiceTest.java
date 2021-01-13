package tobias.chess.cashBook.integrationTests;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tobias.chess.cashBook.business.budgetPosition.BudgetPosition;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionLevel;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionNotFoundException;
import tobias.chess.cashBook.business.budgetPosition.BudgetPositionService;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BudgetPositionServiceTest {

    @Autowired
    BudgetPositionService budgetPositionService;

    @Autowired
    CashBookService cashBookService;

    @Autowired
    CashBookDtoService cashBookDtoService;

    @Test
    public void testBudgetPositionEquality() throws CashBookNotFoundException, BudgetPositionNotFoundException {

        CashBook cashBook = new CashBook();
        cashBook.setAccountNumber("");
        cashBook.setName("Cash-Book");
        cashBook = cashBookService.save(cashBook);
        CashBookDto cashBookDto = cashBookDtoService.createCashBookDtoFromCashBook(cashBook);

        BudgetPositionHeader header = budgetPositionService.saveHeader(cashBookDto, "Header", 1, Lists.emptyList());
        BudgetPositionTitle title = budgetPositionService.saveTitle(header, "Header", 1, Lists.emptyList());
        BudgetPositionPoint point = budgetPositionService.savePoint(title, "Header", 1, Lists.emptyList());

        List<BudgetPosition> budgetPositionList = budgetPositionService.findAllByCashBookDto(cashBookDto).stream()
                .filter(p -> p.getLevel().equals(BudgetPositionLevel.POINT))
                .collect(Collectors.toList());
        assertThat(budgetPositionList).hasSize(1);

        BudgetPosition firstPosition = budgetPositionList.get(0);

        BudgetPosition secondPosition = budgetPositionService.findByCashBookDtoAndPosition(cashBookDto, "111");
        assertThat(secondPosition.getCashBookDto()).isEqualTo(firstPosition.getCashBookDto());
        assertThat(secondPosition.getHeader()).isEqualTo(firstPosition.getHeader());
        assertThat(secondPosition.getTitle()).isEqualTo(firstPosition.getTitle());
        assertThat(secondPosition.getPoint()).isEqualTo(firstPosition.getPoint());
        assertThat(firstPosition).isEqualTo(secondPosition);

    }

}