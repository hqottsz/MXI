package com.mxi.am.domain;

import java.util.Date;
import java.util.Optional;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefWPSignReqStatusKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * Sign off requirement of a work package.
 */
public class SignOffRequirement {

   TaskKey workpackage;
   RefWPSignReqStatusKey status;
   RefLabourSkillKey skill;
   HumanResourceKey signedBy;
   Date signOffDate;


   public TaskKey getWorkpackage() {
      return workpackage;
   }


   public void setWorkpackage( TaskKey workpackage ) {
      this.workpackage = workpackage;
   }


   public RefWPSignReqStatusKey getStatus() {
      return status;
   }


   public void setStatus( RefWPSignReqStatusKey status ) {
      this.status = status;
   }


   public RefLabourSkillKey getSkill() {
      return skill;
   }


   public void setSkill( RefLabourSkillKey skill ) {
      this.skill = skill;
   }


   public Optional<HumanResourceKey> getSignedBy() {
      return Optional.ofNullable( signedBy );
   }


   public void setSignedBy( HumanResourceKey signedBy ) {
      this.signedBy = signedBy;
   }


   public Optional<Date> getSignOffDate() {
      return Optional.ofNullable( signOffDate );
   }


   public void setSignOffDate( Date signOffDate ) {
      this.signOffDate = signOffDate;
   }

}
