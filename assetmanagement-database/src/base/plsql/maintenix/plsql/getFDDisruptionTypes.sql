--liquibase formatted sql


--changeSet getFDDisruptionTypes:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getFDDisruptionTypes
(
   aFdId fl_leg_disrupt.leg_disrupt_id%TYPE
) RETURN String
IS
   lFdDisruptionTypes String(4000);

   /* cursor declarations */
   CURSOR lcur_DisruptionTypes (
         cl_FdId fl_leg_disrupt.leg_disrupt_id%type
      ) IS

      SELECT
        fl_leg_disrupt_type.disrupt_type_cd

      FROM
        fl_leg_disrupt,
        fl_leg_disrupt_type

      WHERE
        fl_leg_disrupt.leg_disrupt_id = cl_FdId
        AND
        fl_leg_disrupt.leg_disrupt_id = fl_leg_disrupt_type.leg_disrupt_id

      ORDER BY
        disrupt_type_cd;

   lrec_DisruptionTypes lcur_DisruptionTypes%ROWTYPE;


BEGIN

   lFdDisruptionTypes := NULL;

   /* loop for every disruption type define for this flight disruption */
   FOR lrec_DisruptionTypes IN lcur_DisruptionTypes(aFdId) LOOP

      IF lFdDisruptionTypes IS NULL THEN
        /* *** If this is the first row, don't add a comma *** */
        lFdDisruptionTypes := lrec_DisruptionTypes.disrupt_type_cd;

      ELSE
        /* *** add a comma *** */
        lFdDisruptionTypes := lFdDisruptionTypes || ', ' || lrec_DisruptionTypes.disrupt_type_cd;

      END IF;

   END LOOP;


   RETURN lFdDisruptionTypes;

END getFDDisruptionTypes;
/