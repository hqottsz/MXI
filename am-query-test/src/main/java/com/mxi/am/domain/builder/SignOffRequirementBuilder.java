package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.am.domain.Domain;
import com.mxi.am.domain.SignOffRequirement;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.table.sched.SchedWPSignReqTable;
import com.mxi.mx.core.table.sched.SchedWPSignTable;


public class SignOffRequirementBuilder {

   public static SchedWPSignReqKey build( SignOffRequirement signOffRequirement ) {

      SchedWPSignReqTable signReqRow = SchedWPSignReqTable.create();
      signReqRow.setTask( signOffRequirement.getWorkpackage() );
      signReqRow.setRefWPSignReqStatus( signOffRequirement.getStatus() );
      signReqRow.setLabourSkill( signOffRequirement.getSkill() );
      SchedWPSignReqKey key = signReqRow.insert();

      if ( signOffRequirement.getSignedBy().isPresent()
            || signOffRequirement.getSignOffDate().isPresent() ) {
         // In order to populate a row in sched_wp_sign both the signed-by and sign-off-date must be
         // provided.
         // So a default value will be used for the other if only one is provided.
         SchedWPSignTable signRow = SchedWPSignTable.create( key );
         signRow.setHr( signOffRequirement.getSignedBy().orElse( Domain.createHumanResource() ) );
         signRow.setSignOffDate( signOffRequirement.getSignOffDate().orElse( new Date() ) );
         signRow.insert();
      }

      return key;
   }
}
