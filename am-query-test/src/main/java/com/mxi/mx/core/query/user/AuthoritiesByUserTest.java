
package com.mxi.mx.core.query.user;

import java.util.ArrayList;
import java.util.List;
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
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QueryRow;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Unit test for com.mxi.mx.core.query.user.AuthoritiesByUser.qrx.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AuthoritiesByUserTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private QueryAccessObject iQueryAccessObject = QuerySetFactory.getInstance();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), AuthoritiesByUserTest.class,
            "AuthoritiesByUserTest.xml" );
   }


   @Test
   public void getAuthoritiesForUserTest() {
      List<MockRow> lExpectedRows = buildMockDataForAuthoritiesForUserTest();
      long userId = 9999999;
      QuerySet lExecuteQuery = executeQuery( userId );
      int lRowCount = lExecuteQuery.getRowCount();
      List<MockRow> lTestRows = new ArrayList<MockRow>();
      {
         List<? extends QueryRow> lRows = lExecuteQuery.getRows();

         for ( QueryRow lRow : lRows ) {
            String lAuthorityCd = lRow.getString( "authority_cd" );
            String lAuthorityName = lRow.getString( "authority_name" );
            String lAuthorityKey = lRow.getString( "authority_key" );
            MockRow lTestRow = new MockRow( lAuthorityCd, lAuthorityName, lAuthorityKey );

            lTestRows.add( lTestRow );
         }
      }

      Assert.assertEquals( 3, lRowCount );
      for ( MockRow lTestRow : lTestRows ) {
         Assert.assertTrue( lExpectedRows.contains( lTestRow ) );
      }
   }


   private QuerySet executeQuery( long aUserId ) {
      String lQueryName = "com.mxi.mx.core.query.user.AuthoritiesByUser";
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.add( "aUserId", aUserId );
      }
      QuerySet lExecuteQuery = iQueryAccessObject.executeQuery( lQueryName, lArgs );

      return lExecuteQuery;
   }


   /**
    * Builds 3 rows.
    */
   private List<MockRow> buildMockDataForAuthoritiesForUserTest() {
      List<MockRow> lExpectedRows = new ArrayList<MockRow>();
      {
         // first row
         String lAuthorityCd = "CODE_1";
         String lAuthorityName = "NAME_1";
         long lAuthorityDbIdKey = 9999999;
         long lAuthorityIdKey = 9999999;
         MockRow lExpectedRow =
               new MockRow( lAuthorityCd, lAuthorityName, lAuthorityDbIdKey, lAuthorityIdKey );

         lExpectedRows.add( lExpectedRow );

         // second row
         lAuthorityCd = "CODE_2";
         lAuthorityName = "NAME_2";
         lAuthorityDbIdKey = 9999998;
         lAuthorityIdKey = 9999998;
         lExpectedRow =
               new MockRow( lAuthorityCd, lAuthorityName, lAuthorityDbIdKey, lAuthorityIdKey );

         lExpectedRows.add( lExpectedRow );

         // third row
         lAuthorityCd = "CODE_3";
         lAuthorityName = "NAME_3";
         lAuthorityDbIdKey = 9999997;
         lAuthorityIdKey = 9999997;
         lExpectedRow =
               new MockRow( lAuthorityCd, lAuthorityName, lAuthorityDbIdKey, lAuthorityIdKey );

         lExpectedRows.add( lExpectedRow );
      }

      return lExpectedRows;
   }


   private static class MockRow {

      // [authority_db_id]:[authority_id]
      private static final Pattern AUTHORITY_KEY_PATTERN = Pattern.compile( "(\\d+?):(\\d+?)" );

      private String iAuthorityCd;
      private String iAuthorityName;
      private long iAuthorityDbId;
      private long iAuthorityId;


      /**
       * Creates a new {@linkplain MockRow} object.
       *
       * @param aAuthority_cd
       * @param aAuthority_name
       * @param aAuthority_key
       */
      MockRow(String aAuthorityCd, String aAuthorityName, long aAuthorityDbId, long aAuthorityId) {
         iAuthorityCd = aAuthorityCd;
         iAuthorityName = aAuthorityName;
         iAuthorityDbId = aAuthorityDbId;
         iAuthorityId = aAuthorityId;
      }


      /**
       * Creates a new {@linkplain MockRow} object.
       *
       * @param aAuthority_cd
       * @param aAuthority_name
       * @param aAuthority_key
       */
      MockRow(String aAuthorityCd, String aAuthorityName, String aAuthorityKey) {
         Matcher authorityKeyMatcher = AUTHORITY_KEY_PATTERN.matcher( aAuthorityKey );
         if ( authorityKeyMatcher.matches() ) {
            iAuthorityCd = aAuthorityCd;
            iAuthorityName = aAuthorityName;
            iAuthorityDbId = NumberUtils.toLong( authorityKeyMatcher.group( 1 ) );
            iAuthorityId = NumberUtils.toLong( authorityKeyMatcher.group( 2 ) );
         } else {
            Assert.fail( String.format(
                  "Authority key: %s supplied to MockRow constructor is not of the format: [number]:[number]." ) );
         }
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ( ( iAuthorityCd == null ) ? 0 : iAuthorityCd.hashCode() );
         result = prime * result + ( int ) ( iAuthorityDbId ^ ( iAuthorityDbId >>> 32 ) );
         result = prime * result + ( int ) ( iAuthorityId ^ ( iAuthorityId >>> 32 ) );
         result = prime * result + ( ( iAuthorityName == null ) ? 0 : iAuthorityName.hashCode() );
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
         if ( iAuthorityCd == null ) {
            if ( other.iAuthorityCd != null )
               return false;
         } else if ( !iAuthorityCd.equals( other.iAuthorityCd ) )
            return false;
         if ( iAuthorityDbId != other.iAuthorityDbId )
            return false;
         if ( iAuthorityId != other.iAuthorityId )
            return false;
         if ( iAuthorityName == null ) {
            if ( other.iAuthorityName != null )
               return false;
         } else if ( !iAuthorityName.equals( other.iAuthorityName ) )
            return false;
         return true;
      }
   }
}
