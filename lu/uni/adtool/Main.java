package lu.uni.adtool;

import lu.uni.adtool.ui.MainWindow;
import org.jvnet.substance.skin.SubstanceCremeLookAndFeel;

import javax.swing.*;

public final class Main
{
  private Main()
  {
  }
  public static void main(final String[] args)
  {
      try {
    	  //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	  
    	  //String lookAndFeel="javax.swing.plaf.metal.MetalLookAndFeel";   //Metal风格
    	  //String lookAndFeel="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";  //Windows风格
    	  //String lookAndFeel="com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";   //Windows经典风格
    	  //String lookAndFeel ="com.sun.java.swing.plaf.motif.MotifLookAndFeel";    //Motif风格
    	  //String lookAndFeel =UIManager.getCrossPlatformLookAndFeelClassName();  
    	  //UIManager.setLookAndFeel(lookAndFeel);
    	  
    	  JFrame.setDefaultLookAndFeelDecorated(true);
    	  JDialog.setDefaultLookAndFeelDecorated(true);
    	  //UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());    //
    	  //UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());      //
    	  UIManager.setLookAndFeel(new SubstanceCremeLookAndFeel());    //待定
    	  //UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
    	  //UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel()); //
    	  //UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());  //待定
    	  //UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());  //待定
    	  //UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());   //待定
    	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        new MainWindow(args);
      }
    });
  }
}
