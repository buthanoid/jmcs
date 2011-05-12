/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package fest.common;

import static org.fest.swing.timing.Pause.*;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.ToolTipManager;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.fixture.ComponentFixture;
import org.fest.swing.image.ImageException;
import org.fest.swing.image.ScreenshotTaker;
import org.fest.swing.junit.v4_5.runner.GUITestRunner;
import org.fest.swing.security.NoExitSecurityManagerInstaller;
import org.fest.util.Files;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * This custom FestSwingJUnitTestCase uses :
 * - custom FestSwingTestCaseTemplate
 * - GUITestRunner
 * - NoExitSecurityManagerInstaller
 * - define a proper way to start / stop the Swing application (not individual components)
 * 
 * @author bourgesl
 *
 * Original header :
 *
 * Understands a template for test cases that use FEST-Swing and JUnit. This template installs a
 * <code>{@link FailOnThreadViolationRepaintManager}</code> to catch violations of Swing thread rules and manages both
 * creation and clean up of a <code>{@link Robot}</code>.
 * @since 1.1
 *
 * @author Alex Ruiz
 */

/*
 * Use GUI test runner to take a screenshot of a failed GUI test
 */
@RunWith(GUITestRunner.class)
public class FestSwingCustomJUnitTestCase extends FestSwingCustomTestCaseTemplate {

  /** Class logger */
  protected static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(
          FestSwingCustomJUnitTestCase.class.getName());
  /** null delay (0 ms) */
  protected static final int NULL_DELAY = 0;
  /** very short delay (50 ms) */
  protected static final int VERY_SHORT_DELAY = 50;
  /** short delay (100 ms) */
  protected static final int SHORT_DELAY = 100;
  /** medium delay (500 ms) */
  protected static final int MEDIUM_DELAY = 500;
  /** long delay (5 s) */
  protected static final int LONG_DELAY = 5000;
  /** pause before screen shot */
  private static long SCREENSHOT_DELAY = SHORT_DELAY;
  /** screenshot taker */
  private static ScreenshotTaker screenshotTaker;
  /** screenshot folder as a file */
  private static File screenshotFolderFile;
  /** screenshot folder as a string*/
  private static String screenshotFolder;

  /**
   * Public constructor required by JUnit
   */
  public FestSwingCustomJUnitTestCase() {
    super();
  }

  /**
   * Installs a <code>{@link FailOnThreadViolationRepaintManager}</code> to catch violations of Swing threading rules.
   *
   * Prepare the screenshot taker stored in the folder ./fest-screenshots/
   */
  @BeforeClass
  public static final void setUpOnce() {
    FailOnThreadViolationRepaintManager.install();

    // does not work within netbeans (fork JVM) :
    /* NoExitSecurityManagerInstaller.installNoExitSecurityManager(); */

    screenshotTaker = new ScreenshotTaker();

    screenshotFolderFile = null;
    try {
      final String currentFolderPath = Files.currentFolder().getCanonicalPath();

      screenshotFolderFile = new File(currentFolderPath + File.separator + "fest-screenshots");

      if (screenshotFolderFile.exists()) {
        if (logger.isLoggable(Level.INFO)) {
          logger.info("setUpOnce : delete any existing file in directory = " + screenshotFolderFile);
        }

        for (File f : screenshotFolderFile.listFiles()) {
          Files.delete(f);
        }
      } else {
        screenshotFolderFile.mkdir();
      }

      screenshotFolder = screenshotFolderFile.getCanonicalPath() + File.separator;

      if (logger.isLoggable(Level.INFO)) {
        logger.info("setUpOnce : screenshot folder = " + screenshotFolder);
      }

    } catch (IOException ioe) {
      throw new RuntimeException("unable to create screenshot folder : " + screenshotFolderFile, ioe);
    }
  }

  /**
   * Uninstalls the <code>{@link NoExitSecurityManagerInstaller}</code>.
   */
  @AfterClass
  public static void tearDownOnce() {
    if (logger.isLoggable(Level.INFO)) {
      logger.info("tearDownOnce : tests done");
    }
    // waits for application to exit properly
    pauseMedium();
  }

  /**
   * Sets up this test's fixture, starting from creation of a new <code>{@link Robot}</code>.
   * @see #setUpRobot()
   * @see #onSetUp()
   */
  @Before
  public final void setUp() {
    setUpRobot();
    onSetUp();
  }

  /**
   * Subclasses need set up their own test fixture in this method. This method is called <strong>after</strong>
   * executing <code>{@link #setUp()}</code>.
   */
  protected void onSetUp() {
  }

  /**
   * Cleans up any resources used in this test. After calling <code>{@link #onTearDown()}</code>, this method cleans up
   * resources used by this test's <code>{@link Robot}</code>.
   * @see #cleanUp()
   * @see #onTearDown()
   */
  @After
  public final void tearDown() {
    try {
      onTearDown();
    } finally {
      cleanUp();
    }
  }

