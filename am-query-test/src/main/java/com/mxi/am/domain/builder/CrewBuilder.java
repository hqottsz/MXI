package com.mxi.am.domain.builder;

import com.mxi.am.domain.Crew;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.table.org.OrgWorkDept;


public class CrewBuilder {

   public static DepartmentKey build( Crew crew ) {

      OrgWorkDept orgWorkDept = OrgWorkDept.create();
      orgWorkDept.setType( RefDeptTypeKey.CREW );
      orgWorkDept.setDeptCd( crew.getCode() );
      orgWorkDept.setName( crew.getName() );
      DepartmentKey departmentKey = orgWorkDept.insert();

      DataSetArgument args;

      for ( HumanResourceKey hr : crew.getHumanResources() ) {
         args = new DataSetArgument();
         args.add( departmentKey, "dept_db_id", "dept_id" );
         args.add( hr, "hr_db_id", "hr_id" );
         MxDataAccess.getInstance().executeInsert( "org_dept_hr", args );
      }

      for ( LocationKey lLocation : crew.getLocations() ) {
         args = new DataSetArgument();
         args.add( departmentKey, "dept_db_id", "dept_id" );
         args.add( lLocation, "loc_db_id", "loc_id" );
         MxDataAccess.getInstance().executeInsert( "inv_loc_dept", args );
      }

      return departmentKey;
   }

}
