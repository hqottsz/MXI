
package com.mxi.mx.core.dao.ppc.capacitypattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.model.ppc.key.IdKey;
import com.mxi.mx.model.ppc.key.IdKeyImpl;
import com.mxi.mx.model.ppc.valueobject.actuals.RefTerm;
import com.mxi.mx.model.ppc.valueobject.capacity.CapacityPattern;
import com.mxi.mx.model.ppc.valueobject.capacity.CapacityPattern.CapacityShift;


/**
 * CapacityPatternDaoImpl Test
 */
public class CapacityPatternDaoImplTest extends DaoTestCase {

   private static final IdKeyImpl CAPACITY_PATTERN_ID_1 = new IdKeyImpl( 4650, 1 );
   private static final IdKeyImpl CAPACITY_PATTERN_ID_3 = new IdKeyImpl( 4650, 3 );
   private static final IdKeyImpl CAPACITY_PATTERN_ID_8 = new IdKeyImpl( 4650, 8 );

   private static final String CP_3_CODE = "ABQ/LINE";
   private static final String CP_3_DESC = "1 day pattern";

   private static final float DAY_SHIFT_START_HOUR = 6.0f;
   private static final float SWING_SHIFT_START_HOUR = 17.0f;

   private static final RefTerm REF_TERM_ENG = new RefTerm( 0, "ENG", "ENG" );
   private static final RefTerm REF_TERM_INSP = new RefTerm( 10, "INSP", "INSP" );
   private static final RefTerm REF_TERM_LBR = new RefTerm( 0, "LBR", "LBR" );
   private static final RefTerm REF_TERM_PAINT = new RefTerm( 10, "PAINT", "PAINT" );

   private CapacityPatternDaoImpl iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();
      iDao = new CapacityPatternDaoImpl();
   }


   /**
    * test builder using a simple plan with one of each of the exported objects
    */
   @Test
   public void testLoadAll() {
      Map<IdKey, CapacityPattern> lMap = iDao.loadAll();

      // assert number of capacity patterns is accurate
      assertEquals( 2, lMap.size() );

      CapacityPattern lCp;
      List<CapacityShift> lShifts;

      lCp = lMap.get( CAPACITY_PATTERN_ID_1 );
      assertNull( lCp );

      lCp = lMap.get( CAPACITY_PATTERN_ID_3 );
      assertNotNull( lCp );
      assertEquals( CP_3_CODE, lCp.getCode() );
      assertEquals( CP_3_DESC, lCp.getDescription() );
      lShifts = lCp.getShifts( 1 );
      assertNotNull( lShifts );
      assertEquals( 2, lShifts.size() );

      // can not gurantee order
      for ( CapacityShift lShift : lShifts ) {
         if ( lShift.getStartHour() == DAY_SHIFT_START_HOUR ) {
            assertEquals( 101, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 102, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }

         if ( lShift.getStartHour() == SWING_SHIFT_START_HOUR ) {
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 103, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 105, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 104, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 106, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }
      }

      lCp = lMap.get( CAPACITY_PATTERN_ID_8 );
      assertNotNull( lCp );
      lShifts = lCp.getShifts( 1 );
      assertNotNull( lShifts );
      assertEquals( 2, lShifts.size() );

      // can not gurantee order
      for ( CapacityShift lShift : lShifts ) {
         if ( lShift.getStartHour() == DAY_SHIFT_START_HOUR ) {
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 16, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 12, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 18, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 14, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }

         if ( lShift.getStartHour() == SWING_SHIFT_START_HOUR ) {
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 11, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 15, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }
      }

      lShifts = lCp.getShifts( 2 );
      assertNotNull( lShifts );
      assertEquals( 2, lShifts.size() );

      // can not gurantee order
      for ( CapacityShift lShift : lShifts ) {
         if ( lShift.getStartHour() == DAY_SHIFT_START_HOUR ) {
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 20, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 21, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }

         if ( lShift.getStartHour() == SWING_SHIFT_START_HOUR ) {
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getLicensedQt( REF_TERM_LBR ) );
            assertEquals( 22, lShift.getLicensedQt( REF_TERM_PAINT ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_ENG ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_INSP ) );
            assertEquals( 0, lShift.getUnlicensedQt( REF_TERM_LBR ) );
            assertEquals( 23, lShift.getUnlicensedQt( REF_TERM_PAINT ) );
         }
      }
   }
}
