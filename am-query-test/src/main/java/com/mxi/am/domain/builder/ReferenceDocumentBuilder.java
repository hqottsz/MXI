package com.mxi.am.domain.builder;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.ReferenceDocument;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;


public class ReferenceDocumentBuilder {

   public static TaskKey build( ReferenceDocument aReferenceDocument ) {

      TaskBuilder lTaskBuilder = new TaskBuilder();

      if ( aReferenceDocument.getTaskClass() == null ) {
         aReferenceDocument.setTaskClass( TaskRevisionBuilder.TASK_CLASS_REF );
      }
      lTaskBuilder.withTaskClass( aReferenceDocument.getTaskClass() );

      if ( aReferenceDocument.getInventory() == null ) {
         aReferenceDocument.setInventory( Domain.createAircraft() );
      }
      lTaskBuilder.onInventory( aReferenceDocument.getInventory() );

      if ( aReferenceDocument.getDefinition() == null ) {
         aReferenceDocument.setDefinition( Domain.createReferenceDocumentDefinition() );
      }
      lTaskBuilder.withTaskRevision( aReferenceDocument.getDefinition() );

      if ( aReferenceDocument.getStatus() == null ) {
         aReferenceDocument.setStatus( RefEventStatusKey.ACTV );
      }
      lTaskBuilder.withStatus( aReferenceDocument.getStatus() );

      if ( RefEventStatusKey.COMPLETE.equals( aReferenceDocument.getStatus() )
            || RefEventStatusKey.CANCEL.equals( aReferenceDocument.getStatus() ) ) {
         lTaskBuilder.asHistoric();
      }

      TaskKey lReqKey = lTaskBuilder.build();

      return lReqKey;
   }

}
