package com.mxi.mx.web.query.selectwindow.manufacturer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.table.eqp.EqpManufactTable;
import com.mxi.mx.web.jsp.selectwindow.manufacturer.SelectManufacturer;
import com.mxi.mx.web.jsp.selectwindow.manufacturer.SelectManufacturerArgument;


/**
 * This class is used to test the SelectManufacturer class.
 *
 */
public class SelectManufacturerTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void testGetDataSet() throws Exception {

      ManufacturerKey lTestManufacturer = Domain.createManufacturer();

      SelectManufacturerArgument lArgs = new SelectManufacturerArgument();
      SelectManufacturer lSelectTag = new SelectManufacturer( lArgs );
      DataSet lResults = lSelectTag.getDataSet( lTestManufacturer.getCd() );

      assertTrue( lResults.getRowCount() == 1 );
      while ( lResults.next() ) {
         assertEquals( lResults.getString( EqpManufactTable.ColumnName.MANUFACT_CD.name() ),
               lTestManufacturer.getCd() );
      }
   }

}
