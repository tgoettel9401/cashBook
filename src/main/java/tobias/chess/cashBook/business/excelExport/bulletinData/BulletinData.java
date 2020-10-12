package tobias.chess.cashBook.business.excelExport.bulletinData;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import lombok.Data;

@Data
public class BulletinData {
	private BulletinHeaderLine headerLine;
	private List<BulletinDataLine> dataLines = Lists.newArrayList();
	private BulletinSubHeaderLineInitialWealth subHeaderLineInitialWealth;
	private BulletinSubHeaderLineCalculatedInitialWealth subHeaderLineCalculatedInitialWealth;
}
