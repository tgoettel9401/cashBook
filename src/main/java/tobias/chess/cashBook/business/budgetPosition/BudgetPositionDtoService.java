package tobias.chess.cashBook.business.budgetPosition;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import tobias.chess.cashBook.business.cashBook.CashBookDto;

@Service
public class BudgetPositionDtoService {
	
	private final BudgetPositionService budgetPositionService;
	
	public BudgetPositionDtoService(BudgetPositionService budgetPositionService) {
		this.budgetPositionService = budgetPositionService;
	}
	
	public List<BudgetPositionDto> findAllDtosForCashBookDto(CashBookDto cashBookDto) {
		List<BudgetPositionDto> dtos = Lists.newArrayList();		
		List<BudgetPosition> budgetPositions = budgetPositionService.findAllByCashBookDto(cashBookDto);
		budgetPositions.forEach(budgetPosition -> dtos.add(convertBudgetPositionToDto(budgetPosition)));
        return dtos;
	}

	public BudgetPositionDto convertBudgetPositionToDto(BudgetPosition budgetPosition) {
		BudgetPositionDto dto = new BudgetPositionDto();
		dto.setBudgetPosition(budgetPosition);
		dto.setPositionString(budgetPosition.getPositionString());
		dto.setHeader(budgetPosition.getHeader().getName());
		dto.setTitle(budgetPosition.getTitle().getName());
		dto.setPoint(budgetPosition.getPoint().getName());
		dto.setEntry(budgetPosition.getEntry().getName());
		return dto;
	}

}
