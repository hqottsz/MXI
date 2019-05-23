
package com.mxi.am.domain.builder;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.CANCEL;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static org.hamcrest.Matchers.arrayContaining;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.JobCard.PartRequirement;
import com.mxi.am.domain.Labour;
import com.mxi.am.domain.Measurement;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;


public class JobCardBuilder {

   public static TaskKey build( JobCard aJobCard ) {

      TaskBuilder lTaskBuilder = new TaskBuilder();
      lTaskBuilder.withTaskClass( RefTaskClassKey.JIC );

      lTaskBuilder
            .onInventory( aJobCard.getInventory().orElseGet( () -> Domain.createAircraft() ) );
      lTaskBuilder.withTaskRevision(
            aJobCard.getDefinition().orElseGet( () -> Domain.createJobCardDefinition() ) );
      lTaskBuilder.withStatus( aJobCard.getStatus().orElse( ACTV ) );

      aJobCard.getSuppressingJobCard().ifPresent(
            lSuppressingJobCard -> lTaskBuilder.withSuppressingTask( lSuppressingJobCard ) );

      if ( aJobCard.isHistorical().orElse( false ) ) {
         lTaskBuilder.asHistoric();
      }
      // if the historical flag is not provided then as a helper, check the status
      if ( !aJobCard.isHistorical().isPresent()
            && arrayContaining( COMPLETE, CANCEL ).matches( aJobCard.getStatus() ) ) {
         lTaskBuilder.asHistoric();
      }

      TaskKey lTaskKey = lTaskBuilder.build();

      // add part requirements to job card
      PartRequirementDomainBuilder lPartReqBuilder = new PartRequirementDomainBuilder( lTaskKey );
      for ( PartRequirement lPartRequirement : aJobCard.getPartRequirements() ) {

         lPartReqBuilder.forPart( lPartRequirement.getPart() );
         lPartReqBuilder.forPartGroup( lPartRequirement.getPartGroup() );
         lPartReqBuilder.withRequestAction( lPartRequirement.getRequestAction() );

         if ( lPartRequirement.getInstallQuantity() != null ) {
            lPartReqBuilder.withInstallQuantity( lPartRequirement.getInstallQuantity() );
            lPartReqBuilder.withInstallPart( lPartRequirement.getPart() );
         }

         lPartReqBuilder.build();
      }

      // add measurements to job card
      for ( DomainConfiguration<Measurement> lMeasurementConfig : aJobCard
            .getMeasurementConfigs() ) {
         Measurement lMeasurement = new Measurement();
         lMeasurementConfig.configure( lMeasurement );

         InvParmDataBuilder lInvParmDataBuilder = new InvParmDataBuilder();

         lInvParmDataBuilder.withDataOrd( lMeasurement.getOrder() );
         lInvParmDataBuilder.withDataType( lMeasurement.getDataType() );
         lInvParmDataBuilder.withInventoryKey( lMeasurement.getInventory() );
         lInvParmDataBuilder.withEventKey( lTaskKey.getEventKey() );

         lInvParmDataBuilder.withNaNote( lMeasurement.getNaNote() );

         lInvParmDataBuilder.build();
      }

      // Add labour
      for ( DomainConfiguration<Labour> lLabourConfig : aJobCard.getLabourConfigurations() ) {
         Labour lLabour = new Labour();
         lLabourConfig.configure( lLabour );
         if ( lLabour.getTask() != null ) {
            throw new RuntimeException( "Labour cannot have their task key previously set" );
         }
         lLabour.setTask( lTaskKey );
         LabourBuilder.build( lLabour );
      }

      return lTaskKey;
   }

}
