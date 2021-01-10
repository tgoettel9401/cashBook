package tobias.chess.cashBook.business.budgetPosition;

public class BudgetPositionNotFoundException extends Exception {
    public BudgetPositionNotFoundException(Long cashBookId, String position) {
        super("The service was not able to find the Budget-Position " + position + " in the CashBook with ID " +
                cashBookId);
    }
}
