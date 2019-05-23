package com.mxi.mx.core.materials.asset.aircraft;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Test the GetAircraftByAuthority query used by the SearchAircraftByAuthority api
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetAircraftByAuthorityTest {

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   /**
    * The xml test data contains 3 aircraft:
    * <ul>
    * <li>one aircraft with authority F2000</li>
    * <li>one aircraft with authority B767</li>
    * <li>one aircraft with no authority specified</li>
    * <ul>
    * and 3 users:
    * <ul>
    * <li>one user with authority F2000</li>
    * <li>one user with authority B767</li>
    * <li>one user with all authorities</li>
    * <ul>
    *
    */
   // test data constants
   private static final int USER_WITH_ONE_AUTHORITY = 1;
   private static final int USER_WITH_ALL_AUTHORITIES = 2;
   private static final int USER_WITH_ONE_AUTHORITY_AND_ALL_AUTHORITIES = 3;
   private static final int USER_WITH_NO_AUTHORITIES = 99;
   private static final String AIRCRAFT_WITH_F200_AUTHORITY = "Falcon 2000 - 701";
   private static final String AIRCRAFT_WITH_B767_AUTHORITY = "Boeing 767-232 - 801";
   private static final String AIRCRAFT_WITH_NO_AUTHORITY = "Airbus A319/A320 - 901";


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetAircraftByAuthorityTest.class,
            "GetAircraftByAuthorityTest.xml" );
   }


   /**
    *
    * Test the query returns all aircraft for a user who has one authority. User has F2000 authority
    * and all_authorities_bool is false. Query should return 2 aircraft - the one with authority
    * F2000 and the one with no authority.
    *
    * @throws ParseException
    */
   @Test
   public void testUserWithOneAuthority() throws ParseException {

      QuerySet lQs = execute( USER_WITH_ONE_AUTHORITY );
      Assert.assertEquals( 2, lQs.getRowCount() );

   }

   /**
    *
    * Test the query returns all aircraft for a user who has all authorities. Query should return 3
    * aircraft.
    *
    * @throws ParseException
    */
   // TODO: Uncomment once OPER-13911 is complete
   // @Test
   // public void testUserWithAllAuthorities() throws ParseException {
   //
   // QuerySet lQs = execute( USER_WITH_ALL_AUTHORITIES );
   // Assert.assertEquals( 3, lQs.getRowCount() );
   //
   // }


   /**
    *
    * Test the query returns all aircraft for a user who has all authorities. Tests that the query
    * works when a user has both a specific authority AND the all authorities bool. All authorities
    * bool should trump specific authority. Query should return 3 aircraft.
    *
    * @throws ParseException
    */
   // TODO: Uncomment once OPER-13911 is complete
   // @Test
   // public void testUserWithOneAuthorityAndAllAuthoritiesBool() throws ParseException {
   //
   // QuerySet lQs = execute( USER_WITH_ONE_AUTHORITY_AND_ALL_AUTHORITIES );
   // Assert.assertEquals( 3, lQs.getRowCount() );
   //
   // }

   /**
    *
    * Test the query returns only aircraft with no authority for a user who has no authorities.
    * Query should return 1 aircraft.
    *
    * @throws ParseException
    */
   @Test
   public void testUserWithNoAuthorities() throws ParseException {

      QuerySet lQs = execute( USER_WITH_NO_AUTHORITIES );
      Assert.assertEquals( 1, lQs.getRowCount() );
      lQs.next();
      Assert.assertEquals( AIRCRAFT_WITH_NO_AUTHORITY, lQs.getString( "name" ) );

   }


   public QuerySet execute( int aUserId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aCurrentUserId", aUserId );

      return iQao.executeQuery(
            "com.mxi.mx.core.materials.asset.aircraft.dao.query.GetAircraftByAuthority", lArgs );
   }

}
