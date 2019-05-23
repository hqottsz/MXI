package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import java.util.concurrent.atomic.AtomicInteger;

import com.mxi.am.domain.Attachment;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Ietm;
import com.mxi.am.domain.TechnicalReference;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.table.ietm.IetmIetm;


/**
 * Domain builder for Ietm entity
 *
 */
public class IetmBuilder {

   private static AtomicInteger iIetmCode = new AtomicInteger( 1 );
   private static final String IETM_NAME = "IETM";


   public static IetmDefinitionKey build( Ietm aIetm ) {

      IetmIetm lIetmIetm = new IetmIetm();
      lIetmIetm.setIetmCode( ( String ) defaultIfNull( aIetm.getCode(),
            String.valueOf( iIetmCode.getAndIncrement() ) ) );
      lIetmIetm.setIetmName(
            ( String ) defaultIfNull( aIetm.getName(), IETM_NAME + iIetmCode.getAndIncrement() ) );
      lIetmIetm.setPrefixLdesc( aIetm.getCommonPrefix() );
      lIetmIetm.setIetmLdesc( aIetm.getDescription() );

      IetmDefinitionKey lIetmDefnKey = lIetmIetm.insert();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lIetmDefnKey, "aIetmDbId", "aIetmId" );
      MxDataAccess.getInstance().executeUpdate( "com.mxi.mx.core.query.ietm.AssignAllAssemblies",
            lArgs );

      for ( DomainConfiguration<TechnicalReference> lTechnicalReferenceConfiguration : aIetm
            .getTechnicalReferences() ) {
         TechnicalReference lTechnicalReference = new TechnicalReference();
         lTechnicalReferenceConfiguration.configure( lTechnicalReference );
         if ( lTechnicalReference.getIetm() != null ) {
            throw new RuntimeException(
                  "Technical references added from IETM can have their IETM set only within IetmBuilder" );
         }
         lTechnicalReference.setIetm( lIetmDefnKey );
         TechnicalReferenceBuilder.build( lTechnicalReference );
      }

      for ( DomainConfiguration<Attachment> lAttachmentConfiguration : aIetm.getAttachments() ) {
         Attachment lAttachment = new Attachment();
         lAttachmentConfiguration.configure( lAttachment );
         if ( lAttachment.getIetmKey() != null ) {
            throw new RuntimeException(
                  "Attachment added from IETM can have their IETM set only within IetmBuilder" );
         }
         lAttachment.setIetmKey( lIetmDefnKey );
         AttachmentBuilder.build( lAttachment );
      }

      return lIetmDefnKey;
   }
}
