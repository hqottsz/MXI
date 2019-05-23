package com.mxi.am.domain.builder;

import com.mxi.am.domain.OrganizationAuthority;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.AuthorityKey;


/**
 * Domain builder for authority.
 *
 */
public class OrganizationAuthorityBuilder {

   private static final String AUTHORITY_TABLE = "org_authority";
   private static final String AUTHORITY_DB_ID_COLUMN = "authority_db_id";
   private static final String AUTHORITY_ID_COLUMN = "authority_id";
   private static final String AUTHORITY_CODE_COLUMN = "authority_cd";
   private static final String AUTHORITY_NAME_COLUMN = "authority_name";


   public static AuthorityKey build( OrganizationAuthority authority ) {
      AuthorityKey authorityKey = authority.getAuthorityKey();
      if ( authorityKey == null ) {
         final int authorityDbId = Table.Util.getDatabaseId();
         // Get the next ID from the sequence
         final int authorityId = EjbFactory.getInstance().createSequenceGenerator()
               .nextValue( "ORG_AUTHORITY_ID_SEQ" );
         authorityKey = new AuthorityKey( authorityDbId, authorityId );
      }

      DataSetArgument dataSetArguments = new DataSetArgument();
      dataSetArguments.add( authorityKey, AUTHORITY_DB_ID_COLUMN, AUTHORITY_ID_COLUMN );
      dataSetArguments.add( AUTHORITY_CODE_COLUMN, authority.getAuthorityCode() );
      dataSetArguments.add( AUTHORITY_NAME_COLUMN, authority.getAuthorityName() );

      MxDataAccess.getInstance().executeInsert( AUTHORITY_TABLE, dataSetArguments );
      return authorityKey;
   }

}
