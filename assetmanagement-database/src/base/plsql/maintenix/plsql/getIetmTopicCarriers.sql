--liquibase formatted sql


--changeSet getIetmTopicCarriers:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getIetmTopicCarriers
(
  aIetmDbId      ietm_topic.ietm_db_id%TYPE,
  aIetmId        ietm_topic.ietm_id%TYPE,
  aIetmTopicId   ietm_topic.ietm_topic_id%TYPE
) RETURN  VARCHAR2
IS
  ls_carriers VARCHAR2(4000);

   CURSOR lcur_Carriers (
         cn_IetmDbId         IN ietm_topic.ietm_db_id%TYPE,
         cn_IetmId           IN ietm_topic.ietm_id%TYPE,
         cn_IetmTopicId      IN ietm_topic.ietm_topic_id%TYPE
      ) IS
      SELECT
         DECODE( org_carrier.iata_cd,
                 NULL,
                 org_carrier.icao_cd,
                 DECODE( org_carrier.icao_cd, NULL,
                         org_carrier.iata_cd ,
                         org_carrier.iata_cd || ' / ' || org_carrier.icao_cd
                 )
         ) AS carrier_name,
         org_carrier.carrier_db_id || ':' || org_carrier.carrier_id AS carrier_key
      FROM
         ietm_topic_carrier,
         org_carrier
      WHERE
          ietm_topic_carrier.ietm_db_id   = cn_IetmDbId AND
         ietm_topic_carrier.ietm_id       = cn_IetmId AND
         ietm_topic_carrier.ietm_topic_id = cn_IetmTopicId
         AND
         org_carrier.carrier_db_id = ietm_topic_carrier.carrier_db_id AND
         org_carrier.carrier_id    = ietm_topic_carrier.carrier_id
      ORDER BY
         org_carrier.icao_cd;
   lrec_Carriers lcur_Carriers%ROWTYPE;
BEGIN

   FOR lrec_Carriers IN lcur_Carriers (aIetmDbId,aIetmId,aIetmTopicId ) LOOP
       ls_carriers := ls_carriers || ', ' || lrec_Carriers.carrier_name;
   END LOOP;

   IF ls_carriers IS NULL THEN
      RETURN NULL;
   END IF;

  RETURN substr(ls_carriers, 3);
END getIetmTopicCarriers;
/