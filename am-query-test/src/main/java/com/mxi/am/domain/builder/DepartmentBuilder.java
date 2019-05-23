
package com.mxi.am.domain.builder;

import java.util.HashSet;
import java.util.Set;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.DataAccessObject;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefDeptTypeKey;
import com.mxi.mx.core.table.org.OrgWorkDept;


/**
 * Builds a <code>org_work_dept</code> object.
 */
public class DepartmentBuilder implements DomainBuilder<DepartmentKey> {

   private String iDeptCode;
   private RefDeptTypeKey iType;
   private Set<HumanResourceKey> iUsers = new HashSet<HumanResourceKey>();
   private Set<LocationKey> iLocations = new HashSet<LocationKey>();
   private DataAccessObject iDao = MxDataAccess.getInstance();


   /**
    * Creates a new {@linkplain DepartmentBuilder} object.
    *
    * @param aUserKey
    */
   public DepartmentBuilder(String aDeptCode) {
      super();
      iDeptCode = aDeptCode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public DepartmentKey build() {

      if ( iDeptCode == null ) {
         throw new MxRuntimeException(
               "DepartmentBuilder requires a valid department code to be set." );
      }

      OrgWorkDept lOrgWorkDept = OrgWorkDept.create();
      lOrgWorkDept.setDeptCd( iDeptCode );
      lOrgWorkDept.setType( iType );

      DepartmentKey lDepartmentKey = lOrgWorkDept.insert();

      DataSetArgument lArgs;

      for ( HumanResourceKey lUser : iUsers ) {
         lArgs = new DataSetArgument();
         lArgs.add( lDepartmentKey, "dept_db_id", "dept_id" );
         lArgs.add( lUser, "hr_db_id", "hr_id" );
         iDao.executeInsert( "org_dept_hr", lArgs );
      }

      for ( LocationKey lLocation : iLocations ) {
         lArgs = new DataSetArgument();
         lArgs.add( lDepartmentKey, "dept_db_id", "dept_id" );
         lArgs.add( lLocation, "loc_db_id", "loc_id" );
         iDao.executeInsert( "inv_loc_dept", lArgs );
      }

      return lDepartmentKey;
   }


   public DepartmentBuilder withUser( HumanResourceKey aUser ) {
      iUsers.add( aUser );

      return this;
   }


   public DepartmentBuilder withLocation( LocationKey aLocation ) {
      iLocations.add( aLocation );

      return this;
   }


   public DepartmentBuilder withType( RefDeptTypeKey aType ) {
      iType = aType;

      return this;
   }

}
