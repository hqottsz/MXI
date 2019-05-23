# Query Test Module  

This module provides support for creating tests that require a database. The database will be generated based on the base database for this repository (with disabled validation.)

## Database Information

Execution of database tests requires a connection to a database. This is provided by the `database.properties` file that is generated at `src/main/resources`. The properties file is generated using the `gradlew :am-query-test:updateDatabaseProperties` command; this is done as part of `build` or `assemble` tasks.

The database properties file will contain the following properties:

    database.username=
    database.password=
    database.host=
    database.port=
    database.service=

# Data

## Mutability

When loading the data, we need to select whether that data is immutable or mutable. Immutable data will be setup once for all tests (and rolled back after the whole test class) while mutable data will be setup once per test (and rolled back after each test.) This allows for fast query tests and service tests that mutate data.

### Immutable Data

For immuable data, we want to create 1 connection for all tests. This is done by creating the database connection at the class level. The `DatabaseConnectionRule` can be annotated with a `@ClassRule` to instantiate the database connection. A method can be annotated with `@BeforeClass` to load the data once.

    @RunWith( BlockJUnit4ClassRunner.class )
    public final class QueryUnderTest {

       @ClassRule
       public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

       @BeforeClass
       public static void loadData() {
          DataLoaders.load( sConnection.getConnection(), QueryUnderTest.class );
       }

       @Test
       public void testIt() {
          // TODO: Implement tests!
       }
    }

### Mutable Data

Mutable Data requires rolling back the database after every test. This is done by creating the database connection for each test. The `DatabaseConnectionRule` can be annotated with a `@Rule` to instantiate the database connection (per test.) A method can be annotated with `@Before` to load the data once (per test.)

    @RunWith( BlockJUnit4ClassRunning.class )
    public final class QueryUnderTest {
       @Rule
       public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

       @Before
       public void loadData() {
          DataLoaders.load( iConnection.getConnection(), getClass() );
       }

       @Test
       public void testIt() {
          // TODO: Implement tests!
       }
    }

## Loading Data (Query)

The hardest part of query tests is setting up the proper data for the query. We have a number of loading tools available to you.

If a loader requires specifying the date as a string (e.g.: `XmlLoader`, `YamlLoader`), they must be in the `YYYY-MM-DD HH:MM:SS` format.

### All Data Loader

The `DataLoader` tries to use all available loaders to load the resource. The `DataLoader` takes a prefix for all the different loaders and sees if the resource is available for that loader.

    DataLoader.load(iConnection, getClass(), "DataFile" );

The `DataLoader` will try to load `${prefix}.sql`, `${prefix}.xml`, and `${prefix}.yaml`. If the file does not exist, no warning or exception will be issued.

### SQL Loader

The `SQLLoader` allows you to have plain SQL files that are executed as-is.

    public void loadData() {
       SqlLoader.load( iConnection, getClass(), "SQLFile.sql" );
    }

### XML Loader

The `XMLLoader` loads from a `dataset` xml schema.

    public void loadData() {
       XmlLoader.load( iConnection, getClass(), "XMLFile.xml" );
    }

The dataset file format is:

    <dataset>
       <${table_name}
           ${column_name} = "${value}"
           ${column_name} = "${value}"
           />
    </dataset>

### Yaml Loader

The `YamlLoader` is loads from a `yaml` file.

    @Before
    public void loadData() {
       YamlLoader.load( iConnection, getClass(), "YamlFile.yaml" );
    }

The yaml file format is:

    - ${table_name}
        ${column_name}: ${value}
    - ${table_name}
        ${column_name}: ${value}

## Loading Data (Business Logic)

To load data for business logic, the relation between what is needed and what you execute becomes a lot less clear. To facilitate this, we've created a way to create domain entities for tests.

This is typically called by the `com.mxi.am.domain.Domain` class.

### Using Domain Builders

Domain Builders allows us to quickly set up a domain entity without worrying too much about what data needs to be stored in the back-end.

    @Test
    public void testEntity() {
       AircraftKey lAircraftKey = Domain.createAircraft(new DomainConfiguration<Aircraft>() {
          public void configure(Aircraft aAircraft) {
             aAircraft.setUsage(DataTypeKey.HOURS, 15);
          }
       });
    }

### Creating Domain Builders

Each domain entity needs to have a dedicated entity object and builder for it.

