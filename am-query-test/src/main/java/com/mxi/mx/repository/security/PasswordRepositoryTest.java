package com.mxi.mx.repository.security;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.wls.security.password.PasswordRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class PasswordRepositoryTest {

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static String EXPECTED_PASSWORD = "PICKLES";
   private static String CHANGED_PASSWORD = "BEETS";
   private static String USERNAME = "CHEDDAR";
   private static String NONEXISTENT_USER = "HERRING";
   private PasswordRepository passwordRepository;

   @ClassRule
   public static DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() throws SQLException {
      DataLoaders.load( databaseConnectionRule.getConnection(),
            PasswordRepositoryTest.class );
   }


   @Before
   public void setUp() {
      passwordRepository = new PasswordRepository();
   }

   @Test(expected = NoSuchElementException.class)
   public void getPasswordShouldReturnExceptionWhenUserDoesNotExist() throws Throwable{
      passwordRepository.getPassword(
         databaseConnectionRule.getConnection(), NONEXISTENT_USER ).get();
   }

   @Test
   public void getPasswordShouldReturnExpectedPassword() throws Throwable{
      String retrievedPass = passwordRepository.getPassword(
         databaseConnectionRule.getConnection(), USERNAME ).get();
      assertEquals( retrievedPass, EXPECTED_PASSWORD );
   }

   @Test
   public void savePasswordShouldSaveProvidedPassword() throws Throwable{
      passwordRepository.savePassword(
            databaseConnectionRule.getConnection(), USERNAME, "BEETS" );

      String retrievedPass = passwordRepository.getPassword(
            databaseConnectionRule.getConnection(), USERNAME).get();

     assertEquals( retrievedPass, CHANGED_PASSWORD );
   }


   @Test ( expected = IllegalArgumentException.class )
   public void savePasswordShouldReturnExceptionProvidedNullPassword() throws Throwable{
      passwordRepository.savePassword(
            databaseConnectionRule.getConnection(), USERNAME, null );
   }

   @Test ( expected = IllegalArgumentException.class )
   public void savePasswordShouldRetrunExceptionProvidedEmptyPassword() throws Throwable{
      passwordRepository.savePassword(
            databaseConnectionRule.getConnection(), USERNAME, "   " );
   }
}
