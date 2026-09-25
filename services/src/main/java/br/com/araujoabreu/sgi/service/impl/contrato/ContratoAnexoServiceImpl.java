package br.com.araujoabreu.sgi.service.impl.contrato;

import br.com.araujoabreu.sgi.administracao.domain.ContratoAnexo;
import br.com.araujoabreu.sgi.administracao.domain.QContratoAnexo;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.service.contrato.ContratoAnexoService;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoPesquisaTO;
import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.fasolti.commons.Util;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.anexo.AnexoRepository;
import br.com.fasolti.sgi.repository.contrato.ContratoAnexoRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

@Service
public class ContratoAnexoServiceImpl extends BaseSearchService<ContratoAnexo, ContratoAnexoTO, ContratoAnexoPesquisaTO, ContratoAnexoRepository> implements ContratoAnexoService {

	private static final QContratoAnexo qContratoAnexo = QContratoAnexo.contratoAnexo;

	@Autowired
	private AnexoRepository anexoRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private SecurityService<Usuario> usuarioSecurityService;

	@Autowired
	public ContratoAnexoServiceImpl(ContratoAnexoRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(ContratoAnexo entity, ContratoAnexoTO object) {
		try {
			salvarArquivo(object);

			object.getAnexo().setCriador(usuarioSecurityService.getUsername());
			object.getAnexo().setDataCriacao(new Date());

			Anexo anexo = anexoRepository.save(getMapper().map(object.getAnexo(), Anexo.class));

			entity.setAnexo(anexo);

		} catch (Exception e) {
			throw new ValidationException("ContratoAnexoServiceImpl-doBeforeSave", e.getMessage());
		}
	}

	@Override
	protected void doBeforeUpdate(ContratoAnexo entity, ContratoAnexoTO object) {
		try {

			salvarArquivo(object);
			entity.setAnexo(anexoRepository.save(getMapper().map(object.getAnexo(), Anexo.class)));

		} catch (Exception e) {
			throw new ValidationException("ContratoAnexoServiceImpl-doBeforeUpdate", e.getMessage());
		}
	}

	@Override
	protected void doAfterUpdate(ContratoAnexo entity, ContratoAnexoTO object) throws ServiceException {
		if (entity.getAditivo() != null && entity.getAditivo().getCodigo() == null)
			entity.setAditivo(null);
	}

	/*
	 *  ----------------
	 *   SALVAR ARQUIVO
	 *  ----------------
	 */
	private void salvarArquivo(ContratoAnexoTO object) {
		AnexoTO anexo = object.getAnexo();

		String pastaDestino = parametroRepository.findByChave("comercial.contrato.anexo.diretorio").getValor();

		File fileDiretorio = new File(pastaDestino);
		if (!fileDiretorio.exists()) {
			fileDiretorio.mkdirs();
		}

		String nomeCompleto = criarNomeArquivo(pastaDestino, anexo.getNome());

		try {
			PrintWriter writer = new PrintWriter(nomeCompleto, "UTF-8");
			writer.print(anexo.getTexto());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("ContratoAnexoServiceImpl-salvarArquivo", e.getMessage());
		}
	}

	/**
	 * Cria e valida o nome do arquivo
	 * prevenindo que não ocorra a sobrescrição de arquivos com o mesmo nome.
	 *
	 * @return
	 */
	private String criarNomeArquivo(String pastaDestino, String nomeArquivo) {

		StringBuilder nomeCompleto = new StringBuilder();

		Boolean nomeInvalido = Boolean.TRUE;
		Integer numero = 1;
		String nome = new String(nomeArquivo);

		while (nomeInvalido) {
			nomeCompleto.setLength(0);
			nomeCompleto.append(pastaDestino);
			nomeCompleto.append("/");
			nomeCompleto.append(nome);

			File fileNomeCompleto = new File(nomeCompleto.toString());
			if (fileNomeCompleto.exists()) {
				nome = new String("Cópia_" + numero + "_" + nomeArquivo);
				numero++;
			} else {
				nomeInvalido = Boolean.FALSE;
			}
		}
		return nomeCompleto.toString();
	}

	@Override
	@Transactional
	public void remover(Long id) throws Exception {
		try {
			ContratoAnexo contratoAnexo = getRepository().findById(id).orElse(null);
			getRepository().delete(contratoAnexo);
			anexoRepository.deleteById(contratoAnexo.getAnexo().getCodigo());

			String pastaDestino = parametroRepository.findByChave("comercial.contrato.anexo.diretorio").getValor();

			File file = new File(pastaDestino + "/" + contratoAnexo.getAnexo().getNome());

			if (file.exists()) {
				file.delete();
			}

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void baixarAnexo(@PathVariable String id, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		try {
			Anexo anexo = anexoRepository.findById(new Long(id)).orElse(null);

			if (anexo == null) {
				throw new FileNotFoundException("Anexo não encontrado");
			} else {
				String pastaDestino = parametroRepository.findByChave("comercial.contrato.anexo.diretorio").getValor();
				File arquivo = new File(pastaDestino + "/" + anexo.getNome());

				Util.download(arquivo, anexo.getContentType().trim(), request, response);
			}
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}


	@Override
	public AnexoTO verificarAnexo(Long id) throws Exception {
		Anexo anexo = anexoRepository.findById(id).orElse(null);

		if (anexo == null) {
			throw new FileNotFoundException("Anexo não encontrado");
		} else {
			String pastaDestino = parametroRepository.findByChave("comercial.contrato.anexo.diretorio").getValor();
			File arquivo = new File(pastaDestino + "/" + anexo.getNome());


			if (!arquivo.exists())
				return new AnexoTO();
			else
				return getMapper().map(anexo, AnexoTO.class);
		}
	}
}
