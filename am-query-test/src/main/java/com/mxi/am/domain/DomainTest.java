package com.mxi.am.domain;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * Verifies the {@link Domain}. Test sets for each of its methods are collected in inner classes.
 *
 */
@RunWith( Enclosed.class )
public class DomainTest {

   private static final String TRACKED_BARCODE = "TRACKED_BARCODE";
   private static final String ENGINE_BARCODE = "ENGINE_BARCODE";
   private static final String SYSTEM_NAME = "SYSTEM_NAME";
   private static final String ANOTHER_SYSTEM_NAME = "ANOTHER_SYSTEM_NAME";
   private static final String SUBSYSTEM_NAME = "SUBSYSTEM_NAME";


   /**
    *
    * Verifies the behaviours of {@link Domain#createTrackedInventory()}
    *
    */
   public static class CreateTrackedInventoryTest {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();


      /**
       * <pre>
       * When it builds
       * Then the built tracked attributes are:
       *    next-highest = itself
       *         highest = itself
       *        assembly = null
       * </pre>
       */
      @Test
      public void itBuilds() {

         InventoryKey lTracked = Domain.createTrackedInventory();

         Assert.assertEquals( null, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built tracked
       * Then the pre-built tracked attributes are:
       *    next-highest = tracked
       *         highest = tracked
       *        assembly = null
       * </pre>
       */
      @Test
      public void itBuildsWithAddedPreBuiltTracked() {
         final InventoryKey lPreBuiltTracked = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lPreBuiltTracked );
                  }
               } );

