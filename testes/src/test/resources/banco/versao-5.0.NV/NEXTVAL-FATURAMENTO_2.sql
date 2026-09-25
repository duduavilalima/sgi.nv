/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno');
