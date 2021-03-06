package com.mxi.mx.core.materials.asset.inventory;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;


public class GetZonesByRegCdTest {

   private final String AC_REG_CD = "CFYJG";
   private final String AC_REG_CD_WITHOUT_ZONE = "BFYJK";
   private final String INVALID_AC_REG_CD = "XXX";
   private final String ZONE_CD = "501";
   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sConnection.getConnection(), GetPanelsByRegCodeTest.class,
            "GetZonesByRegCdTest.xml" );
   }


   @Test
   public void testQueryWithValidAcRegCode() throws ParseException {

      QuerySet lQs = execute( AC_REG_CD );

      Assert.assertEquals( lQs.getRowCount(), 1 );
      Assert.assertTrue( lQs.first() );
      Assert.assertEquals( ZONE_CD, lQs.getString( "zone_cd" ) );
   }


   @Test
   public void testQueryWithInvalidAcRegCode() throws ParseException {
      QuerySet lQs = execute( INVALID_AC_REG_CD );
      Assert.assertTrue( lQs.isEmpty() );
   }


   @Test
   public void testQueryWithRightRegCodeAndNoZone() throws ParseException {
      QuerySet lQs = execute( AC_REG_CD_WITHOUT_ZONE );
      Assert.assertTrue( lQs.isEmpty() );
   }


   public QuerySet execute( String aRegCode ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aRegCode", aRegCode );

      return iQao.executeQuery( "com.mxi.mx.core.query.inventory.getZonesByRegCd", lArgs );
   }
}