         Assert.assertEquals( lTracked, getNextHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lPreBuiltTracked ) );
      }


      /**
       * <pre>
       * When it builds with added tracked configuration
       * Then the child tracked attributes are:
       *    next-highest = tracked
       *         highest = tracked
       *        assembly = null
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedConfiguration() {
         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( new DomainConfiguration<TrackedInventory>() {

                        @Override
                        public void configure( TrackedInventory aBuilder ) {
                           aBuilder.setBarcode( TRACKED_BARCODE );
                        }
                     } );
                  }
               } );

         InventoryKey lChildTracked = getChildWithBarcode( lTracked, TRACKED_BARCODE );

         Assert.assertEquals( lTracked, getNextHighestInv( lChildTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lChildTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lChildTracked ) );
      }


      /**
       * <pre>
       * When it builds with both an added pre-built tracked and an added tracked configuration
       * Then both the child tracked attributes' are:
       *    next-highest = tracked
       *         highest = tracked
       *        assembly = null
       * </pre>
       */
      @Test
      public void itBuildsWithAddedPreBuiltTrackedAndWithAddedTrackedConfiguration() {
         final InventoryKey lPreBuiltTracked = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lPreBuiltTracked );
                     aBuilder.addTracked( new DomainConfiguration<TrackedInventory>() {

                        @Override
                        public void configure( TrackedInventory aBuilder ) {
                           aBuilder.setBarcode( TRACKED_BARCODE );
                        }
                     } );
                  }
               } );

         Assert.assertEquals( lTracked, getNextHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lPreBuiltTracked ) );

         InventoryKey lConfiguredTracked = getChildWithBarcode( lTracked, TRACKED_BARCODE );

         Assert.assertNotNull( "Configured tracked inventory not created.", lConfiguredTracked );
         Assert.assertEquals( lTracked, getNextHighestInv( lConfiguredTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lConfiguredTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lConfiguredTracked ) );

      }


      /**
       * <pre>
       * When it builds with an added tracked that has a child tracked
       * Then the added tracked attributes are:
       *    next-highest = tracked
       *         highest = tracked
       *        assembly = null
       * Then the child of added tracked attributes are:
       *    next-highest = added tracked
       *         highest = tracked
       *        assembly = null
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedThatHasChildTracked() {
         final InventoryKey lChildTracked = Domain.createTrackedInventory();
         final InventoryKey lAddedTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lChildTracked );
                  }
               } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lAddedTracked );
                  }
               } );

         Assert.assertEquals( lTracked, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lAddedTracked ) );

         Assert.assertEquals( lAddedTracked, getNextHighestInv( lChildTracked ) );
         Assert.assertEquals( lTracked, getHighestInv( lChildTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lChildTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built tracked that already has a parent
       * Then an exception is thrown
       * </pre>
       */
      @Test( expected = RuntimeException.class )
      public void itBuildsWithAddedPreBuiltTrackedThatAlreadyHasParent() {
         final InventoryKey lExistingParent = Domain.createTrackedInventory();
         final InventoryKey lPreBuiltTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lExistingParent );
                  }
               } );

         Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

            @Override
            public void configure( TrackedInventory aBuilder ) {
               aBuilder.addTracked( lPreBuiltTracked );
            }
         } );
      }


      /**
       * <pre>
       * When it builds with a tracked parent
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = tracked parent
       *        assembly = null
       * </pre>
       *
       * Note: 1. parent = highest and is not an assembly nor aircraft
       */
      @Test
      public void itBuildsWithTrackedParent() {
         final InventoryKey lTrackedParent = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lTrackedParent, getHighestInv( lTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an assembly parent
       * Then the build tracked attributes are:
       *    next-highest = assembly
       *         highest = assembly
       *        assembly = assembly
       * </pre>
       *
       * Note: 2. parent = highest and is an assembly
       */
      @Test
      public void itBuildsWithAssemblyParent() {
         final InventoryKey lAssembly = Domain.createEngine();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAssembly );
                  }
               } );

         Assert.assertEquals( lAssembly, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an aircraft parent
       * Then the build tracked attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       *
       * Note: 3. parent = highest and is an aircraft
       */
      @Test
      public void itBuildsWithAircraftParent() {
         final InventoryKey lAircraft = Domain.createAircraft();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAircraft );
                  }
               } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a tracked parent and a tracked grandparent
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = tracked grandparent
       *        assembly = null
       * </pre>
       *
       * Note: 4. parent != highest and neither are an assembly
       */
      @Test
      public void itBuildsWithTrackedParentAndTrackedGrandparent() {
         final InventoryKey lTrackedGrandparent = Domain.createTrackedInventory();
         final InventoryKey lTrackedParent =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedGrandparent );
                  }
               } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lTrackedGrandparent, getHighestInv( lTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an assembly parent and aircraft grandparent
       * Then the build tracked attributes are:
       *    next-highest = assembly
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       *
       * Note: 5. parent is an assembly != highest which is an aircraft
       */
      @Test
      public void itBuildsWithAssemblyParentAndAircraftGrandparent() {
         final InventoryKey lAssembly = Domain.createEngine();
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lAssembly );
            }
         } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAssembly );
                  }
               } );

         Assert.assertEquals( lAssembly, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a tracked parent and an assembly grandparent
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = assembly grandparent
       *        assembly = assembly grandparent
       * </pre>
       *
       * Note: 6. parent is not an assembly != highest which is an assembly
       */
      @Test
      public void itBuildsWithTrackedParentAndAssemblyGrandparent() {
         final InventoryKey lAssemblyGrandparent = Domain.createEngine();
         final InventoryKey lTrackedParent =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAssemblyGrandparent );
                  }
               } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAssemblyGrandparent, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssemblyGrandparent, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a tracked parent and an assembly grandparent and aircraft great-grandparent
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       *
       * Note: 7. parent is not an assembly != highest which is an aircraft but there is an assembly
       * between them
       */
      @Test
      public void itBuildsWithTrackedParentAndAssemblyGrandparenetAndAircraftGreatgrandparent() {
         final InventoryKey lTrackedParent = Domain.createTrackedInventory();
         final InventoryKey lAssembly = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lTrackedParent );
            }
         } );
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lAssembly );
            }
         } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a tracked parent and an aircraft grandparent
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       *
       * Note: 8. parent is not an assembly != highest which is an aircraft and there is no assembly
       * between them
       */
      @Test
      public void itBuildsWithTrackedParentAndAircraftGrandparent() {
         final InventoryKey lTrackedParent = Domain.createTrackedInventory();
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( lTrackedParent );
            }
         } );

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a tracked parent and an added tracked
       * Then the build tracked attributes are:
       *    next-highest = tracked parent
       *         highest = tracked parent
       *        assembly = null
       * Then the added tracked attributes are:
       *    next-highest = built tracked
       *         highest = tracked parent
       *        assembly = null
       * </pre>
       *
       */
      @Test
      public void itBuildsWithTrackedParentAndAddedTracked() {
         final InventoryKey lTrackedParent = Domain.createTrackedInventory();

         final InventoryKey lAddedTracked = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lTrackedParent );
                     aBuilder.addTracked( lAddedTracked );
                  }
               } );

         Assert.assertEquals( lTrackedParent, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lTrackedParent, getHighestInv( lTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lTracked ) );

         Assert.assertEquals( lTracked, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lTrackedParent, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( null, getAssemblyInv( lAddedTracked ) );
      }


      /**
       * <pre>
       * When it builds with an assembly parent and an added tracked
       * Then the build tracked attributes are:
       *    next-highest = assembly
       *         highest = assembly
       *        assembly = assembly
       * Then the added tracked attributes are:
       *    next-highest = built tracked
       *         highest = assembly
       *        assembly = assembly
       * </pre>
       *
       */
      @Test
      public void itBuildsWithAssemblyParentAndAddedTracked() {
         final InventoryKey lAssembly = Domain.createEngine();

         final InventoryKey lAddedTracked = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAssembly );
                     aBuilder.addTracked( lAddedTracked );
                  }
               } );

         Assert.assertEquals( lAssembly, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lTracked ) );

         Assert.assertEquals( lTracked, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAssembly, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lAddedTracked ) );
      }


      /**
       * <pre>
       * When it builds with an assembly parent, an aircraft grandparent, and an added tracked
       * Then the build tracked attributes are:
       *    next-highest = assembly
       *         highest = aircraft
       *        assembly = assembly
       * Then the added tracked attributes are:
       *    next-highest = built tracked
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       *
       */
      @Test
      public void itBuildsWithAssemblyParentAndAircraftGrandparentAndAddedTracked() {
         final InventoryKey lAssembly = Domain.createEngine();
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lAssembly );
            }
         } );

         final InventoryKey lAddedTracked = Domain.createTrackedInventory();

         InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lAssembly );
                     aBuilder.addTracked( lAddedTracked );
                  }
               } );

         Assert.assertEquals( lAssembly, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lTracked ) );

         Assert.assertEquals( lTracked, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAssembly, getAssemblyInv( lAddedTracked ) );
      }

   }

   /**
    *
    * Verifies the behaviours of {@link Domain#createEngine()}
    *
    */
   public static class CreateEngineTest {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();


      /**
       * <pre>
       * When it builds
       * Then the built engine attributes are:
       *    next-highest = null
       *         highest = itself
       *        assembly = itself
       * </pre>
       */
      @Test
      public void itBuilds() {

         InventoryKey lEngine = Domain.createEngine();

         Assert.assertEquals( null, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lEngine, getHighestInv( lEngine ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built tracked
       * Then the pre-built tracked attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedPreBuiltTracked() {
         final InventoryKey lPreBuiltTracked = Domain.createTrackedInventory();

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lPreBuiltTracked );
            }
         } );

         Assert.assertEquals( lEngine, getNextHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lEngine, getHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lPreBuiltTracked ) );
      }


      /**
       * <pre>
       * When it builds with added tracked configuration
       * Then the tracked attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedConfiguration() {
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setBarcode( TRACKED_BARCODE );
                  }
               } );
            }
         } );

         InventoryKey lTracked = getChildWithBarcode( lEngine, TRACKED_BARCODE );

         Assert.assertEquals( lEngine, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );

      }


      /**
       * <pre>
       * When it builds with an added tracked that has a child tracked
       * Then the added tracked attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * Then the child of added tracked attributes are:
       *    next-highest = added tracked
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedThatHasChildTracked() {
         final InventoryKey lChildTracked = Domain.createTrackedInventory();
         final InventoryKey lAddedTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lChildTracked );
                  }
               } );

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lAddedTracked );
            }
         } );

         Assert.assertEquals( lEngine, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lEngine, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lAddedTracked ) );

         Assert.assertEquals( lAddedTracked, getNextHighestInv( lChildTracked ) );
         Assert.assertEquals( lEngine, getHighestInv( lChildTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lChildTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added tracked that already has a parent
       * Then an exception is thrown
       * </pre>
       */
      @Test( expected = RuntimeException.class )
      public void itBuildsWithAddedPreBuiltTrackedThatAlreadyHasParent() {
         final InventoryKey lExistingParent = Domain.createTrackedInventory();
         final InventoryKey lTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lExistingParent );
                  }
               } );

         Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lTracked );
            }
         } );
      }


      /**
       * <pre>
       * When it builds with added system configuration
       * Then the system attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemConfiguration() {
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aBuilder ) {
                     aBuilder.setName( SYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem ) );
      }


      /**
       * <pre>
       * When it builds with added system by name
       * Then the child system attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemName() {
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem ) );
      }


      /**
       * <pre>
       * When it builds with multiple added systems
       * Then both child systems attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithMultipleAddedSystems() {
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aBuilder ) {
                     aBuilder.setName( ANOTHER_SYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem1 = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );
         InventoryKey lSystem2 = InvUtils.getSystemByName( lEngine, ANOTHER_SYSTEM_NAME );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem1 ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem1 ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem1 ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem2 ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem2 ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem2 ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a sub-system
       * Then the system attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * Then the sub-system attributes are:
       *    next-highest = system
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingSubsystem() {
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addSubSystem( SUBSYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );
         InventoryKey lSubSystem = InvUtils.getSystemByName( lSystem, SUBSYSTEM_NAME );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lSubSystem ) );
         Assert.assertEquals( lEngine, getHighestInv( lSubSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSubSystem ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a tracked inventory
       * Then the system attributes are:
       *    next-highest = engine
       *         highest = engine
       *        assembly = engine
       * Then the tracked attributes are:
       *    next-highest = system
       *         highest = engine
       *        assembly = engine
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingTracked() {
         final InventoryKey lTracked = Domain.createTrackedInventory();
         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addTracked( lTracked );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an aircraft parent
       * Then the build engine attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       *
       * Note: 1. parent = highest and is an aircraft
       */
      @Test
      public void itBuildsWithAircraftParent() {
         final InventoryKey lAircraft = Domain.createAircraft();

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.setParent( lAircraft );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with a system parent and an aircraft grandparent
       * Then the build engine attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       *
       * Note: 2. parent != highest which is an aircraft
       */
      @Test
      public void itBuildsWithSystemParentAndAircraftGrandparent() {

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
            }
         } );
         final InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.setParent( lSystem );
            }
         } );

         Assert.assertEquals( lSystem, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with an aircraft parent and an added tracked
       * Then the build engine attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the added tracked attributes are:
       *    next-highest = engine
       *         highest = aircraft
       *        assembly = engine
       * </pre>
       *
       */
      @Test
      public void itBuildsWithAircraftParentAndAddedTracked() {
         final InventoryKey lAircraft = Domain.createAircraft();
         final InventoryKey lTracked = Domain.createTrackedInventory();

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.setParent( lAircraft );
               aBuilder.addTracked( lTracked );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with a system parent, an aircraft grandparent, and an added tracked
       * Then the build engine attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * Then the added tracked attributes are:
       *    next-highest = engine
       *         highest = aircraft
       *        assembly = engine
       * </pre>
       *
       */
      @Test
      public void itBuildsWithSystemParentAndAircraftGrandparentAndAddedTracked() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
            }
         } );
         final InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );
         final InventoryKey lTracked = Domain.createTrackedInventory();

         InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.setParent( lSystem );
               aBuilder.addTracked( lTracked );
            }
         } );

         Assert.assertEquals( lSystem, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }

   }

   /**
    *
    * Verifies the behaviours of {@link Domain#createAircraft()}
    *
    */
   public static class CreateAircraftTest {

      @Rule
      public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

      @Rule
      public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
            new FakeJavaEeDependenciesRule();


      /**
       * <pre>
       * When it builds
       * Then the built aircraft attributes are:
       *    next-highest = null
       *         highest = itself
       *        assembly = itself
       * </pre>
       */
      @Test
      public void itBuilds() {

         final InventoryKey lAircraft = Domain.createAircraft();

         Assert.assertEquals( null, getNextHighestInv( lAircraft ) );
         Assert.assertEquals( lAircraft, getHighestInv( lAircraft ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lAircraft ) );
      }


      /**
       * <pre>
       * When it builds with added system configuration
       * Then the child system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemConfiguration() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aBuilder ) {
                     aBuilder.setName( SYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );
      }


      /**
       * <pre>
       * When it builds with added system by name
       * Then the child system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemName() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );
      }


      /**
       * <pre>
       * When it builds with multiple added systems
       * Then both child systems attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithMultipleAddedSystems() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( SYSTEM_NAME );
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aBuilder ) {
                     aBuilder.setName( ANOTHER_SYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem1 = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );
         InventoryKey lSystem2 = InvUtils.getSystemByName( lAircraft, ANOTHER_SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem1 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem1 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem1 ) );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem2 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem2 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem2 ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a sub-system
       * Then the system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the sub-system attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingSubsystem() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addSubSystem( SUBSYSTEM_NAME );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );
         InventoryKey lSubSystem = InvUtils.getSystemByName( lSystem, SUBSYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lSubSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSubSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSubSystem ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a tracked inventory
       * Then the system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the tracked attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingTracked() {
         final InventoryKey lTracked = Domain.createTrackedInventory();
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addTracked( lTracked );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a assembly
       * Then the system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the assembly attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingAssembly() {
         final InventoryKey lEngine = Domain.createEngine();

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addEngine( lEngine );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has an assembly configuration
       * Then the system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the assembly attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingAssemblyConfiguration() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addEngine( new DomainConfiguration<Engine>() {

                        @Override
                        public void configure( Engine aBuilder ) {
                           aBuilder.setBarcode( ENGINE_BARCODE );
                        }
                     } );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );
         InventoryKey lEngine = getChildWithBarcode( lSystem, ENGINE_BARCODE );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with an added system that has a assembly that has a tracked
       * Then the system attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the assembly attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = aircraft
       * Then the tracked attributes are:
       *    next-highest = assembly
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       */
      @Test
      public void itBuildsWithAddedSystemHavingAssemblyHavingTracked() {
         final InventoryKey lTracked = Domain.createTrackedInventory();
         final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lTracked );
            }
         } );

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addEngine( lEngine );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lAircraft, SYSTEM_NAME );

         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built tracked
       * Then the pre-built tracked attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedPreBuiltTracked() {
         final InventoryKey lPreBuiltTracked = Domain.createTrackedInventory();

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( lPreBuiltTracked );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lPreBuiltTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lPreBuiltTracked ) );
      }


      /**
       * <pre>
       * When it builds with added tracked configuration
       * Then the child tracked attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedConfiguration() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setBarcode( TRACKED_BARCODE );
                  }
               } );
            }
         } );

         InventoryKey lChildTracked = getChildWithBarcode( lAircraft, TRACKED_BARCODE );

         Assert.assertEquals( lAircraft, getNextHighestInv( lChildTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lChildTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lChildTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added tracked that has a child tracked
       * Then the added tracked attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the child of added tracked attributes are:
       *    next-highest = added tracked
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedTrackedThatHasChildTracked() {
         final InventoryKey lChildTracked = Domain.createTrackedInventory();
         final InventoryKey lAddedTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.addTracked( lChildTracked );
                  }
               } );

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( lAddedTracked );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lAddedTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lAddedTracked ) );

         Assert.assertEquals( lAddedTracked, getNextHighestInv( lChildTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lChildTracked ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lChildTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built tracked that already has a parent
       * Then an exception is thrown
       * </pre>
       */
      @Test( expected = RuntimeException.class )
      public void itBuildsWithAddedPreBuiltTrackedThatAlreadyHasParent() {
         final InventoryKey lExistingParent = Domain.createTrackedInventory();
         final InventoryKey lPreBuiltTracked =
               Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

                  @Override
                  public void configure( TrackedInventory aBuilder ) {
                     aBuilder.setParent( lExistingParent );
                  }
               } );

         Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( lPreBuiltTracked );
            }
         } );
      }


      /**
       * <pre>
       * When it builds with an added pre-built assembly
       * Then the pre-built assembly attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedPreBuiltAssembly() {
         final InventoryKey lPreBuiltAssembly = Domain.createEngine();

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addTracked( lPreBuiltAssembly );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lPreBuiltAssembly ) );
         Assert.assertEquals( lAircraft, getHighestInv( lPreBuiltAssembly ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lPreBuiltAssembly ) );
      }


      /**
       * <pre>
       * When it builds with added assembly configuration
       * Then the assembly attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * </pre>
       */
      @Test
      public void itBuildsWithAddedAssemblyConfiguration() {
         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( new DomainConfiguration<Engine>() {

                  @Override
                  public void configure( Engine aBuilder ) {
                     aBuilder.setBarcode( ENGINE_BARCODE );
                  }
               } );
            }
         } );

         InventoryKey lEngine = getChildWithBarcode( lAircraft, ENGINE_BARCODE );

         Assert.assertEquals( lAircraft, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );
      }


      /**
       * <pre>
       * When it builds with an added assembly that has a tracked inventory
       * Then the assembly attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the tracked attributes are:
       *    next-highest = assembly
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       */
      @Test
      public void itBuildsWithAddedAssemblyHavingTracked() {
         final InventoryKey lTracked = Domain.createTrackedInventory();
         final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addTracked( lTracked );
            }
         } );

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lEngine );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added assembly that has a system that has a tracked inventory
       * Then the assembly attributes are:
       *    next-highest = aircraft
       *         highest = aircraft
       *        assembly = aircraft
       * Then the system attributes are:
       *    next-highest = assembly
       *         highest = aircraft
       *        assembly = assembly
       * Then the tracked attributes are:
       *    next-highest = system
       *         highest = aircraft
       *        assembly = assembly
       * </pre>
       */
      @Test
      public void itBuildsWithAddedAssemblyHavingSystemHavingTracked() {
         final InventoryKey lTracked = Domain.createTrackedInventory();
         final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem ) {
                     aSystem.setName( SYSTEM_NAME );
                     aSystem.addTracked( lTracked );
                  }
               } );
            }
         } );

         InventoryKey lSystem = InvUtils.getSystemByName( lEngine, SYSTEM_NAME );

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lEngine );
            }
         } );

         Assert.assertEquals( lAircraft, getNextHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine ) );

         Assert.assertEquals( lEngine, getNextHighestInv( lSystem ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lSystem ) );

         Assert.assertEquals( lSystem, getNextHighestInv( lTracked ) );
         Assert.assertEquals( lAircraft, getHighestInv( lTracked ) );
         Assert.assertEquals( lEngine, getAssemblyInv( lTracked ) );
      }


      /**
       * <pre>
       * When it builds with an added pre-built assembly that already has a parent
       * Then an exception is thrown
       * </pre>
       */
      @Test( expected = RuntimeException.class )
      public void itBuildsWithAddedPreBuiltAssemblyThatAlreadyHasParent() {
         final InventoryKey lExistingParent = Domain.createTrackedInventory();
         final InventoryKey lPreBuiltEngine =
               Domain.createEngine( new DomainConfiguration<Engine>() {

                  @Override
                  public void configure( Engine aBuilder ) {
                     aBuilder.setParent( lExistingParent );
                  }
               } );

         Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               aBuilder.addEngine( lPreBuiltEngine );
            }
         } );
      }


      /**
       * <pre>
       * When it builds with a variety of configured sub-inventory then all the attributes are correct.
       * This test is more to demonstrate a typical test setup with many inventory.
       * </pre>
       */

      // TODO
      @Test
      public void itBuildsWithMultipleConfiguredSubInventory() {

         final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

            @Override
            public void configure( Aircraft aBuilder ) {
               // On the aircraft, add an engine system with two installed engines, each with a fan
               // module (trk) and fan motor (trk).
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aSystem72 ) {
                     aSystem72.setName( "72 - Engine" );
                     aSystem72.addEngine( new DomainConfiguration<Engine>() {

                        @Override
                        public void configure( Engine aEngine1 ) {
                           aEngine1.setBarcode( "barcode_engine1" );
                           aEngine1.addTracked( new DomainConfiguration<TrackedInventory>() {

                              @Override
                              public void configure( TrackedInventory aEng1FanMod ) {
                                 aEng1FanMod.setBarcode( "barcode_eng1_fanMod" );
                                 aEng1FanMod
                                       .addTracked( new DomainConfiguration<TrackedInventory>() {

                                          @Override
                                          public void configure( TrackedInventory aEng1FanMotor ) {
                                             aEng1FanMotor.setBarcode( "barcode_eng1_fanMotor" );
                                          }
                                       } );
                              }
                           } );
                        }
                     } );
                     aSystem72.addEngine( new DomainConfiguration<Engine>() {

                        @Override
                        public void configure( Engine aEngine2 ) {
                           aEngine2.setBarcode( "barcode_engine2" );
                           aEngine2.addTracked( new DomainConfiguration<TrackedInventory>() {

                              @Override
                              public void configure( TrackedInventory aEng2FanMod ) {
                                 aEng2FanMod.setBarcode( "barcode_eng2_fanMod" );
                                 aEng2FanMod
                                       .addTracked( new DomainConfiguration<TrackedInventory>() {

                                          @Override
                                          public void configure( TrackedInventory aEng2FanMotor ) {
                                             aEng2FanMotor.setBarcode( "barcode_eng2_fanMotor" );
                                          }
                                       } );
                              }
                           } );
                        }
                     } );
                  }
               } );
               // On the aircraft, add landing gear system with three sub-landing gear systems
               // (main, left, right), each with a wheel (trk).
               aBuilder.addSystem( new DomainConfiguration<System>() {

                  @Override
                  public void configure( System aLandingGear ) {
                     aLandingGear.setName( "32 - Landing Gear" );
                     aLandingGear.addSubSystem( new DomainConfiguration<System>() {

                        @Override
                        public void configure( System aMainLandingGear ) {
                           aMainLandingGear.setName( "Main Landing Gear" );
                           aMainLandingGear
                                 .addTracked( new DomainConfiguration<TrackedInventory>() {

                                    @Override
                                    public void
                                          configure( TrackedInventory aMainLandingGearWheel ) {
                                       aMainLandingGearWheel
                                             .setBarcode( "barcode_mainLandingGear_wheel" );
                                    }
                                 } );
                        }
                     } );
                     aLandingGear.addSubSystem( new DomainConfiguration<System>() {

                        @Override
                        public void configure( System aLeftLandingGear ) {
                           aLeftLandingGear.setName( "Left Landing Gear" );
                           aLeftLandingGear
                                 .addTracked( new DomainConfiguration<TrackedInventory>() {

                                    @Override
                                    public void
                                          configure( TrackedInventory aLeftLandingGearWheel ) {
                                       aLeftLandingGearWheel
                                             .setBarcode( "barcode_leftLandingGear_wheel" );
                                    }
                                 } );
                        }
                     } );
                     aLandingGear.addSubSystem( new DomainConfiguration<System>() {

                        @Override
                        public void configure( System aRightLandingGear ) {
                           aRightLandingGear.setName( "Right Landing Gear" );
                           aRightLandingGear
                                 .addTracked( new DomainConfiguration<TrackedInventory>() {

                                    @Override
                                    public void
                                          configure( TrackedInventory aRightLandingGearWheel ) {
                                       aRightLandingGearWheel
                                             .setBarcode( "barcode_rightLandingGear_wheel" );
                                    }
                                 } );
                        }
                     } );
                  }
               } );
            }
         } );

         Assert.assertEquals( null, getNextHighestInv( lAircraft ) );
         Assert.assertEquals( lAircraft, getHighestInv( lAircraft ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lAircraft ) );

         InventoryKey lSystem72 = InvUtils.getSystemByName( lAircraft, "72 - Engine" );
         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem72 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem72 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem72 ) );

         InventoryKey lEngine1 = getChildWithBarcode( lSystem72, "barcode_engine1" );
         Assert.assertEquals( lSystem72, getNextHighestInv( lEngine1 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine1 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine1 ) );

         InventoryKey lEng1FanMod = getChildWithBarcode( lEngine1, "barcode_eng1_fanMod" );
         Assert.assertEquals( lEngine1, getNextHighestInv( lEng1FanMod ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEng1FanMod ) );
         Assert.assertEquals( lEngine1, getAssemblyInv( lEng1FanMod ) );

         InventoryKey lEng1FanMotor = getChildWithBarcode( lEng1FanMod, "barcode_eng1_fanMotor" );
         Assert.assertEquals( lEng1FanMod, getNextHighestInv( lEng1FanMotor ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEng1FanMotor ) );
         Assert.assertEquals( lEngine1, getAssemblyInv( lEng1FanMotor ) );

         InventoryKey lEngine2 = getChildWithBarcode( lSystem72, "barcode_engine2" );
         Assert.assertEquals( lSystem72, getNextHighestInv( lEngine2 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEngine2 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lEngine2 ) );

         InventoryKey lEng2FanMod = getChildWithBarcode( lEngine2, "barcode_eng2_fanMod" );
         Assert.assertEquals( lEngine2, getNextHighestInv( lEng2FanMod ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEng2FanMod ) );
         Assert.assertEquals( lEngine2, getAssemblyInv( lEng2FanMod ) );

         InventoryKey lEng2FanMotor = getChildWithBarcode( lEng2FanMod, "barcode_eng2_fanMotor" );
         Assert.assertEquals( lEng2FanMod, getNextHighestInv( lEng2FanMotor ) );
         Assert.assertEquals( lAircraft, getHighestInv( lEng2FanMotor ) );
         Assert.assertEquals( lEngine2, getAssemblyInv( lEng2FanMotor ) );

         InventoryKey lSystem32 = InvUtils.getSystemByName( lAircraft, "32 - Landing Gear" );
         Assert.assertEquals( lAircraft, getNextHighestInv( lSystem32 ) );
         Assert.assertEquals( lAircraft, getHighestInv( lSystem32 ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lSystem32 ) );

         InventoryKey lMainLandingGear = InvUtils.getSystemByName( lSystem32, "Main Landing Gear" );
         Assert.assertEquals( lSystem32, getNextHighestInv( lMainLandingGear ) );
         Assert.assertEquals( lAircraft, getHighestInv( lMainLandingGear ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lMainLandingGear ) );

         InventoryKey lMainLandingGearWheel =
               getChildWithBarcode( lMainLandingGear, "barcode_mainLandingGear_wheel" );
         Assert.assertEquals( lMainLandingGear, getNextHighestInv( lMainLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getHighestInv( lMainLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lMainLandingGearWheel ) );

         InventoryKey lLeftLandingGear = InvUtils.getSystemByName( lSystem32, "Left Landing Gear" );
         Assert.assertEquals( lSystem32, getNextHighestInv( lLeftLandingGear ) );
         Assert.assertEquals( lAircraft, getHighestInv( lLeftLandingGear ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lLeftLandingGear ) );

         InventoryKey lLeftLandingGearWheel =
               getChildWithBarcode( lLeftLandingGear, "barcode_leftLandingGear_wheel" );
         Assert.assertEquals( lLeftLandingGear, getNextHighestInv( lLeftLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getHighestInv( lLeftLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lLeftLandingGearWheel ) );

         InventoryKey lRightLandingGear =
               InvUtils.getSystemByName( lSystem32, "Right Landing Gear" );
         Assert.assertEquals( lSystem32, getNextHighestInv( lRightLandingGear ) );
         Assert.assertEquals( lAircraft, getHighestInv( lRightLandingGear ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lRightLandingGear ) );

         InventoryKey lRightLandingGearWheel =
               getChildWithBarcode( lRightLandingGear, "barcode_rightLandingGear_wheel" );
         Assert.assertEquals( lRightLandingGear, getNextHighestInv( lRightLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getHighestInv( lRightLandingGearWheel ) );
         Assert.assertEquals( lAircraft, getAssemblyInv( lRightLandingGearWheel ) );
      }

   }


   private static InventoryKey getNextHighestInv( InventoryKey aInvKey ) {
      return InvInvTable.findByPrimaryKey( aInvKey ).getNhInvNo();
   }


   private static InventoryKey getHighestInv( InventoryKey aInvKey ) {
      return InvInvTable.findByPrimaryKey( aInvKey ).getHInvNo();
   }


   private static InventoryKey getAssemblyInv( InventoryKey aInvKey ) {
      return InvInvTable.findByPrimaryKey( aInvKey ).getAssmblInvNo();
   }


   private static InventoryKey getChildWithBarcode( InventoryKey aParent, String aBarcode ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aParent, "nh_inv_no_db_id", "nh_inv_no_id" );
      lArgs.add( "barcode_sdesc", aBarcode );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_inv", lArgs );
      if ( lQs.next() ) {
         return lQs.getKey( InventoryKey.class, "inv_no_db_id", "inv_no_id" );
      }
      return null;
   }

}
