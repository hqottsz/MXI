package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.concurrent.atomic.AtomicInteger;

import com.mxi.am.domain.TechnicalReference;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.table.ietm.IetmTopic;
import com.mxi.mx.core.table.ietm.IetmTopicCarrierTable;


/**
 * Domain Builder for Technical Reference
 *
 */
public class TechnicalReferenceBuilder {

   private static AtomicInteger iAtomicInteger = new AtomicInteger( 1 );
   private static final String TECHNICAL_REFERENCE_NAME = "TECH_REF";


   public static IetmTopicKey build( TechnicalReference aTechnicalReference ) {

      IetmTopic lIetmTopic =
            IetmTopic.create( IetmTopic.generatePrimaryKey( aTechnicalReference.getIetm() ) );

      lIetmTopic.setCmdlineParmLdesc( aTechnicalReference.getDefaultContext() );
      lIetmTopic.setTaskDefnCxtLdesc( aTechnicalReference.getTaskDefinitionContext() );
      lIetmTopic.setTaskCxtLDesc( aTechnicalReference.getTaskContext() );
      lIetmTopic.setIetmType( aTechnicalReference.getIetmType() );
      lIetmTopic.setTopicSdesc( ( String ) defaultIfNull( aTechnicalReference.getName(),
            TECHNICAL_REFERENCE_NAME + iAtomicInteger.getAndIncrement() ) );
      lIetmTopic.setApplicabilityRange( aTechnicalReference.getApplicabilityRange() );
      lIetmTopic.setDescLdesc( aTechnicalReference.getDescription() );

      IetmTopicKey lNewIetmTopic = lIetmTopic.insert();

      for ( CarrierKey lOperatorKey : aTechnicalReference.getOperators() ) {
         IetmTopicCarrierTable.create( lNewIetmTopic, lOperatorKey ).insert();
      }

      return lNewIetmTopic;
   }

}
