package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;


public class AdhocTask {

   private InventoryKey inventory;
   private RefEventStatusKey status = RefEventStatusKey.ACTV;
   private TaskKey previousTask;
   private FaultKey relatedFault;
   private DepartmentKey crew;
   private OrgCrewShiftPlanKey crewShiftDay;
   private Boolean isCompleted;
   private Boolean isSoftDeadline;
   private LocationKey locationKey;
   private List<DomainConfiguration<Labour>> labourConfigs = new ArrayList<>();
   private List<DomainConfiguration<PartRequirement>> partRequirementConfigs = new ArrayList<>();
   private List<DomainConfiguration<Deadline>> deadlines = new ArrayList<>();
   private List<String> steps = new ArrayList<>();


   public InventoryKey getInventory() {
      return inventory;
   }


   public void setInventory( InventoryKey inventory ) {
      this.inventory = inventory;
   }


   public void setStatus( RefEventStatusKey status ) {
      this.status = status;
   }


   public RefEventStatusKey getStatus() {
      return status;
   }


   public void setPreviousTask( TaskKey previousTask ) {
      this.previousTask = previousTask;
   }


   public TaskKey getPreviousTask() {
      return previousTask;
   }


   public void setRelatedFault( FaultKey fault ) {
      relatedFault = fault;
   }


   public FaultKey getRelatedFault() {
      return relatedFault;
   }


   public void setCrew( DepartmentKey crew ) {
      this.crew = crew;
   }


   public Optional<DepartmentKey> getCrew() {
      return Optional.ofNullable( crew );
   }


   public Optional<OrgCrewShiftPlanKey> getCrewShiftDay() {
      return Optional.ofNullable( crewShiftDay );
   }


   public void setCrewShiftDay( OrgCrewShiftPlanKey crewShiftDay ) {
      this.crewShiftDay = crewShiftDay;
   }


   public Boolean isCompleted() {
      return isCompleted;
   }


   public void setIsCompleted( boolean value ) {
      isCompleted = value;
   }


   public void addLabour( DomainConfiguration<Labour> labourConfiguration ) {
      labourConfigs.add( labourConfiguration );
   }


   /**
    * Adds a labour when its attributes are not relevant.
    *
    */
   public void addLabour() {
      // Add and empty configuration.
      labourConfigs.add( new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aBuilder ) {
            // no-op
         }
      } );
   }


   public List<DomainConfiguration<Labour>> getLabourConfigurations() {
      return labourConfigs;
   }


   public void
         addPartRequirement( DomainConfiguration<PartRequirement> partRequirementConfiguration ) {
      partRequirementConfigs.add( partRequirementConfiguration );
   }


   public List<DomainConfiguration<PartRequirement>> getPartRequirementConfigurations() {
      return partRequirementConfigs;
   }


   public void addStep( String step ) {
      steps.add( step );
   }


   public List<String> getSteps() {
      return steps;
   }


   public Optional<Boolean> isSoftDeadline() {
      return Optional.ofNullable( isSoftDeadline );
   }


   public void setSoftDeadline( Boolean value ) {
      this.isSoftDeadline = value;
   }


   public List<DomainConfiguration<Deadline>> getDeadlines() {
      return deadlines;
   }


   public void addDeadline( DomainConfiguration<Deadline> deadline ) {
      deadlines.add( deadline );
   }


   public Optional<LocationKey> getLocation() {
      return Optional.ofNullable( locationKey );
   }


   public void setLocationKey( LocationKey locationKey ) {

      this.locationKey = locationKey;
   }

}
