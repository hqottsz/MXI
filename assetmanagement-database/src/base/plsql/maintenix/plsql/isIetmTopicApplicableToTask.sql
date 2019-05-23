--liquibase formatted sql


--changeSet isIetmTopicApplicableToTask:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isIetmTopicApplicableToTask
(
  aIetmDbId      ietm_topic.ietm_db_id%TYPE,
  aIetmId        ietm_topic.ietm_id%TYPE,
  aIetmTopicId   ietm_topic.ietm_topic_id%TYPE,
  aInvNoDbId   inv_inv.inv_no_db_id%TYPE,
  aInvNoId     inv_inv.inv_no_id%TYPE

) RETURN  number
IS
  ls_carriers VARCHAR2(4000);
  ls_check number;

   CURSOR lcur_Carriers (
         cn_IetmDbId         IN ietm_topic.ietm_db_id%TYPE,
         cn_IetmId           IN ietm_topic.ietm_id%TYPE,
         cn_IetmTopicId      IN ietm_topic.ietm_topic_id%TYPE,
         cn_InvNoDbId        IN inv_inv.inv_no_db_id%TYPE,
         cn_InvNoId          IN inv_inv.inv_no_id%TYPE
      ) IS
      SELECT
        ietm_topic.topic_sdesc
      FROM
         inv_inv,
         ietm_topic,
         inv_inv h_inv_inv,
         inv_inv assmbl_inv_inv
      WHERE
         ietm_topic.ietm_db_id    = cn_IetmDbId AND
         ietm_topic.ietm_id       = cn_IetmId AND
         ietm_topic.ietm_topic_id = cn_IetmTopicId
         AND
         inv_inv.inv_no_db_id = cn_InvNoDbId    AND
         inv_inv.inv_no_id    = cn_InvNoId
         AND
         h_inv_inv.inv_no_db_id    = inv_inv.h_inv_no_db_id AND
         h_inv_inv.inv_no_id       = inv_inv.h_inv_no_id    AND
         h_inv_inv.rstat_cd        =  0
         AND
         assmbl_inv_inv.inv_no_db_id    (+)= inv_inv.assmbl_inv_no_db_id AND
         assmbl_inv_inv.inv_no_id       (+)= inv_inv.assmbl_inv_no_id    AND
         assmbl_inv_inv.rstat_cd        (+)= 0
         AND
         isIETMApplicable(inv_inv.inv_class_cd,inv_inv.appl_eff_cd,h_inv_inv.appl_eff_cd,assmbl_inv_inv.inv_no_db_id,assmbl_inv_inv.appl_eff_cd,ietm_topic.appl_eff_ldesc ) = 1
         AND
         (
             h_inv_inv.carrier_db_id IS NULL
            OR
            EXISTS
            ( SELECT
                 1
              FROM
                 ietm_topic_carrier
              WHERE
                 ietm_topic_carrier.carrier_db_id =  h_inv_inv.carrier_db_id          AND
                 ietm_topic_carrier.carrier_id    =  h_inv_inv.carrier_id             AND
                 ietm_topic_carrier.ietm_db_id    =  ietm_topic.ietm_db_id            AND
                 ietm_topic_carrier.ietm_id       =  ietm_topic.ietm_id               AND
                 ietm_topic_carrier.ietm_topic_id =  ietm_topic.ietm_topic_id
            )

         )
         ;
   lrec_Carriers lcur_Carriers%ROWTYPE;
BEGIN

   FOR lrec_Carriers IN lcur_Carriers (aIetmDbId,aIetmId,aIetmTopicId,aInvNoDbId,aInvNoId) LOOP
       ls_carriers := ls_carriers || ', ' || lrec_Carriers.topic_sdesc;
   END LOOP;

   IF ls_carriers IS NULL THEN
      ls_check := 0;
      ELSE
       ls_check := 1;
       END IF;


  RETURN ls_check;
END isIetmTopicApplicableToTask;

/