package br.com.araujoabreu.sgi.tesouraria.util.boletoUtil;

import java.io.Serializable;

/**
 * <p>
 * Enumeração das implementações dos módulos.
 * </p>
 *
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 * @version 0.2
 * @since 0.2
 */
public enum TipoDeModulo implements Serializable {

	MODULO10 {
		@Override
		public int valor() {
			return 10;
		}
	},

	MODULO11 {
		@Override
		public int valor() {
			return 11;
		}
	};

	public abstract int valor();
}
