package tobias.chess.cashBook;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin
public class CashBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashBookApplication.class, args);
	}

}
