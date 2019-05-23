package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.am.domain.FaultReferenceRequest;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.FaultReferenceRequestKey;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestDao;
import com.mxi.mx.core.maintenance.exec.fault.infra.FaultReferenceRequestTableRow;


public class FaultReferenceRequestBuilder {

   private static final String PENDING = "PENDING";

   static FaultReferenceDao jdbcFaultReferenceDao =
         InjectorContainer.get().getInstance( FaultReferenceDao.class );
   static FaultReferenceRequestDao jdbcFaultReferenceRequestDao =
         InjectorContainer.get().getInstance( FaultReferenceRequestDao.class );


   public static FaultReferenceRequestKey build( FaultReferenceRequest faultReferenceRequest ) {

      FaultReferenceRequestKey faultReferenceRequestKey =
            jdbcFaultReferenceRequestDao.generatePrimaryKey();
      FaultReferenceRequestTableRow faultReferenceRequestTableRow;

      // Get request from current fault reference
      if ( faultReferenceRequest.getReferenceRequestByFaultsCurrentRequest() != null ) {
         FaultKey faultKey = faultReferenceRequest.getReferenceRequestByFaultsCurrentRequest();
         FaultReferenceKey faultReferenceKey =
               jdbcFaultReferenceDao.getCurrentReferenceByFaultId( faultKey );
         faultReferenceRequestTableRow = jdbcFaultReferenceRequestDao
               .create( new FaultReferenceRequestKey( faultReferenceKey.toValueString() ) );
      } else if ( faultReferenceRequest.getReferenceRequestByReferenceKey() != null ) {
         FaultReferenceKey faultReferenceKey =
               faultReferenceRequest.getReferenceRequestByReferenceKey();
         faultReferenceRequestTableRow = jdbcFaultReferenceRequestDao
               .create( new FaultReferenceRequestKey( faultReferenceKey.toValueString() ) );
      } else {
         faultReferenceRequestTableRow =
               jdbcFaultReferenceRequestDao.create( faultReferenceRequestKey );
      }

      // Set the status
      faultReferenceRequestTableRow
            .setRequestStatus( faultReferenceRequest.getRequestStatus() == null ? PENDING
                  : faultReferenceRequest.getRequestStatus() );

      // Set the start and complete dates
      faultReferenceRequestTableRow
            .setDateRequested( faultReferenceRequest.getDateRequested() == null ? new Date()
                  : faultReferenceRequest.getDateRequested() );
      faultReferenceRequestTableRow.setDateResolved( faultReferenceRequest.getDateResolved() == null
            ? new Date() : faultReferenceRequest.getDateResolved() );

      // Set the human resource key
      faultReferenceRequestTableRow.setHumanResource( faultReferenceRequest.getHumanResourceKey() );

      // Commit
      jdbcFaultReferenceRequestDao.insert( faultReferenceRequestTableRow );

      return faultReferenceRequestKey;

   }
}
