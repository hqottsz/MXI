
package com.mxi.am.domain.builder;

import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.common.table.TestTable;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.table.org.OrgOrgTable;


/**
 * Use OrganizationBuilder instead.
 */
@Deprecated
public class OrganizationDomainBuilder implements DomainBuilder<OrgKey> {

   private String iCode = null;
   private String iDescription = null;
   private OrgKey iParentOrgKey;
   private RefOrgTypeKey iType = RefOrgTypeKey.OPERATOR;


   /**
    * Creates a new {@linkplain OrganizationDomainBuilder} object.
    */
   public OrganizationDomainBuilder() {
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public OrgKey build() {
      if ( iCode == null ) {
         throw new MxRuntimeException( "Code cannot be null." );
      }

      if ( iParentOrgKey == null ) {
         // Use the default, root organization "MXI".
         iParentOrgKey = new OrgKey( 0, 1 );
      }

      OrgKey lPrimaryKey = generatePrimaryKey();
      TestTable<OrgKey> lTable = TestTable.create( lPrimaryKey );

      lTable.set( OrgOrgTable.ColumnName.ORG_CD, iCode );
      lTable.set( OrgOrgTable.ColumnName.ORG_SDESC, iDescription );
      lTable.set( OrgOrgTable.ColumnName.ORG_TYPE_DB_ID, iType.getDbId() );
      lTable.set( OrgOrgTable.ColumnName.ORG_TYPE_CD, iType.getCd() );
      lTable.set( OrgOrgTable.ColumnName.NH_ORG_DB_ID, iParentOrgKey.getDbId() );
      lTable.set( OrgOrgTable.ColumnName.NH_ORG_ID, iParentOrgKey.getId() );
      lTable.set( OrgOrgTable.ColumnName.COMPANY_ORG_DB_ID, lPrimaryKey.getDbId() );
      lTable.set( OrgOrgTable.ColumnName.COMPANY_ORG_ID, lPrimaryKey.getId() );

      return lTable.insert();
   }


   /**
    * Sets the organization code.
    *
    * @param aCode
    *           The organization code
    *
    * @return The builder
    */
   public OrganizationDomainBuilder withCode( String aCode ) {
      iCode = aCode;

      return this;
   }


   /**
    * Sets the organization description.
    *
    * @param aDescription
    *           The description
    *
    * @return The builder
    */
   public OrganizationDomainBuilder withDescription( String aDescription ) {
      iDescription = aDescription;

      return this;
   }


   /**
    * Sets the organization type.
    *
    * @param aType
    *           The type
    *
    * @return The builder
    */
   public OrganizationDomainBuilder withType( RefOrgTypeKey aType ) {
      iType = aType;

      return this;
   }


   /**
    * Sets the parent organization.
    *
    * @param aParentOrgKey
    *           The parent organization
    *
    * @return The builder
    */
   public OrganizationDomainBuilder withParentOrganization( OrgKey aParentOrgKey ) {
      iParentOrgKey = aParentOrgKey;

      return this;
   }


   /**
    * Generates a new organization primary key
    *
    * @return OrgKey
    */
   private static OrgKey generatePrimaryKey() {
      try {

         // Get the next id from the sequence
         int lOrganizationDbId = Table.Util.getDatabaseId();
         int lNextOrganizationId =
               EjbFactory.getInstance().createSequenceGenerator().nextValue( "ORG_ID" );

         // Instantiate the table primary key as a proper key
         return new OrgKey( lOrganizationDbId, lNextOrganizationId );
      } catch ( Exception lException ) {
         throw new MxRuntimeException( "Could not generate Organization id", lException );
      }
   }
}
