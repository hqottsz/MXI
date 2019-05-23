
package com.mxi.am.domain.builder;

import java.util.Map;

import com.mxi.am.domain.PartAdvisory;
import com.mxi.am.domain.Vendor;
import com.mxi.mx.common.table.AbstractTable;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OrgVendorPoTypeKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefCurrencyKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefTermsConditionsKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.org.OrgVendor;
import com.mxi.mx.core.table.org.OrgVendorPoTypeTable;
import com.mxi.mx.core.utils.UtlSequence;


/**
 * Builds a <code>org_vendor</code> object
 */
public class VendorBuilder implements DomainBuilder<VendorKey> {

   private String iCode;
   private String iName;
   private LocationKey iLocation =
         new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();
   private RefVendorTypeKey iVendorType;
   private RefCurrencyKey iCurrency;
   private boolean iIsNoPrintReq = false;
   private RefTermsConditionsKey iTermsConditionsKey = null;
   private OwnerKey iOwner = null;


   /**
    * {@inheritDoc}
    */
   @Override
   public VendorKey build() {

      // Get the next id from the sequence
      int lDbId = AbstractTable.getDatabaseId();
      int lNextId = UtlSequence.getNextValue( UtlSequence.Sequence.ORG_VENDOR_SEQ );

      // build the pk.
      VendorKey lNewPk = new VendorKey( lDbId, lNextId );

      OrgVendor lOrgVendor = OrgVendor.create( lNewPk );
      lOrgVendor.setVendorLocation( iLocation );
      lOrgVendor.setVendorName( iName );
      lOrgVendor.setVendorCd( iCode );
      lOrgVendor.setNoPrintReqBool( iIsNoPrintReq );
      lOrgVendor.setTermsConditions( iTermsConditionsKey );

      if ( iOwner != null ) {
         lOrgVendor.setOwner( iOwner );
      }

      if ( iVendorType != null ) {
         lOrgVendor.setVendorType( iVendorType );
      }

      if ( iCurrency != null ) {
         lOrgVendor.setCurrency( iCurrency );
      }

      lOrgVendor.insert();

      return lNewPk;
   }


   /**
    * Sets the value true/false to no_print_req_bool
    *
    * @param aLocation
    *           The location
    *
    * @return The builder
    */
   public VendorBuilder isNoPrintReq( boolean aValue ) {
      iIsNoPrintReq = aValue;

      return this;
   }


   /**
    * Sets the currency.
    *
    * @param aCurrency
    *           The currency
    *
    * @return The builder
    */
   public VendorBuilder withCurrency( RefCurrencyKey aCurrency ) {
      iCurrency = aCurrency;

      return this;
   }


   public VendorBuilder withOwner( OwnerKey aOwner ) {
      iOwner = aOwner;

      return this;
   }


   /**
    * Sets the code
    *
    * @param aCode
    *           The code
    *
    * @return The builder
    */
   public VendorBuilder withCode( String aCode ) {
      iCode = aCode;

      return this;
   }


   /**
    * Sets the name
    *
    * @param aName
    *           The name
    *
    * @return The builder
    */
   public VendorBuilder withName( String aName ) {
      iName = aName;

      return this;
   }


   /**
    *
    * Sets the terms and conditions
    *
    * @param aTermsConditionsKey
    *           the terms and conditions key
    * @return the builder
    */
   public VendorBuilder withTermsAndConditions( RefTermsConditionsKey aTermsConditionsKey ) {
      iTermsConditionsKey = aTermsConditionsKey;

      return this;
   }


   /**
    * Sets the vendor type
    *
    * @param aName
    *           The vendor type
    *
    * @return The builder
    */
   public VendorBuilder withVendorType( RefVendorTypeKey aVendorType ) {
      iVendorType = aVendorType;

      return this;
   }


   /**
    * Sets the location.
    *
    * @param aLocation
    *           The location
    *
    * @return The builder
    */
   public VendorBuilder atLocation( LocationKey aLocation ) {
      iLocation = aLocation;

      return this;
   }


   /**
    * Sets vendor type as broker
    *
    *
    * @return The builder
    */
   public VendorBuilder asBroker() {
      iVendorType = RefVendorTypeKey.BROKER;

      return this;
   }


   public static VendorKey build( Vendor aVendor ) {

      // Get the next id from the sequence
      int lDbId = AbstractTable.getDatabaseId();
      int lNextId = UtlSequence.getNextValue( UtlSequence.Sequence.ORG_VENDOR_SEQ );

      // build the pk.
      VendorKey vendorKey = new VendorKey( lDbId, lNextId );

      OrgVendor lOrgVendor = OrgVendor.create( vendorKey );
      lOrgVendor.setVendorCd( aVendor.getCode() );
      lOrgVendor.setVendorLocation( aVendor.getLocation() );
      lOrgVendor.setVendorType( RefVendorTypeKey.PURCHASE );
      lOrgVendor.insert();

      OrgVendorPoTypeKey orgVendorPoTypeKey =
            new OrgVendorPoTypeKey( OrgKey.ADMIN, vendorKey, RefPoTypeKey.PURCHASE );

      OrgVendorPoTypeTable lOrgVendorPoTypeTable =
            OrgVendorPoTypeTable.create( orgVendorPoTypeKey );
      lOrgVendorPoTypeTable.setVendorStatus( RefVendorStatusKey.APPROVED );
      lOrgVendorPoTypeTable.insert();

      for ( Map.Entry<PartNoKey, PartAdvisory> advisory : aVendor.getVendorPartAdvisories()
            .entrySet() ) {

         if ( advisory.getValue() != null ) {

            PartAdvisory partAdvisory = advisory.getValue();
            partAdvisory.setPartNo( advisory.getKey() );
            partAdvisory.setVendor( vendorKey );

            PartAdvisoryBuilder.build( partAdvisory );
         }
      }

      return vendorKey;
   }
}
