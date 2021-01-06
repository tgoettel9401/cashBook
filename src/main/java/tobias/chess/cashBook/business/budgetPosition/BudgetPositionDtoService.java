package tobias.chess.cashBook.business.budgetPosition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import tobias.chess.cashBook.business.budgetPosition.header.BudgetPositionHeader;
import tobias.chess.cashBook.business.budgetPosition.point.BudgetPositionPoint;
import tobias.chess.cashBook.business.budgetPosition.title.BudgetPositionTitle;
import tobias.chess.cashBook.business.cashBook.CashBookDto;
import tobias.chess.cashBook.business.cashBook.CashBookDtoService;

@Service
public class BudgetPositionDtoService {
	
	private final BudgetPositionService budgetPositionService;
	private final CashBookDtoService cashBookService;
	
	public BudgetPositionDtoService(BudgetPositionService budgetPositionService, CashBookDtoService cashBookService) {
		this.budgetPositionService = budgetPositionService;
		this.cashBookService = cashBookService;
	}
	
	public List<BudgetPositionDto> findAllDtosForCashBookDto(CashBookDto cashBookDto) {
		List<BudgetPositionDto> dtos = Lists.newArrayList();		
		List<BudgetPosition> budgetPositions = budgetPositionService.findAllByCashBookDto(cashBookDto);
		budgetPositions.forEach(budgetPosition -> dtos.add(convertBudgetPositionToDto(budgetPosition)));
        return dtos;
	}

	public BudgetPositionDto convertBudgetPositionToDto(BudgetPosition budgetPosition) {
		BudgetPositionDto dto = new BudgetPositionDto();
		dto.setCashBookDto(cashBookService.createCashBookDtoFromCashBook(budgetPosition.getHeader().getCashBook()));
		dto.setBudgetPosition(budgetPosition);
		dto.setType(BudgetPositionType.of(budgetPosition.getLevel()));
		dto.setPositionString(budgetPosition.getPositionString());
		
		if (budgetPosition.getHeader() != null)
			dto.setHeader(budgetPosition.getHeader().getName());
		if (budgetPosition.getTitle() != null)
			dto.setTitle(budgetPosition.getTitle().getName());
		if (budgetPosition.getPoint() != null)
			dto.setPoint(budgetPosition.getPoint().getName());
		
		return dto;
	}
	
	public List<BudgetPositionHeader> findAllHeaders() {
    	return budgetPositionService.findAllHeaders();
    }
    
    public List<BudgetPositionTitle> findAllTitles() {
    	return budgetPositionService.findAllTitles();
    }
    
    public List<BudgetPositionPoint> findAllPoints() {
    	return budgetPositionService.findAllPoints();
    }

	public BudgetPositionHeader saveHeader(CashBookDto cashBook, String name, Integer position) {
		return budgetPositionService.saveHeader(cashBook, name, position);
	}

	public BudgetPositionTitle saveTitle(BudgetPositionHeader header, String name, Integer position) {
		return budgetPositionService.saveTitle(header, name, position);
	}

	public BudgetPositionPoint savePoint(BudgetPositionTitle title, String name, Integer position) {
		return budgetPositionService.savePoint(title, name, position);
	}
	
}
