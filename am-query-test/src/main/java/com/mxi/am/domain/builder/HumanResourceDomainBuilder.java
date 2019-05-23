
package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgHrSupplyKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgHrSupplyTable;
import com.mxi.mx.core.table.org.OrgHrTable;


/**
 * Use HumanResourceBuilder instead.
 */
@Deprecated
public class HumanResourceDomainBuilder implements DomainBuilder<HumanResourceKey> {

   private Integer iUserId;

   private String iUsername;

   private final List<LocationKey> iSupplyLocations = new ArrayList<LocationKey>();

   private OrgKey iOrganization;

   private String iFirstName;

   private String iLastName;


   /**
    * {@inheritDoc}
    */
   @Override
   public HumanResourceKey build() {

      UserKey lUserKey = ( iUserId != null ) ? new UserKey( iUserId ) : generateUserKey();

      DataSetArgument lUserArgs = new DataSetArgument();
      lUserArgs.add( lUserKey, "user_id" );
      lUserArgs.add( "username", iUsername );
      lUserArgs.add( "utl_id", Table.Util.getDatabaseId() );
      // first name and last name are mandatory in the UserBuilder (inner class of User)

      if ( iFirstName == null ) {
         lUserArgs.add( "first_name", "John" );
      } else {
         lUserArgs.add( "first_name", iFirstName );
      }

      if ( iLastName == null ) {
         lUserArgs.add( "last_name", "Smith" );
      } else {
         lUserArgs.add( "last_name", iLastName );
      }

      MxDataAccess.getInstance().executeInsert( "utl_user", lUserArgs );

      OrgHrTable lTable = OrgHrTable.create();
      lTable.setUserId( lUserKey );

      HumanResourceKey lHumanResourceKey = lTable.insert();

      for ( LocationKey lSupplyLocation : iSupplyLocations ) {
         OrgHrSupplyKey lHrSupplyLink =
               new OrgHrSupplyKey( lSupplyLocation.getDbId(), lSupplyLocation.getId(),
                     lUserKey.getId(), lHumanResourceKey.getDbId(), lHumanResourceKey.getId() );
         OrgHrSupplyTable lOrgHrSupply = new OrgHrSupplyTable();
         lOrgHrSupply.insert( lHrSupplyLink );
      }

      if ( iOrganization != null ) {
         DataSetArgument lArgs = iOrganization.getPKWhereArg();
         lArgs.add( lHumanResourceKey.getPKWhereArg() );
         MxDataAccess.getInstance().executeInsert( "org_org_hr", lArgs );
      }

      return lHumanResourceKey;

   }


   /**
    * Sets the user id
    *
    * @param aUserId
    *           The user id
    *
    * @return The builder
    */
   public HumanResourceDomainBuilder withUserId( int aUserId ) {
      iUserId = aUserId;

      return this;
   }


   public HumanResourceDomainBuilder withFirstName( String aFirstName ) {
      iFirstName = aFirstName;

      return this;
   }


   public HumanResourceDomainBuilder withLastName( String aLastName ) {
      iLastName = aLastName;

      return this;
   }


   /**
    * Sets the user's username.
    *
    * @param aUsername
    *           The username
    *
    * @return The builder
    */
   public HumanResourceDomainBuilder withUsername( String aUsername ) {
      iUsername = aUsername;

      return this;
   }


   /**
    * Links the user to the given supply location.
    *
    * @param aSupplyLocation
    *           The supply location
    *
    * @return The builder
    */
   public HumanResourceDomainBuilder withSupplyLocation( LocationKey aSupplyLocation ) {
      iSupplyLocations.add( aSupplyLocation );

      return this;
   }


   public HumanResourceDomainBuilder inOrganization( OrgKey aOrganization ) {
      iOrganization = aOrganization;

      return this;
   }


   /**
    * Generates a new primary key for the utl_user table.
    *
    * @return A new UserKey object.
    */
   private UserKey generateUserKey() {

      try {

         // Get the next id from the sequence
         int lNextId =
               EjbFactory.getInstance().createSequenceGenerator().nextValue( "UTL_USER_ID" );

         // Instantiate the table primary key as a proper key
         return new UserKey( lNextId );
      } catch ( Exception lException ) {
         throw new MxRuntimeException( "Could not generate user id", lException );
      }
   }
}
