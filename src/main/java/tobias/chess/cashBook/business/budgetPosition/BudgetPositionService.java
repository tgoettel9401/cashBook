package tobias.chess.cashBook.business.budgetPosition;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeaderRepository;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPointRepository;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitleRepository;
import tobias.chess.cashBook.business.cashBook.CashBook;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookNotFoundException;
import tobias.chess.cashBook.business.cashBook.CashBookService;
import tobias.chess.cashBook.business.cashBookEntry.CashBookEntryBudgetPosition;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetPositionService {

    private final Logger logger = LoggerFactory.getLogger(BudgetPositionService.class);

    private final BudgetPositionHeaderRepository headerRepository;
    private final BudgetPositionTitleRepository titleRepository;
    private final BudgetPositionPointRepository pointRepository;

    private final CashBookService cashBookService;

    public BudgetPositionService(BudgetPositionHeaderRepository headerRepository,
                                 BudgetPositionTitleRepository titleRepository,
                                 BudgetPositionPointRepository pointRepository,
                                 CashBookService cashBookService) {
        this.headerRepository = headerRepository;
        this.titleRepository = titleRepository;
        this.pointRepository = pointRepository;
        this.cashBookService = cashBookService;
    }

    public List<BudgetPosition> findAllByCashBookDto(CashBookDto cashBookDto) {

        List<BudgetPosition> positions = Lists.newArrayList();

        Optional<CashBook> cashBookOptional = cashBookService.findById(cashBookDto.getId());

        if (cashBookOptional.isPresent()) {

            List<BudgetPositionHeader> headers = headerRepository.findAllByCashBook(cashBookOptional.get());

            for (BudgetPositionHeader header : headers) {

                BudgetPosition headerPosition = new BudgetPosition();
                headerPosition.setHeader(header);
                headerPosition.setCashBookDto(cashBookDto);
                headerPosition.getTags().addAll(header.getTags());
                positions.add(headerPosition);

                List<BudgetPositionTitle> titles = titleRepository.findAllByHeader(header);

                for (BudgetPositionTitle title : titles) {

                    BudgetPosition titlePosition = new BudgetPosition();
                    titlePosition.setCashBookDto(cashBookDto);
                    titlePosition.setHeader(header);
                    titlePosition.setTitle(title);
                    titlePosition.getTags().addAll(header.getTags());
                    titlePosition.getTags().addAll(title.getTags());
                    positions.add(titlePosition);

                    List<BudgetPositionPoint> points = pointRepository.findAllByTitle(title);
                    
                    for (BudgetPositionPoint point : points) {
                    	BudgetPosition position = new BudgetPosition();
                        position.setCashBookDto(cashBookDto);
                        position.setHeader(header);
                        position.setTitle(title);
                        position.setPoint(point);
                        position.getTags().addAll(header.getTags());
                        position.getTags().addAll(title.getTags());
                        position.getTags().addAll(point.getTags());
                        positions.add(position);
                    }

                }

            }

        }

        positions.sort(Comparator.comparing(BudgetPosition::getPosition));

        return positions;
    }

    public CashBookEntryBudgetPosition createCashBookEntryBudgetPosition(BudgetPosition budgetPosition) {
        CashBookEntryBudgetPosition cashBookEntryBudgetPosition = new CashBookEntryBudgetPosition();
        cashBookEntryBudgetPosition.setBudgetPositionHeader(budgetPosition.getHeader());
        cashBookEntryBudgetPosition.setBudgetPositionTitle(budgetPosition.getTitle());
        cashBookEntryBudgetPosition.setBudgetPositionPoint(budgetPosition.getPoint());
        return cashBookEntryBudgetPosition;
    }

    public BudgetPosition createBudgetPosition(CashBookEntryBudgetPosition cashBookEntryBudgetPosition) {
        BudgetPosition budgetPosition = new BudgetPosition();
        budgetPosition.setHeader(cashBookEntryBudgetPosition.getBudgetPositionHeader());
        budgetPosition.setTitle(cashBookEntryBudgetPosition.getBudgetPositionTitle());
        budgetPosition.setPoint(cashBookEntryBudgetPosition.getBudgetPositionPoint());
        return budgetPosition;
    }
    
    public BudgetPositionHeader saveHeader(CashBookDto cashBookDto, String name, Integer position, List<String> tags)
            throws CashBookNotFoundException {
		BudgetPositionHeader header = new BudgetPositionHeader();

        CashBook cashBook = cashBookService.findById(cashBookDto.getId())
                .orElseThrow(() -> new CashBookNotFoundException(cashBookDto.getId()));
        header.setCashBook(cashBook);
		header.setName(name);
		header.setPosition(position);
		header.setTags(tags);
		header = headerRepository.save(header);

        return header;
    }

    public BudgetPositionTitle saveTitle(BudgetPositionHeader header, String name, Integer position, List<String> tags) {
        BudgetPositionTitle title = new BudgetPositionTitle();
        title.setHeader(header);
        title.setName(name);
        title.setPosition(position);
        title.setTags(tags);
        return titleRepository.save(title);
    }

    public BudgetPositionPoint savePoint(BudgetPositionTitle title, String name, Integer position, List<String> tags) {
        BudgetPositionPoint point = new BudgetPositionPoint();
        point.setTitle(title);
        point.setName(name);
        point.setPosition(position);
        point.setTags(tags);
        return pointRepository.save(point);
    }

    public BudgetPosition findByCashBookDtoAndPosition(CashBookDto cashBookDto, String position) throws BudgetPositionNotFoundException {
        List<BudgetPosition> positions = findAllByCashBookDto(cashBookDto);
        return positions.stream()
                .filter(budgetPosition -> budgetPosition.getPosition().equals(position))
                .findFirst().orElseThrow(() -> new BudgetPositionNotFoundException(cashBookDto.getId(), position));
    }

    public List<BudgetPositionHeader> findAllHeadersByCashBookDto(CashBookDto cashBookDto) {
        List<BudgetPositionHeader> headers = Lists.newArrayList();
        cashBookService.findById(cashBookDto.getId()).ifPresent(
                cashBook -> headers.addAll(headerRepository.findAllByCashBook(cashBook))
        );
        return headers;
    }

    public List<BudgetPositionTitle> findAllTitlesByHeader(BudgetPositionHeader header) {
        return titleRepository.findAllByHeader(header);
    }

    public List<BudgetPositionPoint> findAllPointsByTitle(BudgetPositionTitle title) {
        return pointRepository.findAllByTitle(title);
    }

}
