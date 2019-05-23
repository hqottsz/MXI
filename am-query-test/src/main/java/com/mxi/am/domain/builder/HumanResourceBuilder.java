package com.mxi.am.domain.builder;

import com.mxi.am.domain.HumanResource;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrSupplyKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgHrSupplyTable;
import com.mxi.mx.core.table.org.OrgHrTable;


/**
 * Domain builder for human resource entity. Performs inserts on ORG_HR and ORG_ORG_HR.
 *
 */
public class HumanResourceBuilder {

   private static final String ORG_ORG_HR_TABLE = "org_org_hr";
   private static final String ORG_HR_TABLE = "org_hr";
   private static final String ORG_DB_ID_COLUMN = "org_db_id";
   private static final String ORG_ID_COLUMN = "org_id";
   private static final String DEFAULT_ORG_BOOL = "default_org_bool";
   private static final String ALL_AUTHORITY_BOOL_COLUMN = "all_authority_bool";
   private static final String HR_DB_ID_COLUMN = "hr_db_id";
   private static final String HR_ID_COLUMN = "hr_id";
   private static final String HR_ALT_ID_COLUMN = "alt_id";
   private static final String USER_ID_COLUMN = "user_id";
   private static final String HR_CODE_COLUMN = "hr_cd";


   public static HumanResourceKey build( HumanResource aHumanResource ) {

      UserKey lUserKey = aHumanResource.getUser();
      if ( lUserKey == null && aHumanResource.getUsername() != null ) {
         lUserKey = buildUtlUser( aHumanResource.getUsername() );
      }

      // create a record in org_hr table
      HumanResourceKey lHumanResourceKey = OrgHrTable.generatePrimaryKey();
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lHumanResourceKey, HR_DB_ID_COLUMN, HR_ID_COLUMN );
      lArgs.add( ALL_AUTHORITY_BOOL_COLUMN, aHumanResource.hasAllAuthority().orElse( false ) );
      if ( lUserKey != null ) {
         lArgs.add( USER_ID_COLUMN, lUserKey.toString() );
      }
      if ( aHumanResource.getAltId() != null ) {
         lArgs.add( HR_ALT_ID_COLUMN, aHumanResource.getAltId().toString() );
      }

      if ( aHumanResource.getHrCode() != null ) {
         lArgs.add( HR_CODE_COLUMN, aHumanResource.getHrCode() );
      }

      if ( MxDataAccess.getInstance().executeInsert( ORG_HR_TABLE, lArgs ) != 1 ) {
         throw new RuntimeException( "Error creating human resource" );
      }

      // create a record in org_org_hr table
      lArgs.clear();
      if ( aHumanResource.getOrganization() != null ) {

         lArgs.add( aHumanResource.getOrganization(), ORG_DB_ID_COLUMN, ORG_ID_COLUMN );
         lArgs.add( lHumanResourceKey, HR_DB_ID_COLUMN, HR_ID_COLUMN );

         if ( aHumanResource.isDefaultOrg() != null ) {
            lArgs.add( DEFAULT_ORG_BOOL, aHumanResource.isDefaultOrg() ? "1" : "0" );
         }

         if ( MxDataAccess.getInstance().executeInsert( ORG_ORG_HR_TABLE, lArgs ) != 1 ) {
            throw new RuntimeException(
                  "Error associating Human resource with the provided organization" );
         }
      }

      // create record in org_hr_supply table
      if ( !aHumanResource.getSupplyLocations().isEmpty() ) {

         for ( LocationKey lSupplyLocation : aHumanResource.getSupplyLocations() ) {
            OrgHrSupplyKey lHrSupplyKey =
                  new OrgHrSupplyKey( lSupplyLocation.getDbId(), lSupplyLocation.getId(),
                        lUserKey.getId(), lHumanResourceKey.getDbId(), lHumanResourceKey.getId() );
            OrgHrSupplyTable lOrgHrSupply = new OrgHrSupplyTable();
            lOrgHrSupply.insert( lHrSupplyKey );
         }

      }

      // create records in org_hr_authority table
      if ( !aHumanResource.getAuthorities().isEmpty() ) {
         aHumanResource.getAuthorities().stream().forEach( aAuthority -> {
            DataSetArgument lInsertArgs = new DataSetArgument();
            lInsertArgs.add( lHumanResourceKey, "hr_db_id", "hr_id" );
            lInsertArgs.add( aAuthority, "authority_db_id", "authority_id" );
            MxDataAccess.getInstance().executeInsert( "org_hr_authority", lInsertArgs );
         } );
      }
      return lHumanResourceKey;

   }


   private static UserKey buildUtlUser( String aUsername ) {
      UserKey lUserKey = new UserKey(
            EjbFactory.getInstance().createSequenceGenerator().nextValue( "UTL_USER_ID" ) );
      DataSetArgument args = new DataSetArgument();
      args.add( lUserKey, "user_id" );
      args.add( "username", aUsername );
      args.add( "utl_id", Table.Util.getDatabaseId() );
      MxDataAccess.getInstance().executeInsert( "utl_user", args );
      return lUserKey;
   }

}
