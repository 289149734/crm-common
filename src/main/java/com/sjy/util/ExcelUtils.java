package com.sjy.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sjy.exception.CrmException;

public class ExcelUtils {
	private InputStream inStream;
	// *.xls
	private HSSFWorkbook hssfWorkbook;
	// *.xlsx
	private XSSFWorkbook xssfWorkbook;
	private List<String> titles;
	private List<String[]> values;
	private int lineTokenCount;
	private int tokenCount;

	public ExcelUtils(InputStream br, int type) throws IOException {
		this.inStream = br;
		if (type == 1) {
			this.hssfWorkbook = new HSSFWorkbook(br);
		} else if (type == 2) {
			this.xssfWorkbook = new XSSFWorkbook(br);
		} else {
			this.inStream.close();
			throw new CrmException("文件格式错误");
		}

		this.titles = new ArrayList<String>();
		this.values = new ArrayList<String[]>();
	}

	public ExcelUtils(File file, int lineTokenCount) throws InvalidFormatException, IOException {
		String fileName = file.getAbsolutePath();
		this.inStream = new BufferedInputStream(new FileInputStream(file));
		if (this.inStream != null) {
			if (fileName.endsWith(".xls")) {
				this.hssfWorkbook = new HSSFWorkbook(inStream);
			} else if (fileName.endsWith(".xlsx")) {
				this.xssfWorkbook = new XSSFWorkbook(inStream);
			} else {
				this.inStream.close();
				throw new InvalidFormatException("文件格式错误，请选择*.xls或者*.xlsx文件");
			}
		}
		this.titles = new ArrayList<String>();
		this.values = new ArrayList<String[]>();
		this.lineTokenCount = lineTokenCount;
	}

	public ExcelUtils(String fileName) throws InvalidFormatException, IOException {
		this.inStream = new BufferedInputStream(new FileInputStream(fileName));
		if (this.inStream != null) {
			if (fileName.endsWith(".xls")) {
				this.hssfWorkbook = new HSSFWorkbook(inStream);
			} else if (fileName.endsWith(".xlsx")) {
				this.xssfWorkbook = new XSSFWorkbook(inStream);
			} else {
				this.inStream.close();
				throw new InvalidFormatException("文件格式错误，请选择*.xls或者*.xlsx文件");
			}
		}
		this.titles = new ArrayList<String>();
		this.values = new ArrayList<String[]>();
		this.lineTokenCount = 0;
	}

	public String[] getTitles() {
		String[] a = new String[titles.size()];
		return titles.toArray(a);
	}

	public List<String[]> getValues() {
		return values;
	}

	private String getValue(HSSFCell hssfCell) {
		return new DataFormatter().formatCellValue(hssfCell);

		// if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
		// return String.valueOf(hssfCell.getBooleanCellValue());
		// } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		// if (DateUtil.isCellDateFormatted(hssfCell)) {
		// return FormatUtil.formatDate(hssfCell.getDateCellValue());
		// } else {
		// return String.valueOf(hssfCell.getNumericCellValue());
		// }
		// } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
		// return hssfCell.getCellFormula();
		// } else {
		// return String.valueOf(hssfCell.getStringCellValue());
		// }
	}

	private String getValue(XSSFCell xssfCell) {
		return new DataFormatter().formatCellValue(xssfCell);

		// if (xssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
		// return String.valueOf(xssfCell.getBooleanCellValue());
		// } else if (xssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		// if (DateUtil.isCellDateFormatted(xssfCell)) {
		// return FormatUtil.formatDate(xssfCell.getDateCellValue());
		// } else {
		// return new DataFormatter().formatCellValue(xssfCell);
		// }
		// } else if (xssfCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
		// return xssfCell.getCellFormula();
		// } else {
		// return String.valueOf(xssfCell.getStringCellValue());
		// }
	}

	/**
	 * 校验所有的数据是否为空
	 * 
	 * @param strs
	 * @return
	 */
	private boolean checkListValue(String[] strs) {
		int flag = 0;
		for (String str : strs) {
			if (StringUtils.isBlank(str)) {
				flag++;
			}
		}
		return flag == strs.length;
	}

