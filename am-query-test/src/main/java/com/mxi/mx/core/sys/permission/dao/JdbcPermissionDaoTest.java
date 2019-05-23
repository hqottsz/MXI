
package com.mxi.mx.core.sys.permission.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.sys.permission.model.Permission;


/**
 * Query test for com.mxi.mx.core.sys.permission.dao.query.GetPermission.qrx
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcPermissionDaoTest {

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   private final PermissionDao iDao = new JdbcPermissionDao();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

   private static final ArrayList<String> sParmNames = new ArrayList<String>();


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sConnection.getConnection(), JdbcPermissionDaoTest.class,
            "JdbcPermissionDaoTest.sql" );
      // Initialize list of parm names
      sParmNames.add( "A" );
      sParmNames.add( "B" );
      sParmNames.add( "C" );
      sParmNames.add( "D" );
      sParmNames.add( "E" );
      sParmNames.add( "F" );
   }


   @Test
   public void itGetsTruePermissionsForUser() {

      // Execute the query

      Collection<Permission> lPermissions = iDao.checkPermission( "0101", sParmNames );

      HashMap<String, Boolean> lCorrectPermissions = new HashMap<String, Boolean>();
      lCorrectPermissions.put( "A", new Boolean( true ) );
      lCorrectPermissions.put( "B", new Boolean( true ) );
      lCorrectPermissions.put( "C", new Boolean( true ) );
      lCorrectPermissions.put( "D", new Boolean( true ) );
      lCorrectPermissions.put( "E", new Boolean( true ) );
      lCorrectPermissions.put( "F", new Boolean( true ) );

      for ( Permission lPermission : lPermissions ) {
         Assert.assertEquals( lPermission.getCode(),
               lCorrectPermissions.get( lPermission.getCode() ), lPermission.isEnabled() );
      }

   }


   @Test
   public void itGetsFalsePermissionsForUser() {

      // Execute the query

      Collection<Permission> lPermissions = iDao.checkPermission( "0102", sParmNames );

      HashMap<String, Boolean> lCorrectPermissions = new HashMap<String, Boolean>();
      lCorrectPermissions.put( "A", new Boolean( false ) );
      lCorrectPermissions.put( "B", new Boolean( false ) );
      lCorrectPermissions.put( "C", new Boolean( false ) );
      lCorrectPermissions.put( "D", new Boolean( false ) );
      lCorrectPermissions.put( "E", new Boolean( false ) );
      lCorrectPermissions.put( "F", new Boolean( false ) );

      for ( Permission lPermission : lPermissions ) {
         Assert.assertEquals( lPermission.getCode(),
               lCorrectPermissions.get( lPermission.getCode() ), lPermission.isEnabled() );
      }

   }

}
