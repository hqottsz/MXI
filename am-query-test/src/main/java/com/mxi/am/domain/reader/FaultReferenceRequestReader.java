package com.mxi.am.domain.reader;

import java.util.Optional;

import com.mxi.am.domain.FaultReferenceRequest;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.maintenance.exec.fault.infra.JdbcFaultRepository;


public class FaultReferenceRequestReader {

   /**
    *
    * Gets fault reference request domain object for the provided fault key.
    *
    */
   public static FaultReferenceRequest read( FaultKey faultKey ) {

      if ( faultKey == null ) {
         throw new RuntimeException( "FaultKey must be provided" );
      }

      FaultRepository faultRepository = new JdbcFaultRepository();

      Optional<com.mxi.mx.core.maintenance.exec.fault.domain.Fault> fault =
            faultRepository.get( faultKey );
      if ( fault.isPresent() ) {

         Optional<com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference> reference =
               fault.get().getCurrentFaultReference();
         if ( reference.isPresent() ) {

            Optional<com.mxi.mx.core.maintenance.exec.fault.domain.FaultReferenceRequest> request =
                  reference.get().getFaultReferenceRequest();
            if ( request.isPresent() ) {

               FaultReferenceRequest domainRequest = new FaultReferenceRequest();
               domainRequest.setRequestStatus( request.get().getStatusKey().getCd() );
               domainRequest.setDateRequested( request.get().getDateRequested() );
               return domainRequest;
            }
         }
      }

      return null;
   }

}
