/** *****************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ***************************************************************************** */
package fr.jmmc.jmal.star;

import fr.jmmc.jmcs.gui.component.MessagePane;
import fr.jmmc.jmcs.gui.component.SearchField;
import fr.jmmc.jmcs.gui.component.StatusBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * Store informations relative to a star.
 *
 * @author Sylvain LAFRASSE, Laurent BOURGES.
 */
public class StarResolverWidget extends SearchField implements Observer {

    /** default serial UID for Serializable interface */
    private static final long serialVersionUID = 1;
    /** Container to store retrieved star properties */
    private final Star _star;
    /** Menu to choose SIMBAD mirror */
    private final static JPopupMenu _mirrorPopupMenu;

    // Static initialization
    static {
        // init mirrorPopup menu on first display
        _mirrorPopupMenu = new JPopupMenu();

        // Add title
        JMenuItem menuItem = new JMenuItem("Choose Simbad Location:");
        menuItem.setEnabled(false);
        _mirrorPopupMenu.add(menuItem);

        // And populate with StarResolver mirrors
        final Set<String> mirrors = StarResolver.getSimbadMirrors();
        for (final String mirror : mirrors) {
            menuItem = new JMenuItem(mirror);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StarResolver.setSimbadMirror(mirror);
                }
            });
            _mirrorPopupMenu.add(menuItem);
        }
    }

    /**
     * Creates a new StarResolverWidget object dedicated to unique star resolution.
     * @param star star model
     */
    public StarResolverWidget(final Star star) {
        super("Simbad", _mirrorPopupMenu);

        _star = star;
        _star.addObserver(this);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String starName = e.getActionCommand().trim();

                if (starName.length() > 0) {
                    _logger.info("Searching CDS Simbad data for star '{}'.", starName);

                    StatusBar.show("searching CDS Simbad data for star '"
                            + starName + "'... (please wait, this may take a while)");

                    // Disable search field while request processing to avoid concurrent calls :
                    setEnabled(false);

                    new StarResolver(starName, _star).resolve();
                }
            }
        });
    }

    /**
     * Return the star model
     * @return star model
     */
    public final Star getStar() {
        return _star;
    }

    /**
     * Creates a new StarResolverWidget object dedicated to multiple star resolutions.
     * @param stars star list
     */
    public StarResolverWidget(final StarList stars) {

        _star = null;
        stars.addObserver(this);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String starNames = e.getActionCommand().trim();

                if (starNames.length() > 0) {
                    _logger.info("Searching CDS Simbad data for star '{}'.", starNames);

                    StatusBar.show("searching CDS Simbad data for stars '"
                            + starNames + "'... (please wait, this may take a while)");

                    // Disable search field while request processing to avoid concurrent calls :
                    setEnabled(false);

                    new StarResolver(starNames, stars).multipleResolve();
                }
            }
        });
    }

    /**
     * Automatically called on attached Star changes.
     */
    @Override
    public final void update(final Observable o, final Object arg) {
        Star.Notification notification = Star.Notification.UNKNOWN;

        if (arg != null && arg instanceof Star.Notification) {
            notification = (Star.Notification) arg;
        }

        switch (notification) {
            case QUERY_COMPLETE:
                StatusBar.show("CDS Simbad star resolution done.");
                break;

            case QUERY_ERROR:
                final String errorMessage = _star.consumeCDSimbadErrorMessage();
                if (errorMessage != null) {
                    MessagePane.showErrorMessage(errorMessage, "CDS Simbad problem");
                }

                StatusBar.show("CDS Simbad star resolution failed.");
                break;

            default:
                return;
        }

        // Enable search field after request processing done :
        setEnabled(true);
    }

    /**
     * Main - for StarResolverWidget demonstration and test only.
     * @param args unused
     */
    public static void main(final String[] args) {
        // GUI initialization
        final JFrame frame = new JFrame("StarResolverWidget Demo");

        // Force to exit when the frame closes :
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
         // Resolver initialization
         final Star star = new Star();
         star.addObserver(new Observer() {
         @Override
         public void update(Observable o, Object arg) {
         _logger.info("Star changed:\n{}", star);
         }
         });
        
         final JPanel panel = new JPanel();
         panel.add(new StarResolverWidget(star));
         */

        // Resolver initialization
        final StarList stars = new StarList();
        stars.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                _logger.info("Star list complete:\n{}", stars);
            }
        });

        final JPanel panel = new JPanel();
        panel.add(new StarResolverWidget(stars));

        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
/*___oOo___*/
