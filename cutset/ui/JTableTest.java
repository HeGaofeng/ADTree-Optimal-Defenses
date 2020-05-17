package cutset.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.*;

import cutset.settool.Test;
import cutset.tree.ATNode;
import lu.uni.adtool.adtree.ADTreeForGui;
import lu.uni.adtool.adtree.ADTreeNode;
import lu.uni.adtool.domains.ValuationDomain;
import lu.uni.adtool.domains.rings.RealG0;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.ui.ADTreeCanvas;
import lu.uni.adtool.ui.ADTreeView;
import net.infonode.docking.View;


public class JTableTest extends JFrame {
	protected JTable table;
	protected JFrame frame;
	
	
	public void initUI() {
		frame=new JFrame("安全属性量化指标");
		frame.setBounds(200, 200, 1000, 740);// 窗体大小
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tables.png")));  //设置当前窗口的标题图标
		Container c = frame.getContentPane();
		
		/////////////////////
		String[] columnNames = { "Defense-Node", "Defense-Cost", "Success-Probability"};// 定义表格列
		String[][] tableValues = new String[20][columnNames.length];// 定义数组，用来存储表格数据

		for (int row = 0; row < tableValues.length; row++) {
			for (int column = 0; column < columnNames.length; column++) {
				tableValues[row][column] = columnNames[column] + row;// 给表格赋值
			}
		}
		
		
		
		/////////////////////////
		table = new JTable(tableValues, columnNames);
		
		//JScrollPane sc = new JScrollPane(table);
		c.add(table, BorderLayout.CENTER);

		table.setSelectionForeground(Color.RED);// 设置前景色，字体颜色
		//table.setBackground(Color.PINK);
		table.setSelectionBackground(Color.yellow);// 背景色
		table.setRowHeight(30);// 设置行高30像素
		

		//System.out.println("表格共有" + table.getRowCount() + "行" + table.getColumnCount() + "列");
		
		
		frame.setVisible(true);
	

	}
	
	
	
}

