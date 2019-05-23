
package com.mxi.mx.core.dao.lrp.assembly;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.mxi.mx.core.dao.lrp.DaoTestCase;
import com.mxi.mx.model.lrp.Assembly;


/**
 * DOCUMENT_ME
 *
 * @author yvakulenko
 */
public class AssemblyDaoTest extends DaoTestCase {

   private AssemblyDao iDao;


   @Override
   @Before
   public void loadData() throws Exception {
      super.loadData();

      iDao = iFactory.getAssemblyDao();
   }


   /**
    * Reads aircrafts.<br>
    * Expects one actual and one future aircrafts.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testRead() throws Exception {
      Collection<Assembly> lAssemblies = iDao.getAssemblies();

      assertEquals( 5, lAssemblies.size() ); // assert number of record
   }
}
