package com.mxi.mx.web.query.acftgroup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.services.acftgroup.AircraftGroupService;


/**
 * This class is used to test the AircraftGroupService class.
 *
 * @author ywang
 * @created Nov 23, 2016
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AircraftGroupServiceTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftGroupByNameTest.class );
   }


   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /*
    * Test the method createAircraftGroup can create an Aircraft Groups and insert it into DB
    */
   @Test
   public void testCreateAircraftGroup() throws Exception {

      int lId = new AircraftGroupService().createAircraftGroup( "AircraftGroup1", null );

      // poll database to see if aircraft group was created
      assertTrue( isAircraftGroupExistedInDb( lId, "AircraftGroup1" ) );
   }


   /*
    * Test the method removeAircraftGroup can remove Aircraft Groups from DB
    */
   @Test
   public void testRemoveAircraftGroup() throws Exception {

      int lId1 = new AircraftGroupService().createAircraftGroup( "AircraftGroup2", null );
      int lId2 = new AircraftGroupService().createAircraftGroup( "AircraftGroup3", null );

      // poll database to see if aircraft group was created
      assertTrue( isAircraftGroupExistedInDb( lId1, "AircraftGroup2" ) );
      assertTrue( isAircraftGroupExistedInDb( lId2, "AircraftGroup3" ) );

      String[] lIds = { String.valueOf( lId1 ), String.valueOf( lId2 ) };

      AircraftGroupService lAircraftGroupService = new AircraftGroupService();
      lAircraftGroupService.removeAircraftGroup( lIds );
      // poll database to see if aircraft group was removed
      assertFalse( isAircraftGroupExistedInDb( lId1, "AircraftGroup2" ) );
      assertFalse( isAircraftGroupExistedInDb( lId2, "AircraftGroup3" ) );
   }


   /**
    * Check whether the aircraft group is created
    *
    * @param aId
    *           aircraft group id
    * @param aName
    *           aircraft group name
    *
    * @return true/false
    */
   private boolean isAircraftGroupExistedInDb( int aId, String aName ) {

      // get the aircraft group with ID aId
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "id", aId );
      QuerySet lDs = QuerySetFactory.getInstance().executeQueryTable( "acft_group", lArgs );

      if ( lDs.next() ) {

         String lName = lDs.getString( "name" );
         String lDescription = lDs.getString( "description" );

         if ( lName.equalsIgnoreCase( aName ) && lDescription == null )
            return true;
      }

      // If the aircraft group is not created
      return false;
   }

}
