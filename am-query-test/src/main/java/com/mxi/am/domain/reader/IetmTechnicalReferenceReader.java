package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;


/**
 * Reader for retrieving technical reference for a given IETM and technical reference name
 *
 */
public class IetmTechnicalReferenceReader {

   public static IetmTopicKey read( IetmDefinitionKey aIetm, String aTechnicalReferenceName ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereIn( new String[] { "ietm_db_id", "ietm_id" }, aIetm );
      lArgs.addWhere( "topic_sdesc", aTechnicalReferenceName );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "ietm_topic", lArgs );
      if ( !lQs.next() ) {
         throw new RuntimeException(
               String.format( "Ietm %s does not have a technical reference named: %s", aIetm,
                     aTechnicalReferenceName ) );
      }

      return lQs.getKey( IetmTopicKey.class,
            new String[] { "ietm_db_id", "ietm_id", "ietm_topic_id" } );

   }

}
