/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-29 15:36                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_municipio_ect;
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_servico_tipo;

ALTER TABLE public.nota_saida	DROP CONSTRAINT fk_nota_saida_servico_tipo;

/* ---------------------------------------------------------------------- */
/* Modify table "servico_tipo"                                            */
/* ---------------------------------------------------------------------- */
DROP INDEX public.uq_servico_tipo_municipio_ect_sigla;
DROP INDEX public.ix_servico_tipo_municipio_ect;

ALTER TABLE public.servico_tipo
	DROP codigo_municipio_ect;

CREATE UNIQUE INDEX uq_servico_tipo_sigla ON public.servico_tipo (sigla);

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo			ADD CONSTRAINT fk_servico_tipo_servico_tipo 
    FOREIGN KEY (codigo_pai)			REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.nota_saida			ADD CONSTRAINT fk_nota_saida_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)	REFERENCES public.servico_tipo (codigo);