1. Create the entity pojo object

    public final class Aircraft {

       Aircraft() {
       }

       private final Map<DataTypeKey, Double> iUsages = new HashMap<>();
       public void addUsage(DataTypeKey aDataType, Double aUsage) {
          iUsages.put(aDataType, aUsage);
       }

       public Map<DataTypeKey, Double> getUsages() {
          return iUsages;
       }
    }

2. Create the builder for the entity

    public final class AircraftBuilder {
       private AircraftBuilder() {
          // utility class
       }

       public static InventoryKey build(Aircraft aAircraft) {
          // TODO: Use AbstractTable to create the aircraft entity
          return lAircraftKey;
       }
    }

3. Add the entity to the Domain object

    public InventoryKey createAircraft(DomainConfiguration<Aircraft> aDomainConfiguration) {
       Aircraft lAircraft = new Aircraft();
       aDomainConfiguration.configure(lAircraft);
       return AircraftBuilder.build(lAircraft);
    }

# Query Tests (*.qrx)

Most of Maintenix uses `.qrx` files to execute queries. The following example shows how the query test can be called:

    @RunWith( BlockJunit4ClassRunner.class )
    public final class QrxTest {
       @ClassRule
       public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();

       @BeforeClass
       public static void loadData() {
          DataLoaders.load( sConnection.getConnection(), getClass() );
       }

       @Test
       public void testQuery() {
          QuerySet lQs = execute( 1, 2, 3 );

          // TODO: See Testing with Fixed Ordering / No Ordering
       }

       public QuerySet execute( int aArg1, int aArg2, int aArg3 ) {
          DataSetArgument lArgs = new DataSetArgument();
          lArgs.add( "aArg1", aArg1 );
          lArgs.add( "aArg2", aArg2 );
          lArgs.add( "aArg3", aArg3 );

          return sConnection.getQao().executeQuery( "com.mxi.am.Qrx", lArgs );
       }
    }

## Testing with Fixed Ordering

Some queries require returning a set of data in a fixed order. When this occurs, we need to validate that the order it returns data in is the right order.


    @Test
    public void testQuery() {
       QuerySet lQs = execute( 1, 2, 3 );

       Assert.assertTrue( lQs.next() );
       Assert.assertEquals( 1, lQs.getInt( "value1" ) );
       Assert.assertEquals( 2, lQs.getInt( "value2" ) );
       Assert.assertEquals( 3, lQs.getInt( "value3" ) );

       Assert.assertFalse( lQs.next() );
    }

## Testing with No Ordering

Some queries do not have any promise of order. When this occurs, we need to make sure that we can test the contents by getting the right row to assert.


    @Test
    public void testQuery() {
       QuerySet lQs = execute( 1, 2, 3 );

       Assert.assertEquals( 1, lQs.getRowCount() );

       InventoryKey lImportantKey = new InventoryKey( 4650, 1 );
       QuerySetRowSelector.select(lQs, new QuerySetKeyColumnPredicate( lImportantKey, "inv_no_db_id", "inv_no_id" ) );
       Assert.assertEquals( 1, lQs.getInt( "value1" ) );
       Assert.assertEquals( 2, lQs.getInt( "value2" ) );
       Assert.assertEquals( 3, lQs.getInt( "value3" ) );
    }

# Service Class Tests (*Service.java)

## Testing Service Classes with Java EE dependencies

Most Service Classes depends on Java EE. In order to test code with Java EE dependencies, we need to fake them out. We use the `FakeJavaEeDependenciesRule` to fake all the Java EE dependencies.

    public final class SomeServiceTest {

       @Rule
       public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

       @Rule
       public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

       private SomeService iService;

       @Before
       public void setUp() {
          iService = new SomeService();
       }

       @Test
       public void itServes() {
          // Arrange
          // See Data Loaders section

          // Act
          int lValue = iService.addNumbers(1, 2, 3);

          // Assert
          Assert.assertEquals(6, lValue);
       }
    }

## Testing Service Classes with a Specified User

Some services require the 'current user.' In these cases, we have to use the `OperateAsUser` rule to override that behavior to return the user we're behaving as.

    @RunWith( BlockJUnit4ClassRunner.class )
    public final class SomeServiceTest {

       @Rule
       public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

       @Rule
       public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

       @Rule
       public OperateAsUser iOperateAsUser = new OperateAsUser(1000, "currentuser");

       private SomeService iService;

       @Before
       public void setUp() {
          iService = new SomeService();
       }

       @Test
       public void itServes() {
          // Arrange
          // See Data Loaders section

          // Act
          int lValue = iService.addNumbers(1, 2, 3);

          // Assert
          Assert.assertEquals(6, lValue);
       }
    }