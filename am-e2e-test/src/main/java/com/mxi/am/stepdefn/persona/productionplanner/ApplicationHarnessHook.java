package com.mxi.am.stepdefn.persona.productionplanner;

import static org.junit.Assert.assertFalse;
import ilog.views.gantt.IlvGanttProduct;

import java.awt.Window;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.mxi.am.driver.ppc.Wait;
import com.mxi.am.driver.ppc.login.LoginDialogDriver;
import com.mxi.driver.swing.condition.SwingConditions;
import com.mxi.driver.swing.driver.WindowDriver;
import com.mxi.mx.ppc.JvmTerminator;
import com.mxi.mx.ppc.PpcApplication;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;


/**
 * This hooks PPC to properly start-up and tear-down
 */
public class ApplicationHarnessHook {

   private static final Logger LOGGER = Logger.getLogger( ApplicationHarnessHook.class );

   @Inject
   private LoginDialogDriver iLoginDialog;


   @After( "@RunPPCTest" )
   public void closeApplication() {

      LOGGER.info( "Close PPC Client" );

      // Make sure no window pops up before we get a chance to close it.
      Wait.pause( 1000 );
      Wait.until( SwingConditions.systemIsIdle() );

      // Update all the windows so that they do not exit the JVM when they are terminated
      for ( Window lWindow : Window.getWindows() ) {
         if ( lWindow instanceof JFrame ) {
            ( ( JFrame ) lWindow ).setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
         }
      }

      closeAllWindows();

      // Wait for all the Windows to finish what they're doing
      JvmTerminator.getInstance().terminate();
      Wait.until( SwingConditions.systemIsIdle() );
      JvmTerminator.resetInstance();
   }


   /**
    * Close all windows
    *
    */
   private void closeAllWindows() {
      while ( isAnyWindowVisible() ) {
         for ( Window lWindow : Window.getWindows() ) {
            // Try to dispose of the window properly first
            WindowDriver.close( lWindow );

            // If the window doesn't dispose properly from the window closing event, let it
            // disappear so it at least doesn't impact other tests
            if ( lWindow.isVisible() ) {
               lWindow.setVisible( false );
            }
         }
         Wait.until( SwingConditions.systemIsIdle() );
      }

   }


   private boolean isAnyWindowVisible() {
      for ( Window lWindow : Window.getWindows() ) {
         if ( lWindow.isVisible() ) {
            return true;
         }
      }
      return false;
   }


   @Before( "@RunPPCTest" )
   public void startApplication( Scenario aScenario ) {
      // Ensure that any previous tests have properly shut down; without this, we cannot accurately
      // identify which window is 'active'
      if ( isAnyWindowVisible() ) {
         closeAllWindows();
      }
      for ( Window lWindow : Window.getWindows() ) {
         assertFalse( lWindow.getName() + " must not be visible.", lWindow.isVisible() );
      }

      // Set the JVM Terminator so that it doesn't shut down the JVM. If the JVM terminators, then
      // the junit runner shuts down. No reports will be generated nor will any future tests will
      // be executed.
      JvmTerminator.setInstance( new JvmTerminator() {

         @Override
         public void terminate( int aStatus ) {
            PpcApplication.getInstance().getExecutorService().shutdown();
            try {
               PpcApplication.getInstance().getExecutorService().awaitTermination( 10,
                     TimeUnit.SECONDS );
            } catch ( InterruptedException e ) {
               Thread.currentThread().interrupt();
            }
         }
      } );

      LOGGER.info( "Start PPC Client" );

      LOGGER.info( "JViews Gantt " + IlvGanttProduct.getVersion() + "."
            + IlvGanttProduct.getMinorVersion() + "." + IlvGanttProduct.getSubMinorVersion()
            + " patch" + IlvGanttProduct.getPatchLevel() );

      PpcApplication.main( null );
      iLoginDialog.waitUntilVisible();
   }
}
