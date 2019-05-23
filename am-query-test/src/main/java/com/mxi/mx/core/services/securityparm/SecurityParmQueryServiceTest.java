package com.mxi.mx.core.services.securityparm;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.RoleKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class SecurityParmQueryServiceTest {

   private static final String DESCRIPTION_NOT_IN_DB = "Where is the polkaroo?";
   private static final String DESCRIPTION_IN_DB = "oEm";

   private static final int ADMINISTRATOR_ROLE_ID = 19000;
   private static final String ALL_CATEGORY = "All";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   private SecurityParmQueryService iSecurityParmQueryService;


   /**
    * Test that the query method returns results when no parm name or description is specified
    */
   @Test
   public void testQueryReturnsResult() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, null, null );

      assertTrue( "Security Parms not found", lQuerySet.first() );
   }


   /**
    * Test that the query method returns results when description is blank
    */
   @Test
   public void testQueryWithBlankDescriptionReturnsResult() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, null, "" );

      assertTrue( "Security Parms not found", lQuerySet.first() );
   }


   /**
    * Test that the query method returns results when specified description exists
    */
   @Test
   public void testQueryWithDescriptionReturnsResult() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, null, DESCRIPTION_IN_DB );

      assertTrue( "Security Parm not found", lQuerySet.first() );
      assertTrue( "Did not find Security Parm containing specified description", StringUtils
            .containsIgnoreCase( lQuerySet.getString( "parm_desc" ), DESCRIPTION_IN_DB ) );

      assertTrue( "Another Security Parm not found", lQuerySet.next() );
      assertTrue( "Did not find another Security Parm containing specified description", StringUtils
            .containsIgnoreCase( lQuerySet.getString( "parm_desc" ), DESCRIPTION_IN_DB ) );
   }


   /**
    * Test that the query method returns no results when specified description does not exist
    */
   @Test
   public void testQueryWithDescriptionNotInDBReturnsNoResult() {
      QuerySet lQuerySet =
            iSecurityParmQueryService.query( ALL_CATEGORY, null, DESCRIPTION_NOT_IN_DB );

      assertTrue( "Unexpectedly found Security Parm", lQuerySet.isEmpty() );
   }


   /**
    * Test that the query method returns results when parm name is blank
    */
   @Test
   public void testQueryWithBlankParmNameReturnsResults() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, "", null );

      assertTrue( "Security Parms not found", lQuerySet.first() );
   }


   /**
    * Test that the query method returns results when specified parm name exists
    */
   @Test
   public void testQueryWithExactParmNameReturnsResult() {
      QuerySet lQuerySet =
            iSecurityParmQueryService.query( ALL_CATEGORY, "SHOW_part_Availability", null );

      assertTrue( "Security Parm not found", lQuerySet.first() );
      assertTrue( "Did not find Security Parm with specified parm name", StringUtils
            .equalsIgnoreCase( lQuerySet.getString( "parm_name" ), "SHOW_PART_AVAILABILITY" ) );
   }


   /**
    * Test that the query method returns results when specified partial parm name exists
    */
   @Test
   public void testQueryWithPartialParmNameReturnsResult() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, "ow_Part_Ava", null );

      assertTrue( "Security Parm not found", lQuerySet.first() );
      assertTrue( "Did not find Security Parm with specified partial parm name", StringUtils
            .equalsIgnoreCase( lQuerySet.getString( "parm_name" ), "SHOW_PART_AVAILABILITY" ) );
   }


   /**
    * Test that the query method returns no results when specified parm name does not exist
    */
   @Test
   public void testQueryWithExactParmNameReturnsNoResult() {
      QuerySet lQuerySet =
            iSecurityParmQueryService.query( ALL_CATEGORY, "Parm Name not in DB", null );

      assertTrue( "Unexpectedly found Security Parm", lQuerySet.isEmpty() );
   }


   /**
    * Test that the query method returns results when specified partial parm name matches
    */
   @Test
   public void testQueryWithParmNameWildcardReturnsResult() {
      QuerySet lQuerySet = iSecurityParmQueryService.query( ALL_CATEGORY, "%PART%", null );

      assertTrue( "Security Parm not found", lQuerySet.first() );
      assertTrue( "Did not find Security Parm containing specified parm name",
            StringUtils.contains( lQuerySet.getString( "parm_name" ), "PART" ) );

      assertTrue( "Another Security Parm not found", lQuerySet.next() );
      assertTrue( "Did not find another Security Parm containing specified parm name",
            StringUtils.contains( lQuerySet.getString( "parm_name" ), "PART" ) );
   }


   @Before
   public void setup() {
      iSecurityParmQueryService =
            new SecurityParmQueryService( new RoleKey( ADMINISTRATOR_ROLE_ID ), false );
   }
}
