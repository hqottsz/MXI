
package com.mxi.mx.core.query.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.QueryRow;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.erp.hr.user.model.UserAccountId;
import com.mxi.mx.core.erp.organization.model.OrganizationId;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgOrgTable;
import com.mxi.mx.core.table.utl.UtlUser;


/**
 * Unit test for com.mxi.mx.core.erp.hr.user.dao.query.FindAllUserAccounts.qrx.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class FindAllUserAccountsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private QueryAccessObject iQueryAccessObject = QuerySetFactory.getInstance();


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sDatabaseConnectionRule.getConnection(), FindAllUserAccountsTest.class,
            "FindAllUserAccountsTest.sql" );
   }


   /**
    * The query finds all users in the db (that belong to an organisation). There are 4 users in the
    * test db, but one doesn't have an organisation, so 3 should be found. Since the set of users in
    * the test db at any point in time is not deterministic, the test ignores existing users and
    * validates whether the users added via {@link #loadData()} are found.
    */
   @Test
   public void findAllUsersTest() {
      List<MockRow> lExpectedRows = buildMockDataForFindAllUsersTest();
      QuerySet lExecuteQuery = executeQuery();
      List<MockRow> lTestRows = new ArrayList<MockRow>();
      {
         List<? extends QueryRow> lRows = lExecuteQuery.getRows();

         for ( QueryRow lRow : lRows ) {
            MockRow lTestRow = buildMockRow( lRow );

            lTestRows.add( lTestRow );
         }
      }
      int lRowCount = 0;

      for ( MockRow lTestRow : lTestRows ) {
         boolean lFoundRow = lExpectedRows.contains( lTestRow );

         if ( lFoundRow ) {
            lRowCount++;
         }
      }
      Assert.assertEquals( 3, lRowCount );
   }


   /**
    *
    * @param lRow
    *           A {@link QueryRow} mapping to a row in the query's resultset.
    * @return The
    */
   private MockRow buildMockRow( QueryRow lRow ) {
      UUID lUserAltId = lRow.getUuid( "user_alt_id" );
      String lUserName = lRow.getString( "username" );
      String lFirstName = lRow.getString( "first_name" );
      String lLastName = lRow.getString( "last_name" );
      String lMiddleName = lRow.getString( "middle_name" );
      String lEmail = lRow.getString( "email_addr" );
      String lAlertEmail = lRow.getString( "alert_email_addr" );
      long lLockedBool = lRow.getLong( "locked_bool" );
      String lHrLegacyKey = lRow.getString( "hr_legacy_key" );
      String lHrCd = lRow.getString( "hr_cd" );
      UUID lOrgAltId = lRow.getUuid( "org_alt_id" );
      MockRow lTestRow = new MockRow.MockRowBuilder().userAltId( lUserAltId ).userName( lUserName )
            .firstName( lFirstName ).lastName( lLastName ).middleName( lMiddleName ).email( lEmail )
            .alertEmail( lAlertEmail ).lockedBool( lLockedBool ).orgHrKey( lHrLegacyKey )
            .orgHrCd( lHrCd ).orgAltId( lOrgAltId ).build();

      return lTestRow;
   }


   private QuerySet executeQuery() {
      String lQueryName = "com.mxi.mx.core.erp.hr.user.dao.query.FindAllUserAccounts";
      QuerySet lExecuteQuery = iQueryAccessObject.executeQuery( lQueryName );

      return lExecuteQuery;
   }


   /**
    * Builds 3 valid users (i.e. all of which belong to an organisation).
    */
   private List<MockRow> buildMockDataForFindAllUsersTest() {
      List<MockRow> lExpectedRows = new ArrayList<MockRow>();
      {
         // first row
         int lUserId = 999000;
         UUID lUserAltId = generateUUIDFromUserId( lUserId );
         int lOrgId = 997000;
         int lOrgDbId = 0;
         UUID lOrgAltId = generateUUIDFromOrgId( lOrgId, lOrgDbId );
         MockRow lExpectedRow = new MockRow.MockRowBuilder().userAltId( lUserAltId )
               .userName( "user1" ).firstName( "first1" ).lastName( "last1" ).middleName( "mid1" )
               .email( "email1" ).alertEmail( "alert1" ).lockedBool( 0 ).orgHrId( 998000 )
               .orgHrDbId( 0 ).orgHrCd( "org1" ).orgAltId( lOrgAltId ).build();

         lExpectedRows.add( lExpectedRow );

         // second row
         lUserId = 999001;
         lUserAltId = generateUUIDFromUserId( lUserId );
         lOrgId = 997000;
         lOrgDbId = 0;
         lOrgAltId = generateUUIDFromOrgId( lOrgId, lOrgDbId );
         lExpectedRow = new MockRow.MockRowBuilder().userAltId( lUserAltId ).userName( "user2" )
               .firstName( "first2" ).lastName( "last2" ).middleName( "mid2" ).email( "email2" )
               .alertEmail( "alert2" ).lockedBool( 0 ).orgHrId( 998001 ).orgHrDbId( 0 )
               .orgHrCd( "org1" ).orgAltId( lOrgAltId ).build();

         lExpectedRows.add( lExpectedRow );

         // third row
         lUserId = 999002;
         lUserAltId = generateUUIDFromUserId( lUserId );
         lOrgId = 997001;
         lOrgDbId = 0;
         lOrgAltId = generateUUIDFromOrgId( lOrgId, lOrgDbId );
         lExpectedRow = new MockRow.MockRowBuilder().userAltId( lUserAltId ).userName( "user3" )
               .firstName( "first3" ).lastName( "last3" ).middleName( "mid3" ).email( "email3" )
               .alertEmail( "alert3" ).lockedBool( 0 ).orgHrId( 998002 ).orgHrDbId( 0 )
               .orgHrCd( "org2" ).orgAltId( lOrgAltId ).build();

         lExpectedRows.add( lExpectedRow );
      }

      return lExpectedRows;
   }


   private UUID generateUUIDFromUserId( int aUserId ) {
      UserKey lUserKey = new UserKey( aUserId );
      UtlUser lUtlUserTable = UtlUser.findByPrimaryKey( lUserKey );
      UUID lUserAltId = lUtlUserTable.getAlternateKey();

      return lUserAltId;
   }


   private UUID generateUUIDFromOrgId( int aOrgId, int aOrgDbId ) {
      OrgKey lOrgKey = new OrgKey( aOrgDbId, aOrgId );
      OrgOrgTable lOrgTable = OrgOrgTable.findByPrimaryKey( lOrgKey );
      UUID lOrgAltId = lOrgTable.getAlternateKey();

      return lOrgAltId;
   }


   /**
    * Represents a row from the query's result set.
    */
   private static class MockRow {

      // utl_user fields
      private UserAccountId iUserAltId;
      private String iUserName;
      private String iFirstName;
      private String iLastName;
      private String iMiddleName;
      private String iEmail;
      private String iAlertEmail;
      private long iLockedBool;

      // org_hr fields
      private long iOrgHrId;
      private long iOrgHrDbId;
      private String iOrgHrCd;

      // org_org fields
      private OrganizationId iOrgAltId;


      /**
       * Creates a new {@linkplain MockRow} object.
       *
       */
      public MockRow() {
      }


      /**
       * Sets a new value for the userAltId property.
       *
       * @param aUserAltId
       *           the new value for the userAltId property
       */
      public void setUserAltId( UserAccountId aUserAltId ) {
         iUserAltId = aUserAltId;
      }


      /**
       * Sets a new value for the userName property.
       *
       * @param aUserName
       *           the new value for the userName property
       */
      public void setUserName( String aUserName ) {
         iUserName = aUserName;
      }


      /**
       * Sets a new value for the firstName property.
       *
       * @param aFirstName
       *           the new value for the firstName property
       */
      public void setFirstName( String aFirstName ) {
         iFirstName = aFirstName;
      }


      /**
       * Sets a new value for the lastName property.
       *
       * @param aLastName
       *           the new value for the lastName property
       */
      public void setLastName( String aLastName ) {
         iLastName = aLastName;
      }


      /**
       * Sets a new value for the middleName property.
       *
       * @param aMiddleName
       *           the new value for the middleName property
       */
      public void setMiddleName( String aMiddleName ) {
         iMiddleName = aMiddleName;
      }


      /**
       * Sets a new value for the email property.
       *
       * @param aEmail
       *           the new value for the email property
       */
      public void setEmail( String aEmail ) {
         iEmail = aEmail;
      }


      /**
       * Sets a new value for the alertEmail property.
       *
       * @param aAlertEmail
       *           the new value for the alertEmail property
       */
      public void setAlertEmail( String aAlertEmail ) {
         iAlertEmail = aAlertEmail;
      }


      /**
       * Sets a new value for the lockedBool property.
       *
       * @param aLockedBool
       *           the new value for the lockedBool property
       */
      public void setLockedBool( long aLockedBool ) {
         iLockedBool = aLockedBool;
      }


      /**
       * Sets a new value for the orgHrId property.
       *
       * @param aOrgHrId
       *           the new value for the orgHrId property
       */
      public void setOrgHrId( long aOrgHrId ) {
         iOrgHrId = aOrgHrId;
      }


      /**
       * Sets a new value for the orgHrDbId property.
       *
       * @param aOrgHrDbId
       *           the new value for the orgHrDbId property
       */
      public void setOrgHrDbId( long aOrgHrDbId ) {
         iOrgHrDbId = aOrgHrDbId;
      }


      /**
       * Sets a new value for the orgHrCd property.
       *
       * @param aOrgHrCd
       *           the new value for the orgHrCd property
       */
      public void setOrgHrCd( String aOrgHrCd ) {
         iOrgHrCd = aOrgHrCd;
      }


      /**
       * Sets a new value for the orgAltId property.
       *
       * @param aOrgAltId
       *           the new value for the orgAltId property
       */
      public void setOrgAltId( OrganizationId aOrgAltId ) {
         iOrgAltId = aOrgAltId;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ( ( iAlertEmail == null ) ? 0 : iAlertEmail.hashCode() );
         result = prime * result + ( ( iEmail == null ) ? 0 : iEmail.hashCode() );
         result = prime * result + ( ( iFirstName == null ) ? 0 : iFirstName.hashCode() );
         result = prime * result + ( ( iLastName == null ) ? 0 : iLastName.hashCode() );
         result = prime * result + ( int ) ( iLockedBool ^ ( iLockedBool >>> 32 ) );
         result = prime * result + ( ( iMiddleName == null ) ? 0 : iMiddleName.hashCode() );
         result = prime * result + ( ( iOrgAltId == null ) ? 0 : iOrgAltId.hashCode() );
         result = prime * result + ( ( iOrgHrCd == null ) ? 0 : iOrgHrCd.hashCode() );
         result = prime * result + ( int ) ( iOrgHrDbId ^ ( iOrgHrDbId >>> 32 ) );
         result = prime * result + ( int ) ( iOrgHrId ^ ( iOrgHrId >>> 32 ) );
         result = prime * result + ( ( iUserAltId == null ) ? 0 : iUserAltId.hashCode() );
         result = prime * result + ( ( iUserName == null ) ? 0 : iUserName.hashCode() );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         MockRow other = ( MockRow ) obj;
         if ( iAlertEmail == null ) {
            if ( other.iAlertEmail != null )
               return false;
         } else if ( !iAlertEmail.equals( other.iAlertEmail ) )
            return false;
         if ( iEmail == null ) {
            if ( other.iEmail != null )
               return false;
         } else if ( !iEmail.equals( other.iEmail ) )
            return false;
         if ( iFirstName == null ) {
            if ( other.iFirstName != null )
               return false;
         } else if ( !iFirstName.equals( other.iFirstName ) )
            return false;
         if ( iLastName == null ) {
            if ( other.iLastName != null )
               return false;
         } else if ( !iLastName.equals( other.iLastName ) )
            return false;
         if ( iLockedBool != other.iLockedBool )
            return false;
         if ( iMiddleName == null ) {
            if ( other.iMiddleName != null )
               return false;
         } else if ( !iMiddleName.equals( other.iMiddleName ) )
            return false;
         if ( iOrgAltId == null ) {
            if ( other.iOrgAltId != null )
               return false;
         } else if ( !iOrgAltId.equals( other.iOrgAltId ) )
            return false;
         if ( iOrgHrCd == null ) {
            if ( other.iOrgHrCd != null )
               return false;
         } else if ( !iOrgHrCd.equals( other.iOrgHrCd ) )
            return false;
         if ( iOrgHrDbId != other.iOrgHrDbId )
            return false;
         if ( iOrgHrId != other.iOrgHrId )
            return false;
         if ( iUserAltId == null ) {
            if ( other.iUserAltId != null )
               return false;
         } else if ( !iUserAltId.equals( other.iUserAltId ) )
            return false;
         if ( iUserName == null ) {
            if ( other.iUserName != null )
               return false;
         } else if ( !iUserName.equals( other.iUserName ) )
            return false;
         return true;
      }


      public static class MockRowBuilder {

         // [hr_db_id]:[hr_id]
         private static final Pattern ORG_HR_KEY_PATTERN = Pattern.compile( "(\\d+?):(\\d+?)" );

         // utl_user fields
         private UserAccountId iUserAltId;
         private String iUserName;
         private String iFirstName;
         private String iLastName;
         private String iMiddleName;
         private String iEmail;
         private String iAlertEmail;
         private long iLockedBool;

         // org_hr fields
         private long iOrgHrId;
         private long iOrgHrDbId;
         private String iOrgHrCd;

         // org_org fields
         private OrganizationId iOrgAltId;


         /**
          * Sets a new value for the userAltId property.
          *
          * @param aUserAltId
          *           the new value for the userAltId property
          */
         public MockRowBuilder userAltId( UUID aUserAltId ) {
            iUserAltId = new UserAccountId( aUserAltId );

            return this;
         }


         /**
          * Sets a new value for the userName property.
          *
          * @param aUserName
          *           the new value for the userName property
          */
         public MockRowBuilder userName( String aUserName ) {
            iUserName = aUserName;

            return this;
         }


         /**
          * Sets a new value for the firstName property.
          *
          * @param aFirstName
          *           the new value for the firstName property
          */
         public MockRowBuilder firstName( String aFirstName ) {
            iFirstName = aFirstName;

            return this;
         }


         /**
          * Sets a new value for the lastName property.
          *
          * @param aLastName
          *           the new value for the lastName property
          */
         public MockRowBuilder lastName( String aLastName ) {
            iLastName = aLastName;

            return this;
         }


         /**
          * Sets a new value for the middleName property.
          *
          * @param aMiddleName
          *           the new value for the middleName property
          */
         public MockRowBuilder middleName( String aMiddleName ) {
            iMiddleName = aMiddleName;

            return this;
         }


         /**
          * Sets a new value for the email property.
          *
          * @param aEmail
          *           the new value for the email property
          */
         public MockRowBuilder email( String aEmail ) {
            iEmail = aEmail;

            return this;
         }


         /**
          * Sets a new value for the alertEmail property.
          *
          * @param aAlertEmail
          *           the new value for the alertEmail property
          */
         public MockRowBuilder alertEmail( String aAlertEmail ) {
            iAlertEmail = aAlertEmail;

            return this;
         }


         /**
          * Sets a new value for the lockedBool property.
          *
          * @param aLockedBool
          *           the new value for the lockedBool property
          */
         public MockRowBuilder lockedBool( long aLockedBool ) {
            iLockedBool = aLockedBool;

            return this;
         }


         /**
          * Sets a new value for the orgHrId property.
          *
          * @param aOrgHrId
          *           the new value for the orgHrId property
          */
         public MockRowBuilder orgHrId( long aOrgHrId ) {
            iOrgHrId = aOrgHrId;

            return this;
         }


         /**
          * Sets a new value for the orgHrDbId property.
          *
          * @param aOrgHrDbId
          *           the new value for the orgHrDbId property
          */
         public MockRowBuilder orgHrDbId( long aOrgHrDbId ) {
            iOrgHrDbId = aOrgHrDbId;

            return this;
         }


         public MockRowBuilder orgHrKey( String aOrgHrKey ) {
            Matcher orgHrKeyMatcher = ORG_HR_KEY_PATTERN.matcher( aOrgHrKey );

            if ( orgHrKeyMatcher.matches() ) {
               iOrgHrDbId = NumberUtils.toLong( orgHrKeyMatcher.group( 1 ) );
               iOrgHrId = NumberUtils.toLong( orgHrKeyMatcher.group( 2 ) );
            } else {
               Assert.fail( String.format(
                     "Org HR key: %s supplied to MockRow constructor is not of the format: [number]:[number]." ) );
            }

            return this;
         }


         /**
          * Sets a new value for the orgHrCd property.
          *
          * @param aOrgHrCd
          *           the new value for the orgHrCd property
          */
         public MockRowBuilder orgHrCd( String aOrgHrCd ) {
            iOrgHrCd = aOrgHrCd;

            return this;
         }


         /**
          * Sets a new value for the orgAltId property.
          *
          * @param aOrgAltId
          *           the new value for the orgAltId property
          */
         public MockRowBuilder orgAltId( UUID aOrgAltId ) {
            iOrgAltId = new OrganizationId( aOrgAltId );

            return this;
         }


         public MockRow build() {
            MockRow mockRow = new MockRow();
            {
               mockRow.setUserAltId( iUserAltId );
               mockRow.setUserName( iUserName );
               mockRow.setFirstName( iFirstName );
               mockRow.setLastName( iLastName );
               mockRow.setMiddleName( iMiddleName );
               mockRow.setEmail( iEmail );
               mockRow.setAlertEmail( iAlertEmail );
               mockRow.setLockedBool( iLockedBool );
               mockRow.setOrgHrId( iOrgHrId );
               mockRow.setOrgHrDbId( iOrgHrDbId );
               mockRow.setOrgHrCd( iOrgHrCd );
               mockRow.setOrgAltId( iOrgAltId );
            }

            return mockRow;
         }
      }
   }
}
