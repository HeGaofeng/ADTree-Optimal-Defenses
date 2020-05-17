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
    	  
    	  //String lookAndFeel="javax.swing.plaf.metal.MetalLookAndFeel";   //Metal���
    	  //String lookAndFeel="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";  //Windows���
    	  //String lookAndFeel="com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";   //Windows������
    	  //String lookAndFeel ="com.sun.java.swing.plaf.motif.MotifLookAndFeel";    //Motif���
    	  //String lookAndFeel =UIManager.getCrossPlatformLookAndFeelClassName();  
    	  //UIManager.setLookAndFeel(lookAndFeel);
    	  
    	  JFrame.setDefaultLookAndFeelDecorated(true);
    	  JDialog.setDefaultLookAndFeelDecorated(true);
    	  //UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());    //
    	  //UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());      //
    	  UIManager.setLookAndFeel(new SubstanceCremeLookAndFeel());    //����
    	  //UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
    	  //UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel()); //
    	  //UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());  //����
    	  //UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());  //����
    	  //UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());   //����
    	
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
