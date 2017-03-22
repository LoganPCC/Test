package importExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	/**
	 * Excel���
	 * 
	 * @author Snowolf
	 * @version 1.0
	 * @since 1.0
	 */
	public abstract class ImportUser {

		/**
		 * Excel 2003
		 */
		private final static String XLS = "xls";
		/**
		 * Excel 2007
		 */
		private final static String XLSX = "xlsx";
		/**
		 * �ָ���
		 */
		private final static String SEPARATOR = "|";

		/**
		 * ��Excel�ļ���Sheet������List
		 * 
		 * @param file
		 * @param sheetNum
		 * @return
		 */
		public static List<String> exportListFromExcel(File file, int sheetNum)
				throws IOException {
			return exportListFromExcel(new FileInputStream(file),
					FilenameUtils.getExtension(file.getName()), sheetNum);
		}

		/**
		 * ��Excel����Sheet������List
		 * 
		 * @param is
		 * @param extensionName
		 * @param sheetNum
		 * @return
		 * @throws IOException
		 */
		public static List<String> exportListFromExcel(InputStream is,
				String extensionName, int sheetNum) throws IOException {

			Workbook workbook = null;

			if (extensionName.toLowerCase().equals(XLS)) {
				workbook = new HSSFWorkbook(is);
			} else if (extensionName.toLowerCase().equals(XLSX)) {
				workbook = new XSSFWorkbook(is);
			}

			return exportListFromExcel(workbook, sheetNum);
		}

		/**
		 * ��ָ����Sheet������List
		 * 
		 * @param workbook
		 * @param sheetNum
		 * @return
		 * @throws IOException
		 */
		private static List<String> exportListFromExcel(Workbook workbook,
				int sheetNum) {

			Sheet sheet = workbook.getSheetAt(sheetNum);

			// ������ʽ���
			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();

			List<String> list = new ArrayList<String>();

			int minRowIx = sheet.getFirstRowNum();
			int maxRowIx = sheet.getLastRowNum();
			for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {
				Row row = sheet.getRow(rowIx);
				StringBuilder sb = new StringBuilder();

				short minColIx = row.getFirstCellNum();
				short maxColIx = row.getLastCellNum();
				for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
					Cell cell = row.getCell(new Integer(colIx));
					CellValue cellValue = evaluator.evaluate(cell);
					if (cellValue == null) {
						continue;
					}
					// ������ʽ���������ֻ����Boolean��Numeric��String�����������ͣ��������Error��
					// �����������ͣ����ݹٷ��ĵ�����ȫ���Ժ���http://poi.apache.org/spreadsheet/eval.html
					switch (cellValue.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						sb.append(SEPARATOR + cellValue.getBooleanValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						// ������������ͻᱻת��Ϊ�������ͣ���Ҫ�б�����ִ���
						if (DateUtil.isCellDateFormatted(cell)) {
							sb.append(SEPARATOR + cell.getDateCellValue());
						} else {
							double d =  cellValue.getNumberValue();
							int i = (int) d;
							sb.append(SEPARATOR +i);

						}
						break;
					case Cell.CELL_TYPE_STRING:
						sb.append(SEPARATOR + cellValue.getStringValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						break;
					case Cell.CELL_TYPE_BLANK:
						break;
					case Cell.CELL_TYPE_ERROR:
						break;
					default:
						break;
					}
				}
				list.add(sb.toString());
			}
			return list;
		}
	}





