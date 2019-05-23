package com.mxi.mx.repository.user;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;


/**
 * The test data
 * (/am-query-test/src/main/resources/com/mxi/mx/repository/user/JdbcUserRepositoryTest.sql) adds
 * {@link #USERS_MATCHING_FILTER} valid users, each with a first and last name containing the
 * partial name of 'UserRepository'. There is a single additional user introduced in the test data
 * with the partial name of 'UserRepository', but it has no matching organisation, so shouldn't turn
 * up in searches.
 */
public class JdbcUserRepositoryTest {

   private static final String USER_PARTIAL_NAME = "UserRepository";
   private static final int USERS_MATCHING_FILTER = 5;
   private static final int USERS_MATCHING_FIRST_NAME_FILTER = 3;
   private static final int USERS_MATCHING_LAST_NAME_FILTER = 2;
   private static final int TEST_USER_DEFAULT_CAP = 2;

   private UserRepository jdbcUserRepository;

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() throws Exception {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      jdbcUserRepository = new JdbcUserRepository( TEST_USER_DEFAULT_CAP );
   }


   @Test
   public void searchWithFilter_capLowerThanTotalUsers() {
      int loweredCap = USERS_MATCHING_FILTER - 1;
      List<SearchUserTO> users =
            jdbcUserRepository.searchWithFilter( USER_PARTIAL_NAME, loweredCap );

      assertEquals( loweredCap, users.size() );
   }


   @Test
   public void searchWithFilter_capHigherThanTotalUsers() {
      int raisedCap = USERS_MATCHING_FILTER + 1;
      List<SearchUserTO> users =
            jdbcUserRepository.searchWithFilter( USER_PARTIAL_NAME, raisedCap );

      assertEquals( USERS_MATCHING_FILTER, users.size() );
   }


   /**
    * The repository's default cap should be used in this scenario.
    */
   @Test
   public void searchWithFilter_capLessThan1() {
      List<SearchUserTO> users = jdbcUserRepository.searchWithFilter( USER_PARTIAL_NAME, -1 );

      assertEquals( TEST_USER_DEFAULT_CAP, users.size() );
   }


   @Test
   public void searchWithFilter_firstNameMatch() {
      List<SearchUserTO> users = jdbcUserRepository.searchWithFilter( "first" + USER_PARTIAL_NAME,
            USERS_MATCHING_FILTER );

      assertEquals( USERS_MATCHING_FIRST_NAME_FILTER, users.size() );
   }


   @Test
   public void searchWithFilter_lastNameMatch() {
      List<SearchUserTO> users = jdbcUserRepository.searchWithFilter( "last" + USER_PARTIAL_NAME,
            USERS_MATCHING_FILTER );

      assertEquals( USERS_MATCHING_LAST_NAME_FILTER, users.size() );
   }


   @Test
   public void searchWithFilter_noMatch() {
      String noWayThisIsAMatch = "eljefeConQueso";
      List<SearchUserTO> users =
            jdbcUserRepository.searchWithFilter( noWayThisIsAMatch, USERS_MATCHING_FILTER );

      assertEquals( 0, users.size() );
   }
}
