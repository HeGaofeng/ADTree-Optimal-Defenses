package cutset.ui;

import javax.swing.*;
import java.awt.*;

public class SetWindow extends Frame {
	protected JFrame frame;
	protected String result;
	protected cutset.ui.TextAreaMenu jTextArea; //自定义类TextAreaMenu

	public void initUI() {
		frame = new JFrame("安全效益评估结果");    //  Javax.swing.JRrame类 构造方法：创建一个窗口对象
		Dimension dim = getScreenSize(0.5 , 0.4);    //自定义方法getScreenSize获取当前屏幕的尺寸——宽度和高度
		frame.setSize(dim.width, dim.height);      //设置窗口对象的大小
		frame.setLocationRelativeTo(null);  //设置当前窗口相对于指定组件的位置
		frame.setDefaultCloseOperation(2);   //设置当前窗口的默认关闭操作
		frame.setBackground(Color.WHITE);    //设置当前窗口的背景
		frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tree.png")));  //设置当前窗口的标题图标

		JScrollPane jScrollPane = new JScrollPane(); //Javax.swing.JScrollPane类 构造方法 ：创建一个滚动条对象
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  //设置水平滚动条何时显示在窗口上
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);      //设置垂直滚动条何时显示在窗口上
		jScrollPane.setPreferredSize(new Dimension(600, 500));   //设置滚动条最好的大小（更具界面整体变化）
		
		jTextArea = new TextAreaMenu();    //自定义类的实例jTextArea ——JTextArea类的继承类（多行文本容器）
		jTextArea.setToolTipText("");   //设置提示信息（鼠标停留时）
		jTextArea.setFont(new Font("宋体", Font.PLAIN, 20));  //设置字体
		jTextArea.setLineWrap(true);  //设置当行过长时是否自动换行
		
		jScrollPane.setViewportView(jTextArea);   //滚动条 创建一个视口(如果有必要)并设置其视图
		frame.add(jScrollPane); //当前窗口对象添加滚动条

		frame.setVisible(true);    //设置窗口为可见
	}

	public void setPanel(String result) {   
		jTextArea.setText(result);
		
	}

	public Dimension getScreenSize(double scaleY, double scaleX) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		DisplayMode dm = gs[0].getDisplayMode();
		return new Dimension((int) (dm.getWidth() * scaleX), (int) (dm.getHeight() * scaleY));
	}

}