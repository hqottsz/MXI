package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefLabourRoleTypeKey.CERT;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.INSP;
import static com.mxi.mx.core.key.RefLabourRoleTypeKey.TECH;
import static com.mxi.mx.core.key.RefLabourStageKey.ACTV;

import com.mxi.am.domain.Labour;
import com.mxi.am.domain.LabourRole;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.table.sched.SchedLabourTable;


public class LabourBuilder {

   public static SchedLabourKey build( Labour labour ) {

      // Verify that the caller has not provided conflicting information.
      {
         if ( labour.getRequiresCertification().isPresent() ) {
            if ( !labour.getRequiresCertification().get()
                  && labour.getCertifierRole().isPresent() ) {
               throw new RuntimeException(
                     "Cannot set requires-certification to false and provide a certifier." );
            }
         }
         if ( labour.getRequiresInspection().isPresent() ) {
            if ( !labour.getRequiresInspection().get() && labour.getInspectorRole().isPresent() ) {
               throw new RuntimeException(
                     "Cannot set requires-inspection to false and provide an inspector." );
            }
         }
      }

      boolean requiresCertification = labour.getRequiresCertification().orElseGet( () -> {
         return labour.getCertifierRole().isPresent();
      } );
      boolean requiresInspection = labour.getRequiresInspection().orElseGet( () -> {
         return labour.getInspectorRole().isPresent();
      } );

      SchedLabourTable schedLabourRow = SchedLabourTable.create();
      schedLabourRow.setTask( labour.getTask() );
      labour.getSkill().ifPresent( skill -> schedLabourRow.setLabourSkill( skill ) );
      labour.getStageReason().ifPresent( reason -> schedLabourRow.setStageReason( reason ) );
      labour.getSourceJobStopLabour()
            .ifPresent( source -> schedLabourRow.setSourceJobStopLabourRow( source ) );
      schedLabourRow.setWorkPerformed( true );
      schedLabourRow.setCertification( requiresCertification );
      schedLabourRow.setIndependentInspection( requiresInspection );
      schedLabourRow.setLabourStage( labour.getStage().orElse( ACTV ) );
      schedLabourRow.setCurrentStatusOrder( 1 );
      SchedLabourKey schedLabour = schedLabourRow.insert();

      // By definition a labour must have a technician.
      LabourRole technician = labour.getTechnicianRole().orElseGet( () -> {
         return new LabourRole();
      } );
      technician.setType( TECH );
      technician.setLabour( schedLabour );
      buildLabourRole( labour, technician );

      // Optionally, a labour may have a certifier and/or an inspector.
      if ( requiresCertification ) {
         LabourRole certifier = labour.getCertifierRole().orElseGet( () -> {
            return new LabourRole();
         } );
         certifier.setType( CERT );
         certifier.setLabour( schedLabour );
         LabourRoleBuilder.build( certifier );
      }

      if ( requiresInspection ) {
         LabourRole inspector = labour.getInspectorRole().orElseGet( () -> {
            return new LabourRole();
         } );
         inspector.setType( INSP );
         inspector.setLabour( schedLabour );
         LabourRoleBuilder.build( inspector );
      }

      return schedLabour;
   }


   private static void buildLabourRole( Labour labour, LabourRole technician ) {
      LabourRoleBuilder.build( technician );
   }

}
