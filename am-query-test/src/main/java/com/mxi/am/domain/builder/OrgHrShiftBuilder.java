package com.mxi.am.domain.builder;

import java.util.Date;

import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrShiftKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.org.OrgHrShift;


/**
 * Builds a <code>org_hr_shift</code> object
 */
public class OrgHrShiftBuilder {

   private HumanResourceKey iHrKey;
   private Date iDay;
   private ShiftKey iShiftKey;
   private RefLabourSkillKey iLabourSkill;
   private LocationKey iLocationKey;
   private DepartmentKey iDepartmentKey;


   /**
    * {@inheritDoc}
    */
   public OrgHrShiftKey build() {

      OrgHrShift lTable = OrgHrShift.create();
      lTable.setDayDt( iDay );
      lTable.setShiftKey( iShiftKey );
      lTable.setLabourSkillKey( iLabourSkill );
      lTable.setLocationKey( iLocationKey );
      lTable.setCrew( iDepartmentKey );
      lTable.insert( ( OrgHrShift.generatePrimaryKey( iHrKey ) ) );

      return lTable.getPk();

   }


   public OrgHrShiftBuilder withHumanResourceKey( HumanResourceKey aHrKey ) {

      iHrKey = aHrKey;

      return this;
   }


   public OrgHrShiftBuilder withDay( Date aDay ) {
      iDay = aDay;

      return this;
   }


   public OrgHrShiftBuilder withShiftKey( ShiftKey aShiftKey ) {
      iShiftKey = aShiftKey;

      return this;
   }


   public OrgHrShiftBuilder withLabourSkill( RefLabourSkillKey aLabourSkill ) {
      iLabourSkill = aLabourSkill;
      return this;
   }


   public OrgHrShiftBuilder withLocationKey( LocationKey aLocationKey ) {
      iLocationKey = aLocationKey;
      return this;
   }


   public OrgHrShiftBuilder withDepartmentKey( DepartmentKey aDepartmentKey ) {
      iDepartmentKey = aDepartmentKey;

      return this;
   }
}
