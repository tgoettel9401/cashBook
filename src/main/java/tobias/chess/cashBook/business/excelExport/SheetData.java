package tobias.chess.cashBook.business.excelExport;

import java.util.List;

import tobias.chess.cashBook.business.excelExport.bulletinData.BulletinDataLine;

public interface SheetData {
	public BulletinDataLine getHeaderLine();
	public List<DataLine> getDataLines();
}
