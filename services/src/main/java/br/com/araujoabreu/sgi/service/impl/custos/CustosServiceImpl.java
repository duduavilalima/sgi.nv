package br.com.araujoabreu.sgi.service.impl.custos;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.custos.CustosService;
import br.com.araujoabreu.sgi.service.custos.transfer.ImportacaoOrcamentoEmpresarialTO;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static br.com.araujoabreu.sgi.service.custos.transfer.ImportacaoOrcamentoEmpresarialTO.TipoPlanilha;

@Service
public class CustosServiceImpl implements CustosService {

	private final Log Log = LogFactory.getLog(CustosServiceImpl.class);

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	private final String RUBRICAS = "RUBRICAS";
	private final String CONSOLIDADO = "consolidado";
	private final String ASSINATURA = "Assinatura";
	private final String ORCAMENTO = "ORÇAMENTO";
	private final String DATA = "Data";
	private final String MES = "Mês";

	@Override
	public void importacaoOrcamentoEmpresarial(ImportacaoOrcamentoEmpresarialTO importacaoOrcamentoEmpresarialTO) throws Exception {
		validarPlanilha(importacaoOrcamentoEmpresarialTO);

		TipoPlanilha tipoPlanilha = TipoPlanilha.getEnum(importacaoOrcamentoEmpresarialTO.getTipoPlanilha());

		byte[] arrayBytes = importacaoOrcamentoEmpresarialTO.getFile();

		InputStream excelFile = null;

		if (arrayBytes == null) {
			return;
		}

		try {
			excelFile = new ByteArrayInputStream(arrayBytes);
			Workbook workbook = new XSSFWorkbook(excelFile);

			List<Sheet> listSheet;

			// Obtendo as planilhas
			if (TipoPlanilha.DIRETORIA.equals(tipoPlanilha)) {
				listSheet = getSheetList(workbook, 1);
			} else {
				listSheet = getSheetList(workbook, 13);
			}

			List<Map<String, Object>> list = new ArrayList<>();

			list.add(new TreeMap<>());

			// A planilha sempre será por mês
			Map<String, List<Row>> planilhas = new HashMap<>();
			planilhas.put(listSheet.get(12).getSheetName(), getRowList(listSheet.get(12)));
			planilhas.put(listSheet.get(11).getSheetName(), getRowList(listSheet.get(11)));
			planilhas.put(listSheet.get(10).getSheetName(), getRowList(listSheet.get(10)));
			planilhas.put(listSheet.get(9).getSheetName(), getRowList(listSheet.get(9)));
			planilhas.put(listSheet.get(8).getSheetName(), getRowList(listSheet.get(8)));
			planilhas.put(listSheet.get(7).getSheetName(), getRowList(listSheet.get(7)));
			planilhas.put(listSheet.get(6).getSheetName(), getRowList(listSheet.get(6)));
			planilhas.put(listSheet.get(5).getSheetName(), getRowList(listSheet.get(5)));
			planilhas.put(listSheet.get(4).getSheetName(), getRowList(listSheet.get(4)));
			planilhas.put(listSheet.get(3).getSheetName(), getRowList(listSheet.get(3)));
			planilhas.put(listSheet.get(2).getSheetName(), getRowList(listSheet.get(2)));
			planilhas.put(listSheet.get(1).getSheetName(), getRowList(listSheet.get(1)));

			for (String key : planilhas.keySet()) {
				System.out.println("Planilha: " + key);
				List<Row> rowList = planilhas.get(key);

				boolean continuarLendoLinha = false;
				List<Row> linhasValidas = new ArrayList<>();

				for (Row row : rowList) {
					List<Cell> cellList = Lists.newArrayList(row.iterator());
					if (cellList.size() > 0 && cellList.get(0).getCellType() != CellType.BLANK) {
						if (cellList.get(0).getCellType() == CellType.STRING) {
							if (cellList.get(0).getStringCellValue().trim().equalsIgnoreCase(RUBRICAS)) {
								Log.info("Adicionando linhas da planilha");
								continuarLendoLinha = true;
							}
							if (cellList.get(0).getStringCellValue().trim().equalsIgnoreCase("(%)")) {
								Log.info("Não adicionar linhas");
								continuarLendoLinha = false;
							}
							if (continuarLendoLinha) {
								linhasValidas.add(row);
							}
						}
					}
				}

				for (Row row : linhasValidas) {
					// A primeira linha são os centros financeiros. A partir da segunda coluna
					List<String> codigosCentrosFinanceiros = new ArrayList<>();
					for (Cell cell : Lists.newArrayList(row.iterator())) {
						if (cell.getCellType() == CellType.STRING) {
							if (!cell.getStringCellValue().trim().equalsIgnoreCase(RUBRICAS) || !cell.getStringCellValue().trim().equalsIgnoreCase("SUB-TOTAL")) {
								String identificador = cell.getStringCellValue().trim();
								// Não adicionar a gerências. Em algumas planilhas, a última coluna é o total da gerência.
								CentroFinanceiroTO gerencia = centroFinanceiroService.buscarCentroFinanceiroById(importacaoOrcamentoEmpresarialTO.getGerencia().getCodigo().toString());
								if (!gerencia.getIdentificadorFinal().equalsIgnoreCase(identificador)) {
									CentroFinanceiroTO centroFinanceiro = centroFinanceiroService.getCentroLucroByIndetificadorFinal(importacaoOrcamentoEmpresarialTO.getGerencia().getCodigo(), identificador);
									if (centroFinanceiro == null) {
										throw new Exception(String.format("Centro de lucro (%s) não identificado na planilha %s", identificador, key));
									}
									codigosCentrosFinanceiros.add(cell.getStringCellValue().trim());
								}
							}
						}
					}

					for (Cell cell : Lists.newArrayList(row.iterator())) {
						if (cell.getCellType() != CellType.BLANK) {
							if (cell.getCellType() == CellType.STRING) {
								System.out.print(cell.getStringCellValue());
								System.out.print(" ");
							} else if (cell.getCellType() == CellType.NUMERIC) {
								System.out.print(cell.getNumericCellValue());
								System.out.print(" ");
							} else if (cell.getCellType() == CellType.FORMULA) {
								if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
									System.out.print(cell.getNumericCellValue());
									System.out.print(" ");
								} else if (cell.getCachedFormulaResultType() == CellType.STRING) {
									System.out.print(cell.getStringCellValue());
									System.out.print(" ");
								}
							}
						}
					}
					System.out.println(" ");
				}
			}
/*
			for (Sheet sheet : listSheet) {
				if (!CONSOLIDADO.equals(sheet.getSheetName())) {

					// Obter as linhas da planilha
					List<Row> rowList = getRowList(sheet);

					int coluna = 1;

					for (int linha = 0; linha <= rowList.size(); linha++) {

						if (linha == rowList.size()) {
							coluna = coluna + 1;
							linha = 0;
						}

						if (list.size() == coluna) {
							list.add(new TreeMap<>());
						}

						if (coluna == 13) {
							break;
						}

						Row currentRow = rowList.get(linha);

						List<Cell> cellList = Lists.newArrayList(currentRow.iterator());

						if (cellList.get(0).getCellType() != CellType.BLANK) {
							if (coluna < cellList.size() && cellList.size() > 0 && continuar(cellList.get(0))) {
								Cell currentCell = cellList.get(coluna);
								if (currentCell.getCellType() == CellType.STRING) {
									list.get(coluna).put(cellList.get(0).getStringCellValue(), currentCell.getStringCellValue());
								} else if (currentCell.getCellType() == CellType.NUMERIC) {
									list.get(coluna).put(cellList.get(0).getStringCellValue(), currentCell.getNumericCellValue());
								} else if (currentCell.getCellType() == CellType.BLANK) {
									list.get(coluna).put(cellList.get(0).getStringCellValue(), BigDecimal.ZERO);
								} else if (currentCell.getCellType() == CellType.FORMULA) {
									if (currentCell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC) {
										list.get(coluna).put(cellList.get(0).getStringCellValue(), currentCell.getNumericCellValue());
									} else if (currentCell.getCachedFormulaResultTypeEnum() == CellType.STRING) {
										list.get(coluna).put(cellList.get(0).getStringCellValue(), currentCell.getStringCellValue());
									}
								}
							}
						}
					}
				}
			} // Fim - For */

			// list.remove(list.size() - 1);
			// list.remove(0);

			// System.out.println(list);

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (excelFile != null) {
				try {
					excelFile.close();
				} catch (IOException e) {
					throw new Exception(e);
				}
			}
		}
	}

	private boolean continuar(Cell currentCell) {
		if (currentCell.getCellType() == CellType.STRING) {
			return !currentCell.getStringCellValue().contains(ASSINATURA) && !currentCell.getStringCellValue().contains(DATA)
				&& !currentCell.getStringCellValue().contains(ORCAMENTO) && !currentCell.getStringCellValue().contains(MES);
		} else if (currentCell.getCellType() == CellType.NUMERIC) {
			return false;
		}
		return false;
	}

	private void validarPlanilha(ImportacaoOrcamentoEmpresarialTO importacaoOrcamentoEmpresarialTO) throws Exception {
		TipoPlanilha tipoPlanilhaImportacao = TipoPlanilha.getEnum(importacaoOrcamentoEmpresarialTO.getTipoPlanilha());
		if (tipoPlanilhaImportacao == null) {
			throw new Exception("Informe o tipo de planilha");
		}
		switch (tipoPlanilhaImportacao) {
			case MATRIZ_FILIAL:
				if (importacaoOrcamentoEmpresarialTO.getUnidade() == null) {
					throw new Exception("Matriz/empresa é obrigatória");
				}
				break;
			case DIRETORIA:
				if (importacaoOrcamentoEmpresarialTO.getDiretoria() == null) {
					throw new Exception("Diretoria é obrigatória");
				}
				break;
			case GERENCIA:
				if (importacaoOrcamentoEmpresarialTO.getGerencia() == null) {
					throw new Exception("Gerência é obrigatória");
				}
				break;
			case CENTRO_DE_LUCRO:
				if (importacaoOrcamentoEmpresarialTO.getCentroFinanceiro() == null) {
					throw new Exception("Centro de Lucro é obrigatório");
				}
				break;
		}
		if (importacaoOrcamentoEmpresarialTO.getReferenciaAno() == null || importacaoOrcamentoEmpresarialTO.getReferenciaAno() < 1900 || importacaoOrcamentoEmpresarialTO.getReferenciaAno() > 2100) {
			throw new Exception("Centro de Lucro é obrigatório");
		}
	}

	private List<Sheet> getSheetList(Workbook workbook, int sheetLength) throws Exception {
		List<Sheet> listSheet = Lists.newArrayList(workbook.sheetIterator());
		if (listSheet.size() != sheetLength) {
			throw new Exception("Núemro de planilhas incompatível");
		}
		return listSheet;
	}

	private List<Row> getRowList(Sheet sheet) {
		List<Row> rowList = Lists.newArrayList(sheet.iterator());
		return rowList;
	}

}
