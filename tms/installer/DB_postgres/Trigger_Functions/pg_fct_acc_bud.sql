-- Function: tms.pg_fct_acc_bud()

-- DROP FUNCTION tms.pg_fct_acc_bud();

CREATE OR REPLACE FUNCTION tms.pg_fct_acc_bud()
  RETURNS trigger AS
$BODY$
DECLARE
/******************************************************************************
   NAME:       ACC_BUD
   PURPOSE:    Validar que el campo de Key no sea ni modificado ni borrado 

******************************************************************************/
BEGIN   
   if OLD.key_encriptation <> NEW.key_encriptation then     
     RAISE EXCEPTION 'No se puedo modificar el campo de la key de la cuenta';      
   end if;
   if NEW.key_encriptation is null then
    RAISE EXCEPTION 'No se puedo modificar el campo de la key de la cuenta';      
   end if;
   return null;
EXCEPTION
   WHEN raise_exception THEN
      -- Consider logging the error and then re-raise
      RAISE EXCEPTION 'No se puedo modificar el campo de la key de la cuenta';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION tms.pg_fct_acc_bud() OWNER TO tms;
