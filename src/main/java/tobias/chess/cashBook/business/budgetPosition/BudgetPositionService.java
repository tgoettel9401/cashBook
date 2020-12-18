package tobias.chess.cashBook.business.budgetPosition;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntry;
import tobias.chess.cashBook.business.budgetPosition.entry.BudgetPositionEntryRepository;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeaderRepository;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPointRepository;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitleRepository;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookService;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryBudgetPosition;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetPositionService {

    private final BudgetPositionHeaderRepository headerRepository;
    private final BudgetPositionTitleRepository titleRepository;
    private final BudgetPositionPointRepository pointRepository;
    private final BudgetPositionEntryRepository entryRepository;

    private final CashBookService cashBookService;

    public BudgetPositionService(BudgetPositionHeaderRepository headerRepository,
                                 BudgetPositionTitleRepository titleRepository,
                                 BudgetPositionPointRepository pointRepository,
                                 BudgetPositionEntryRepository entryRepository,
                                 CashBookService cashBookService) {
        this.headerRepository = headerRepository;
        this.titleRepository = titleRepository;
        this.pointRepository = pointRepository;
        this.entryRepository = entryRepository;
        this.cashBookService = cashBookService;
    }

    public List<BudgetPosition> findAllByCashBookDto(CashBookDto cashBookDto) {

        List<BudgetPosition> positions = Lists.newArrayList();

        Optional<CashBook> cashBookOptional = cashBookService.findById(cashBookDto.getId());

        if (cashBookOptional.isPresent()) {

            List<BudgetPositionHeader> headers = headerRepository.findAllByCashBook(cashBookOptional.get());

            for (BudgetPositionHeader header : headers) {

                List<BudgetPositionTitle>  titles = titleRepository.findAllByHeader(header);

                for (BudgetPositionTitle title : titles) {

                    List<BudgetPositionPoint> points = pointRepository.findAllByTitle(title);

                    for (BudgetPositionPoint point : points) {

                        List<BudgetPositionEntry> entries = entryRepository.findAllByPoint(point);

                        for (BudgetPositionEntry entry : entries) {

                            BudgetPosition position = new BudgetPosition();
                            position.setHeader(header);
                            position.setTitle(title);
                            position.setPoint(point);
                            position.setEntry(entry);
                            position.setPositionString(createPositionString(header, title, point, entry));
                            positions.add(position);

                        }

                    }

                }

            }

        }

        return positions;
    }

    public CashBookEntryBudgetPosition createCashBookEntryBudgetPosition(BudgetPosition budgetPosition) {
        CashBookEntryBudgetPosition cashBookEntryBudgetPosition = new CashBookEntryBudgetPosition();
        cashBookEntryBudgetPosition.setBudgetPositionHeader(budgetPosition.getHeader());
        cashBookEntryBudgetPosition.setBudgetPositionTitle(budgetPosition.getTitle());
        cashBookEntryBudgetPosition.setBudgetPositionPoint(budgetPosition.getPoint());
        cashBookEntryBudgetPosition.setBudgetPositionEntry(budgetPosition.getEntry());
        return cashBookEntryBudgetPosition;
    }

    public BudgetPosition createBudgetPosition(CashBookEntryBudgetPosition cashBookEntryBudgetPosition) {
        BudgetPosition budgetPosition = new BudgetPosition();
        budgetPosition.setHeader(cashBookEntryBudgetPosition.getBudgetPositionHeader());
        budgetPosition.setTitle(cashBookEntryBudgetPosition.getBudgetPositionTitle());
        budgetPosition.setPoint(cashBookEntryBudgetPosition.getBudgetPositionPoint());
        budgetPosition.setEntry(cashBookEntryBudgetPosition.getBudgetPositionEntry());
        budgetPosition.setPositionString(createPositionString(
                cashBookEntryBudgetPosition.getBudgetPositionHeader(), cashBookEntryBudgetPosition.getBudgetPositionTitle(),
                cashBookEntryBudgetPosition.getBudgetPositionPoint(), cashBookEntryBudgetPosition.getBudgetPositionEntry()
        ));
        return budgetPosition;
    }

    private String createPositionString(BudgetPositionHeader header, BudgetPositionTitle title,
                                        BudgetPositionPoint point, BudgetPositionEntry entry) {
        return header.getPosition() +
                "." +
                title.getPosition() +
                "." +
                point.getPosition() +
                "." +
                entry.getPosition();
    }

}
