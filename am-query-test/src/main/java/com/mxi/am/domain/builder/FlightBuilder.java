package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Flight;
import com.mxi.am.domain.UsageAdjustment;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;
import com.mxi.mx.core.usage.model.UsageType;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;


/**
 * Builds an <code>fl_leg</code> object
 */
public final class FlightBuilder {

   private FlightBuilder() {
      // utility class
   }


   public static FlightLegId build( Flight aFlight ) {

      FlightLegId lFlightLegId = new FlightLegId( new SequentialUuidGenerator().newUuid() );
      FlightLegEntity lFlightLegEntity = new FlightLegEntity( lFlightLegId );

      lFlightLegEntity.setFlightName( aFlight.getName() );
      lFlightLegEntity.setArrivalLocation( aFlight.getArrivalLocation() );
      lFlightLegEntity.setExternalKey( aFlight.getExternalKey() );
      lFlightLegEntity.setFlightStatus(
            ( FlightLegStatus ) defaultIfNull( aFlight.getStatus(), FlightLegStatus.MXPLAN ) );
      lFlightLegEntity.setHistorical( aFlight.isHistorical() );
      if ( aFlight.getAircraft() == null ) {
         aFlight.setAircraft( Domain.createAircraft() );
      }
      lFlightLegEntity.setAircraft( aFlight.getAircraft() );

      if ( aFlight.getDepartureLocation() == null ) {
         aFlight.setDepartureLocation( Domain.createLocation() );
      }
      lFlightLegEntity.setDepartureLocation( aFlight.getDepartureLocation() );

      if ( aFlight.getArrivalLocation() == null ) {
         aFlight.setArrivalLocation( Domain.createLocation() );
      }

      lFlightLegEntity.setSchedDepartureDate( aFlight.getScheduledDepartureDate() );
      lFlightLegEntity.setSchedArrivalDate( aFlight.getScheduledArrivalDate() );
      lFlightLegEntity.setActualDepartureDate( aFlight.getDepartureDate() );
      lFlightLegEntity.setActualArrivalDate( aFlight.getArrivalDate() );

      lFlightLegEntity.setUsageRecord( generateUsageAdjustment( aFlight ) );

      new JdbcFlightLegDao().persist( lFlightLegEntity );

      return lFlightLegId;
   }


   private static UsageAdjustmentId generateUsageAdjustment( final Flight aFlight ) {

      // Return usage adjustment record id, if provided.
      if ( aFlight.getUsageRecord() != null ) {

         if ( !aFlight.getUsages().isEmpty() ) {
            throw new RuntimeException(
                  "Both the usage record id and usages are set, can only use one or the other." );
         }

         return aFlight.getUsageRecord();
      }

      // Create a usage adjustment record and return its key, if usages provided.
      UsageAdjustmentId lUsageId =
            Domain.createUsageAdjustment( new DomainConfiguration<UsageAdjustment>() {

               @Override
               public void configure( UsageAdjustment aUsageAdjustment ) {
                  aUsageAdjustment.setMainInventory( aFlight.getAircraft() );
                  aUsageAdjustment.setUsageDate( aFlight.getArrivalDate() );
                  aUsageAdjustment.setUsageType( UsageType.MXFLIGHT );

                  for ( Flight.UsageInfo lUsage : aFlight.getUsages() ) {
                     aUsageAdjustment.addUsage( lUsage.getInventory(), lUsage.getType(),
                           lUsage.getTsn(), lUsage.getValue() );
                  }
               }
            } );

      return lUsageId;
   }
}
