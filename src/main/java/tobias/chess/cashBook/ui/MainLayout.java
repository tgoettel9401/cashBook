package tobias.chess.cashBook.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import tobias.chess.cashBook.ui.cashBook.CashBookView;
import tobias.chess.cashBook.ui.cashBookEntry.CashBookEntryView;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Cash Book Application");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink cashBookLink = new RouterLink("Cash-Books", CashBookView.class);
        cashBookLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink cashBookEntryLink = new RouterLink("Cash-Book-Entries", CashBookEntryView.class);
        cashBookEntryLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(cashBookLink, cashBookEntryLink));
    }

}