  /**
   * Subclasses need to clean up resources in this method. This method is called <strong>before</strong> executing
   * <code>{@link #tearDown()}</code>.
   */
  protected void onTearDown() {
  }

  /* Utility methods */
  /**
   * Enable / disable tooltips
   * @param flag value to set
   */
  protected static void enableTooltips(final boolean flag) {
    GuiActionRunner.execute(new GuiTask() {

      public void executeInEDT() {
        ToolTipManager.sharedInstance().setEnabled(flag);
      }
    });
  }

  /**
   * Sleeps for @see #SHORT_DELAY
   */
  protected static void pauseShort() {
    pause(SHORT_DELAY);
  }

  /**
   * Sleeps for @see #MEDIUM_DELAY
   */
  protected static void pauseMedium() {
    pause(MEDIUM_DELAY);
  }

  /**
   * Sleeps for @see #LONG_DELAY
   */
  protected static void pauseLong() {
    pause(LONG_DELAY);
  }

  /**
   * Sleeps for given delay in seconds
   * @param delay seconds to sleep
   */
  protected static void pauseSeconds(final long delay) {
    pause(delay * 1000l);
  }

  /**
   * Define the screenshot delay (@see #SCREENSHOT_DELAY)
   * @param delay milliseconds
   */
  protected static void defineScreenshotDelay(final long delay) {
    SCREENSHOT_DELAY = delay;
  }

  /**
   * Sleeps for @see #SCREENSHOT_DELAY
   */
  protected static void pauseBeforeScreenshot() {
    if (SCREENSHOT_DELAY > 0l) {
      pause(SCREENSHOT_DELAY);
    }
  }

  /**
   * Return the screenshot Folder as a file
   * @return file
   */
  protected static File getScreenshotFolder() {
    return screenshotFolderFile;
  }

  /**
   * Takes a screenshot of the desktop and saves it as a PNG file.
   * @param fileName the file name (including the png extension)
   */
  protected static void saveScreenshot(final String fileName) {
    saveScreenshot((Component) null, fileName);
  }

  /**
   * Takes a screenshot of the given fixture and saves it as a PNG file.
   * @param fixture the given fixture to extract its component.
   * @param fileName the file name (including the png extension)
   */
  protected static void saveScreenshot(final ComponentFixture<?> fixture, final String fileName) {
    saveScreenshot(fixture.component(), fileName);
  }

  /**
   * Takes a screenshot of the given <code>{@link java.awt.Component}</code> and saves it as a PNG file.
   * @param c the given component.
   * @param fileName the file name (including the png extension)
   */
  private static void saveScreenshot(final Component c, final String fileName) {
    final String filePath = screenshotFolder + fileName;
    try {
      pauseBeforeScreenshot();

      if (screenshotTaker != null) {
        if (c == null) {
          screenshotTaker.saveDesktopAsPng(filePath);
        } else {
          screenshotTaker.saveComponentAsPng(c, filePath);
        }

        if (logger.isLoggable(Level.INFO)) {
          logger.info("Screenshot saved as " + filePath);
        }
      }
    } catch (Exception e) {
      if (logger.isLoggable(Level.WARNING)) {
        logger.log(Level.WARNING, "Unable to take screenshot : " + filePath, e);
      }
    }
  }

  /**
   * Takes a screenshot of the given <code>{@link java.awt.Component}</code>.
   * @param fixture the given fixture to extract its component.
   * @return a screenshot of the given component.
   * @throws SecurityException if <code>readDisplayPixels</code> permission is not granted.
   */
  protected static BufferedImage takeScreenshotOf(final ComponentFixture<?> fixture) {
    return takeScreenshotOf(fixture.component());
  }

  /**
   * Takes a screenshot of the given <code>{@link java.awt.Component}</code>.
   * @param c the given component.
   * @return a screenshot of the given component.
   * @throws SecurityException if <code>readDisplayPixels</code> permission is not granted.
   */
  private static BufferedImage takeScreenshotOf(final Component c) {
    pauseBeforeScreenshot();

    return screenshotTaker.takeScreenshotOf(c);
  }

  /**
   * Save the given image as a PNG file.
   * @param image the image to save.
   * @param fileName the file name (including the png extension)
   * @throws ImageException if the given file path is <code>null</code> or empty.
   * or if the given file path does not end with ".png".
   * or if the given file path belongs to a non-empty directory.
   * or if an I/O error prevents the image from being saved as a file.
   */
  protected static void saveImage(final BufferedImage image, final String fileName) throws ImageException {
    final String filePath = screenshotFolder + fileName;
    try {
      screenshotTaker.saveImage(image, filePath);

      if (logger.isLoggable(Level.INFO)) {
        logger.info("Screenshot saved as " + filePath);
      }
    } catch (Exception e) {
      if (logger.isLoggable(Level.WARNING)) {
        logger.log(Level.WARNING, "Unable to save screenshot : " + filePath, e);
      }
    }
  }
}
