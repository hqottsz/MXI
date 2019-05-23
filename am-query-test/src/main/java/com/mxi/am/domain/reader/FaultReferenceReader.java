package com.mxi.am.domain.reader;

import java.util.Optional;

import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.maintenance.exec.fault.domain.FaultRepository;
import com.mxi.mx.core.maintenance.exec.fault.infra.JdbcFaultRepository;


/**
 * Gets the FaultReferenceKey of the current reference for the provided fault.
 *
 */
public class FaultReferenceReader {

   /**
    *
    * Gets fault reference domain object for the provided fault key.
    *
    */
   public static FaultReferenceKey read( FaultKey faultKey ) {

      if ( faultKey == null ) {
         throw new IllegalArgumentException( "FaultKey must be provided." );
      }

      FaultRepository faultRepository = new JdbcFaultRepository();

      Optional<com.mxi.mx.core.maintenance.exec.fault.domain.Fault> faultOptional =
            faultRepository.get( faultKey );

      if ( faultOptional.isPresent() ) {
         Optional<com.mxi.mx.core.maintenance.exec.fault.domain.FaultReference> referenceOptional =
               faultOptional.get().getCurrentFaultReference();

         if ( referenceOptional.isPresent() ) {

            return referenceOptional.get().getFaultReferenceKey();
         }
      }
      return null;
   }

}
