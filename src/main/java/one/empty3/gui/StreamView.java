package one.empty3.gui;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Fri Jun 21 08:41:31 CEST 2019
 */



/**
 * @author Manuel Dahmen
 */
public class StreamView  {

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        this.Streams = new JFrame();

        //======== Streams ========
        {
            Container StreamsContentPane = this.Streams.getContentPane();
            StreamsContentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
        }

        this.Streams.pack();
        this.Streams.setLocationRelativeTo(this.Streams.getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame Streams;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
