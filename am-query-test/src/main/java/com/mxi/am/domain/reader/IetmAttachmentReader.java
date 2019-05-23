package com.mxi.am.domain.reader;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;


/**
 * Reader for retrieving attachment key for a given IETM and name
 *
 */
public class IetmAttachmentReader {

   public static IetmTopicKey read( IetmDefinitionKey aIetm, String aAttachmentName ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetm, "ietm_db_id", "ietm_id" );
      lArgs.add( "topic_sdesc", aAttachmentName );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "ietm_topic", lArgs );
      if ( !lQs.next() ) {
         throw new RuntimeException( String.format( "Ietm %s does not have an attachment named: %s",
               aIetm, aAttachmentName ) );
      }

      return lQs.getKey( IetmTopicKey.class,
            new String[] { "ietm_db_id", "ietm_id", "ietm_topic_id" } );

   }

}
