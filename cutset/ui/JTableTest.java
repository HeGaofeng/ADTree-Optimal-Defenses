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
		frame=new JFrame("��ȫ��������ָ��");
		frame.setBounds(200, 200, 1000, 740);// �����С
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tables.png")));  //���õ�ǰ���ڵı���ͼ��
		Container c = frame.getContentPane();
		
		/////////////////////
		String[] columnNames = { "Defense-Node", "Defense-Cost", "Success-Probability"};// ��������
		String[][] tableValues = new String[20][columnNames.length];// �������飬�����洢�������

		for (int row = 0; row < tableValues.length; row++) {
			for (int column = 0; column < columnNames.length; column++) {
				tableValues[row][column] = columnNames[column] + row;// �����ֵ
			}
		}
		
		
		
		/////////////////////////
		table = new JTable(tableValues, columnNames);
		
		//JScrollPane sc = new JScrollPane(table);
		c.add(table, BorderLayout.CENTER);

		table.setSelectionForeground(Color.RED);// ����ǰ��ɫ��������ɫ
		//table.setBackground(Color.PINK);
		table.setSelectionBackground(Color.yellow);// ����ɫ
		table.setRowHeight(30);// �����и�30����
		

		//System.out.println("�����" + table.getRowCount() + "��" + table.getColumnCount() + "��");
		
		
		frame.setVisible(true);
	

	}
	
	
	
}

