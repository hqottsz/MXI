package com.mxi.am.domain.builder;

import com.mxi.am.domain.Organization;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.table.org.OrgOrgTable;


/**
 * Domain builder for an organization
 *
 */
public class OrganizationBuilder {

   private static final String ORG_ORG_TABLE = "org_org";
   private static final String ORG_DB_ID_COLUMN = "org_db_id";
   private static final String ORG_ID_COLUMN = "org_id";
   private static final String ORG_ALT_ID_COLUMN = "alt_id";


   public static OrgKey build( Organization aOrganization ) {

      OrgKey lOrgKey = aOrganization.getOrgKey();;
      if ( aOrganization.getOrgKey() == null ) {
         int lOrgDbId = Table.Util.getDatabaseId();
         // Get the next ID from the sequence
         int lOrgId = EjbFactory.getInstance().createSequenceGenerator().nextValue( "ORG_ID_SEQ" );
         lOrgKey = new OrgKey( lOrgDbId, lOrgId );
      }

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lOrgKey, ORG_DB_ID_COLUMN, ORG_ID_COLUMN );
      if ( aOrganization.getAltId() != null ) {
         lArgs.add( ORG_ALT_ID_COLUMN, aOrganization.getAltId() );
      }

      if ( MxDataAccess.getInstance().executeInsert( ORG_ORG_TABLE, lArgs ) == 1 ) {
         OrgOrgTable lOrgOrgTable = OrgOrgTable.findByPrimaryKey( lOrgKey );
         lOrgOrgTable.setOrgCode( aOrganization.getCode() );
         lOrgOrgTable.setOrgSDesc( aOrganization.getName() );
         lOrgOrgTable.setOrgType( aOrganization.getType() );
         lOrgOrgTable.update();
         return lOrgKey;
      }
      throw new RuntimeException( "Error creating organization" );

   }

}