	private void readXlsExcel() throws IOException {
		try {
			// 循环工作表Sheet
			int sheetNum = 1;// hssfWorkbook.getNumberOfSheets()
			for (int numSheet = 0; numSheet < sheetNum; numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}

				// 循环行Row
				if (lineTokenCount == 0 || lineTokenCount > hssfSheet.getLastRowNum()) {
					lineTokenCount = hssfSheet.getLastRowNum() + 1;
				}
				for (int rowNum = 0; rowNum < lineTokenCount; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}

					// 获取Titles
					if (tokenCount == 0) {
						tokenCount = hssfRow.getLastCellNum();
					}
					if (rowNum == 0) {
						// 循环列Cell
						for (int cellNum = 0; cellNum < tokenCount; cellNum++) {
							HSSFCell hssfCell = hssfRow.getCell(cellNum);
							if (hssfCell == null) {
								continue;
							}
							titles.add(getValue(hssfCell));
						}
					} else {
						String[] list = new String[tokenCount];
						// 循环列Cell
						for (int cellNum = 0; cellNum < tokenCount; cellNum++) {
							HSSFCell hssfCell = hssfRow.getCell(cellNum);
							if (hssfCell == null) {
								continue;
							}
							list[cellNum] = getValue(hssfCell);
						}
						if (!checkListValue(list)) {
							values.add(list);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inStream.close();
		}
	}

	private void readXlsxExcel() throws IOException {
		// 循环工作表Sheet
		try {
			int sheetNum = 1;// xssfWorkbook.getNumberOfSheets();
			for (int numSheet = 0; numSheet < sheetNum; numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}

				// 循环行Row
				if (lineTokenCount == 0 || lineTokenCount > xssfSheet.getLastRowNum()) {
					lineTokenCount = xssfSheet.getLastRowNum() + 1;
				}
				for (int rowNum = 0; rowNum < lineTokenCount; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow == null) {
						continue;
					}

					// 获取Titles
					if (tokenCount == 0) {
						tokenCount = xssfRow.getLastCellNum();
					}
					if (rowNum == 0) {
						// 循环列Cell
						for (int cellNum = 0; cellNum < tokenCount; cellNum++) {
							XSSFCell xssfCell = xssfRow.getCell(cellNum);
							if (xssfCell == null) {
								continue;
							}
							titles.add(getValue(xssfCell));
						}
					} else {
						String[] list = new String[tokenCount];
						// 循环列Cell
						for (int cellNum = 0; cellNum < tokenCount; cellNum++) {
							XSSFCell xssfCell = xssfRow.getCell(cellNum);
							if (xssfCell == null) {
								continue;
							}
							list[cellNum] = getValue(xssfCell);
						}
						if (!checkListValue(list)) {
							values.add(list);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inStream.close();
		}
	}

	public void read() throws Exception {
		if (hssfWorkbook != null) {
			readXlsExcel();
		} else if (xssfWorkbook != null) {
			readXlsxExcel();
		} else {
			throw new CrmException("文件格式错误，请选择*.xls或者*.xlsx文件");
		}
	}

	public List<byte[]> getPictures(String sheetName) {
		if (hssfWorkbook != null) {
			return getXlsPictures(sheetName);
		} else if (xssfWorkbook != null) {
			return getXlsxPictures(sheetName);
		} else {
			throw new CrmException("文件格式错误，请选择*.xls或者*.xlsx文件");
		}
	}

	public List<byte[]> getXlsPictures(String sheetName) {
		List<byte[]> datas = new ArrayList<byte[]>();
		HSSFSheet sheet = hssfWorkbook.getSheet(sheetName);

		if (sheet == null) {
			int sheetNum = hssfWorkbook.getNumberOfSheets();
			for (int i = 0; i < sheetNum; i++) {
				String _sheetName = hssfWorkbook.getSheetName(i);
				if (_sheetName.trim().equals(sheetName)) {
					sheet = hssfWorkbook.getSheetAt(i);
					break;
				}
			}
		}

		if (sheet != null) {
			HSSFPatriarch drawing = sheet.getDrawingPatriarch();
			if (drawing != null) {
				List<HSSFShape> shapes = drawing.getChildren();
				for (HSSFShape shape : shapes) {
					if (shape instanceof HSSFPicture) {
						HSSFPicture pic = (HSSFPicture) shape;
						byte[] data = pic.getPictureData().getData();
						datas.add(data);
					}
				}
			}
		}
		return datas;
	}

	public List<byte[]> getXlsxPictures(String sheetName) {
		List<byte[]> datas = new ArrayList<byte[]>();
		XSSFSheet sheet = xssfWorkbook.getSheet(sheetName);

		if (sheet == null) {
			int sheetNum = xssfWorkbook.getNumberOfSheets();
			for (int i = 0; i < sheetNum; i++) {
				String _sheetName = xssfWorkbook.getSheetName(i);
				if (_sheetName.trim().equals(sheetName)) {
					sheet = xssfWorkbook.getSheetAt(i);
					break;
				}
			}
		}

		if (sheet != null) {
			XSSFDrawing drawing = sheet.getDrawingPatriarch();
			if (drawing != null) {
				List<XSSFShape> shapes = drawing.getShapes();
				for (XSSFShape shape : shapes) {
					if (shape instanceof XSSFPicture) {
						XSSFPicture pic = (XSSFPicture) shape;
						byte[] data = pic.getPictureData().getData();
						datas.add(data);
					}
				}
			}
		}
		return datas;
	}

	public int getLineTokenCount() {
		return lineTokenCount;
	}

	public void setLineTokenCount(int lineTokenCount) {
		this.lineTokenCount = lineTokenCount;
	}

	public int getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(int tokenCount) {
		this.tokenCount = tokenCount;
	}

	public static void main(String[] args) throws Exception {
		ExcelUtils reader = new ExcelUtils(new File("D:\\联行号20150210.xlsx"), 0);
		reader.read();
		for (String s : reader.getTitles()) {
			System.out.print(s + "   ");
		}
		System.out.println();
		for (String[] list : reader.getValues()) {
			for (String obj : list) {
				System.out.print(obj + "   ");
			}
			System.out.println();
		}
	}
}
