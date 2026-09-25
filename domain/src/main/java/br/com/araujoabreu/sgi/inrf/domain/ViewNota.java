package br.com.araujoabreu.sgi.inrf.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VW_INRF_NOTAS")
public class ViewNota implements Serializable {

	private static final long serialVersionUID = 5201591621180234132L;

	@Id
	@Column(name = "codigo")
	@NotNull
	private Long codigo;

	@Column(name = "ind_oper")
	private Integer indOper;

	@Column(name = "ind_emit")
	private Integer indEmit;

	@Column(name = "cod_part")
	private String codPart;

	@Column(name = "cod_sit")
	private String codSit;

	@Column(name = "ser")
	private String ser;

	@Column(name = "sub")
	private String sub;

	@Column(name = "num_doc")
	private String numDoc;

	@Column(name = "chv_nfse")
	private String chvNfse;

	@Column(name = "dt_doc")
	@Temporal(TemporalType.DATE)
	private Date dtDoc;

	@Column(name = "dt_exec_serv")
	@Temporal(TemporalType.DATE)
	private Date dtExecServ;

	@Column(name = "vl_doc")
	private BigDecimal vlDoc;

	@Column(name = "ind_pgto")
	private String indPgto;

	@Column(name = "vl_desc")
	private BigDecimal vlDesc;

	@Column(name = "vl_bc_pis")
	private BigDecimal vlBcPis;

	@Column(name = "vl_bc_cofinf")
	private BigDecimal vlBcCofinf;

	@Column(name = "vl_pis_ret")
	private BigDecimal vlPisRet;

	@Column(name = "vl_cofins_ret")
	private BigDecimal vlCofinsRet;

	@Column(name = "vl_iss")
	private BigDecimal vlIss;

	@Column(name = "ind_frt")
	private Integer indFrt;

	@Column(name = "cod_mod")
	private String codMod;

	public ViewNota() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getIndOper() {
		return indOper;
	}

	public void setIndOper(Integer indOper) {
		this.indOper = indOper;
	}

	public Integer getIndEmit() {
		return indEmit;
	}

	public void setIndEmit(Integer indEmit) {
		this.indEmit = indEmit;
	}

	public String getCodPart() {
		return codPart;
	}

	public void setCodPart(String codPart) {
		this.codPart = codPart;
	}

	public String getCodSit() {
		return codSit;
	}

	public void setCodSit(String codSit) {
		this.codSit = codSit;
	}

	public String getSer() {
		return ser;
	}

	public void setSer(String ser) {
		this.ser = ser;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}

	public String getChvNfse() {
		return chvNfse;
	}

	public void setChvNfse(String chvNfse) {
		this.chvNfse = chvNfse;
	}

	public Date getDtDoc() {
		return dtDoc;
	}

	public void setDtDoc(Date dtDoc) {
		this.dtDoc = dtDoc;
	}

	public Date getDtExecServ() {
		return dtExecServ;
	}

	public void setDtExecServ(Date dtExecServ) {
		this.dtExecServ = dtExecServ;
	}

	public BigDecimal getVlDoc() {
		return vlDoc;
	}

	public void setVlDoc(BigDecimal vlDoc) {
		this.vlDoc = vlDoc;
	}

	public String getIndPgto() {
		return indPgto;
	}

	public void setIndPgto(String indPgto) {
		this.indPgto = indPgto;
	}

	public BigDecimal getVlDesc() {
		return vlDesc;
	}

	public void setVlDesc(BigDecimal vlDesc) {
		this.vlDesc = vlDesc;
	}

	public BigDecimal getVlBcPis() {
		return vlBcPis;
	}

	public void setVlBcPis(BigDecimal vlBcPis) {
		this.vlBcPis = vlBcPis;
	}

	public BigDecimal getVlBcCofinf() {
		return vlBcCofinf;
	}

	public void setVlBcCofinf(BigDecimal vlBcCofinf) {
		this.vlBcCofinf = vlBcCofinf;
	}

	public BigDecimal getVlPisRet() {
		return vlPisRet;
	}

	public void setVlPisRet(BigDecimal vlPisRet) {
		this.vlPisRet = vlPisRet;
	}

	public BigDecimal getVlCofinsRet() {
		return vlCofinsRet;
	}

	public void setVlCofinsRet(BigDecimal vlCofinsRet) {
		this.vlCofinsRet = vlCofinsRet;
	}

	public BigDecimal getVlIss() {
		return vlIss;
	}

	public void setVlIss(BigDecimal vlIss) {
		this.vlIss = vlIss;
	}

	public Integer getIndFrt() {
		return indFrt;
	}

	public void setIndFrt(Integer indFrt) {
		this.indFrt = indFrt;
	}

	public String getCodMod() {
		return codMod;
	}

	public void setCodMod(String codMod) {
		this.codMod = codMod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ViewNota.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ViewNota other = (ViewNota) obj;
		if (codigo == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}
