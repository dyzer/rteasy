package de.uniluebeck.iti.rteasy.gui;

/**
 * Klasse f&uuml;r Simulator-Optionen
 */

import javax.swing.*;
import java.util.*;
import java.io.*;

public class RTOptions {

	public final static int TIMING_2EDGES = 0;
	public final static int TIMING_1EDGE = 1;
	public static int timing = TIMING_1EDGE;
	public static boolean calculateSignals = false;
	public static boolean noisyWarnings = false;
	public static boolean emitZeroDriver = true;
	public static boolean forceInputs = true;
	public static String design_library = "rteasy";
	public static Locale locale = new Locale("en", "US");

	public static void loadOptions() {
		try {
			String propfname = System.getProperty("user.home")
					+ System.getProperty("file.separator") + "rteasy.cfg";
			Properties p = new Properties();
			p.load(new FileInputStream(new File(propfname)));
			/*
			 * if(p.getProperty("forceInputs") != null) { forceInputs =
			 * Boolean.getBoolean(p.getProperty("forceInputs"));
			 * System.err.println("reading forceInputs = "+forceInputs); }
			 * if(p.getProperty("emitZeroDriver") != null) { emitZeroDriver =
			 * Boolean.getBoolean(p.getProperty("emitZeroDriver"));
			 * System.err.println("reading emitZeroDriver = "+emitZeroDriver); }
			 */
			if (p.getProperty("noisyWarnings") != null)
				noisyWarnings = Boolean.getBoolean(p
						.getProperty("noisyWarnings"));
			if (p.getProperty("locale_Language") != null
					&& p.getProperty("locale_Country") != null)
				locale = new Locale(p.getProperty("locale_Language"),
						p.getProperty("locale_Country"));

			if (p.getProperty("plaf") != null) {
				UIManager.setLookAndFeel(p.getProperty("plaf"));
				// SwingUtilities.updateComponentTreeUI(____); // possibly
				// needed
			}
		} catch (Throwable t) { // fall back to default
			System.err.println("INTERNAL ERROR -- " + t.getMessage());
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Throwable t2) {
				System.err.println(t.getLocalizedMessage());
			}
		}
	}

	public static void dumpOptions() {
		locale = IUI.getLocale();
		try {
			System.err.println("options: ");
			String propfname = System.getProperty("user.home")
					+ System.getProperty("file.separator") + "rteasy.cfg";
			System.err.println("cfg file: " + propfname);
			System.err.println("emitZeroDriver = "
					+ Boolean.toString(emitZeroDriver));
			System.err
					.println("forceInputs = " + Boolean.toString(forceInputs));
			System.err.println("noisyWarnings = "
					+ Boolean.toString(noisyWarnings));
			System.err.println("locale_Language = " + locale.getLanguage());
			System.err.println("locale_Country = " + locale.getCountry());
			System.err.println("plaf = "
					+ UIManager.getLookAndFeel().getClass().getName());
		} catch (Throwable t) {
		} // doesn't matte
	}

	public static void saveOptions() {
		locale = IUI.getLocale();
		try {
			String propfname = System.getProperty("user.home")
					+ System.getProperty("file.separator") + "rteasy.cfg";
			Properties p = new Properties();
			p.put("emitZeroDriver", Boolean.toString(emitZeroDriver));
			p.put("forceInputs", Boolean.toString(forceInputs));
			p.put("noisyWarnings", Boolean.toString(noisyWarnings));
			p.put("locale_Language", locale.getLanguage());
			p.put("locale_Country", locale.getCountry());
			p.put("plaf", UIManager.getLookAndFeel().getClass().getName());
			p.store(new FileOutputStream(new File(propfname)),
					"RTeasy settings");
		} catch (Throwable t) {
		} // doesn't matter
	}

}
