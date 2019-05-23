package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.SchedLabourKey;


/**
 * Represents a role that a human resource performs when completing labour for a task.
 *
 */
public class LabourRole {

   private SchedLabourKey labour;
   private RefLabourRoleTypeKey type;
   private HumanResourceKey humanResouce;
   private RefLabourRoleStatusKey status;
   private BigDecimal scheduledHours;
   private BigDecimal actualHours;
   private Date actualStartDate;
   private Date actualEndDate;


   public SchedLabourKey getLabour() {
      return labour;
   }


   public LabourRole setLabour( SchedLabourKey labour ) {
      this.labour = labour;
      return this;
   }


   public RefLabourRoleTypeKey getType() {
      return type;
   }


   public LabourRole setType( RefLabourRoleTypeKey type ) {
      this.type = type;
      return this;
   }


   public HumanResourceKey getHumanResouce() {
      return humanResouce;
   }


   public LabourRole setHumanResouce( HumanResourceKey humanResouce ) {
      this.humanResouce = humanResouce;
      return this;
   }


   public Optional<RefLabourRoleStatusKey> getStatus() {
      return Optional.ofNullable( status );
   }


   public LabourRole setStatus( RefLabourRoleStatusKey status ) {
      this.status = status;
      return this;
   }


   public Optional<BigDecimal> getScheduledHours() {
      return Optional.ofNullable( scheduledHours );
   }


   public LabourRole setScheduledHours( BigDecimal scheduledHours ) {
      this.scheduledHours = scheduledHours;
      return this;
   }


   public LabourRole setScheduledHours( int scheduledHours ) {
      return setScheduledHours( BigDecimal.valueOf( scheduledHours ) );
   }


   public Optional<BigDecimal> getActualHours() {
      return Optional.ofNullable( actualHours );
   }


   public LabourRole setActualHours( BigDecimal actualHours ) {
      this.actualHours = actualHours;
      return this;
   }


   public LabourRole setActualHours( int actualHours ) {
      return setActualHours( BigDecimal.valueOf( actualHours ) );
   }


   public Date getActualStartDate() {
      return actualStartDate;
   }


   public LabourRole setActualStartDate( Date actualStartDate ) {
      this.actualStartDate = actualStartDate;
      return this;
   }


   public Date getActualEndDate() {
      return actualEndDate;
   }


   public LabourRole setActualEndDate( Date actualEndDate ) {
      this.actualEndDate = actualEndDate;
      return this;
   }

}
