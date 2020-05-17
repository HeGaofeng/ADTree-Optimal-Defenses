/**
 * Author: Piotr Kordy (piotr.kordy@uni.lu <mailto:piotr.kordy@uni.lu>)
 * Date:   06/06/2013
 * Copyright (c) 2013,2012 University of Luxembourg -- Faculty of Science,
 *     Technology and Communication FSTC
 * All rights reserved.
 * Licensed under GNU Affero General Public License 3.0;
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package lu.uni.adtool.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import cutset.setresult.ResultRelation;
import cutset.settool.Test;
import cutset.tree.ATNode;
import cutset.tree.ComputeSet;
import cutset.ui.JTableTest;
import cutset.ui.SetWindow;
import lu.uni.adtool.Choices;
import lu.uni.adtool.adtree.ADTSerializer;
import lu.uni.adtool.adtree.ADTXmlImport;
import lu.uni.adtool.adtree.ADTreeForGui;
import lu.uni.adtool.adtree.ADTreeNode;
import lu.uni.adtool.domains.Domain;
import lu.uni.adtool.domains.DomainFactory;
import lu.uni.adtool.domains.ValuationDomain;
import lu.uni.adtool.domains.predefined.MinCost;
import lu.uni.adtool.domains.predefined.Parametrized;
import lu.uni.adtool.domains.rings.RealG0;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.ui.texts.ButtonTexts;
import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.FloatingWindow;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.ClassicDockingTheme;
import net.infonode.docking.theme.DefaultDockingTheme;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.GradientDockingTheme;
import net.infonode.docking.theme.LookAndFeelDockingTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.theme.SlimFlatDockingTheme;
import net.infonode.docking.theme.SoftBlueIceDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.PropertiesUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import cutset.ui.JTableTest;

public class MainWindow extends Frame {
	private MouseHandler mouseHandler;
	private static ADAction fileExportToPdf;
	private static ADAction fileExportToPng;
	private static ADAction fileExportToXml;
	private static ADAction fileImportFromXml;
	private static ADAction fileExit;
	private StatusLine status;
	private HashMap<Integer, ValuationDomain<Ring>> valuations = new HashMap<Integer, ValuationDomain<Ring>>();
	private FileHandler fh;
	private RootWindow rootWindow;
	private JFrame frame = new JFrame("攻击防御树安全效益评估工具");    //主界面标题
	private View[] views = new View[4];
	
	private HashMap<String, ATNode> labels = new HashMap<>();   //
	private HashMap<String,Ring>  denfCost=new HashMap<>();
	private HashMap<String,Double>  minCost=new HashMap<>();
	private String minvalues;
	/**
	 * Contains the dynamic views that has been added to the root window.
	 */
	private HashMap<Integer, DynamicView> dynamicViews = new HashMap<Integer, DynamicView>();

	/**
	 * Contains all the static views.
	 */
	private ViewMap viewMap = new ViewMap();

	/**
	 * The windows menu items.
	 */
	private JMenuItem[] windowsItems = new JMenuItem[views.length];
	/**
	 * The Attribute Domains menu.
	 */
	private JMenu attrDomainsMenu;
	/**
	 * The currently applied docking windows theme
	 */
	private DockingWindowsTheme currentTheme = new LookAndFeelDockingTheme();
	private ADTreeCanvas lastFocused;
	/**
	 * In this properties object the modified property values for close buttons
	 * etc. are stored. This object is cleared when the theme is changed.
	 */
	private RootWindowProperties properties = new RootWindowProperties();

	public ValuationDomain<Ring> getValuation(int id) {
		return valuations.get(new Integer(id));
	}

	/**
	 * Return list of DomainValuations used in main window
	 *
	 */
	public HashMap<Integer, ValuationDomain<Ring>> getValuations() {
		return valuations;
	}

	/**
	 * Sets the lastFocused for this instance.
	 *
	 * @param lastFocused
	 *            The lastFocused.
	 */
	public void setLastFocused(ADTreeCanvas lastFocused) {
		this.lastFocused = lastFocused;
		if (lastFocused == null || lastFocused instanceof DomainCanvas) {
			((ValuationView) views[2].getComponent()).assignCanvas(lastFocused);
			((DetailsView) views[3].getComponent()).assignCanvas(lastFocused);
		}
	}

	@SuppressWarnings("rawtypes")
	public void removeDomain(int i) {
		DynamicView view = dynamicViews.get(new Integer(i));
		Component c = view.getComponent();
		if (c instanceof DomainView) {
			if (lastFocused instanceof DomainCanvas && ((DomainCanvas) lastFocused).getId() == i) {
				setLastFocused(null);
			}
			((DomainView<?>) c).onClose();
			view.close();
			getRootWindow().removeView(view);
		}
		valuations.remove(new Integer(i));
		dynamicViews.remove(new Integer(i));
		createAttrDomainMenu();
	}

	public void removeDomains() {
		if (lastFocused instanceof DomainCanvas) {
			setLastFocused(null);
		}
		Collection<DynamicView> dynViews = dynamicViews.values();
		Vector<DynamicView> set = new Vector<DynamicView>(dynViews);
		for (DynamicView view : set) {
			Component c = view.getComponent();
			if (c instanceof DomainView) {
				((DomainView<?>) c).onClose();
				view.close();
				getRootWindow().removeView(view);
			}
		}
		valuations.clear();
		dynamicViews.clear();
		createAttrDomainMenu();
	}

	/**
	 * Gets the views for this instance.
	 *
	 * @return The views.
	 */
	public View[] getViews() {
		return this.views;
	}

	/**
	 * Gets the root window for this instance.
	 *
	 * @return The rootWindow
	 */
	public RootWindow getRootWindow() {
		return this.rootWindow;
	}

	/**
	 * @return JLabel that holds the text of a status bar.
	 */
	public StatusLine getStatusBar() {
		return status;
	}

	/**
	 * Gets the views for this instance.
	 *
	 * @param index
	 *            The index to get.
	 * @return The views.
	 */
	public View getViews(int index) {
		return this.views[index];
	}

	/**
	 * Gets the lastFocused for this instance.
	 *
	 * @return The lastFocused.
	 */
	public ADTreeCanvas getLastFocused() {
		return this.lastFocused;
	}

	/**
	 * A dynamically created view containing an id.
	 */
	public static class DynamicView extends View {
		static final long serialVersionUID = 4127190623311867764L;
		private int id;

		DynamicView(String title, Icon icon, Component component, int id) {
			super(title, icon, component);
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	@SuppressWarnings("rawtypes")
	public void updateDynamicViewTitles() {
		Collection<DynamicView> dynViews = dynamicViews.values();
		Vector<DynamicView> set = new Vector<DynamicView>(dynViews);
		int i = 0;
		for (DynamicView view : set) {
			i++;
			DomainView dv = (DomainView) view.getComponent();
			view.getViewProperties().setTitle(i + ". " + (dv.getCanvas().getDomain().getName()));
		}
	}

	public MainWindow(final String args[]) {
		createActions();
		createRootWindow();
		restoreDefaultLayout();
		showFrame();
	}

	public MainWindow() {
		// TODO Auto-generated constructor stub
	}

	private StatusLine createStatusBar() {
		StatusLine newStatus = new StatusLine();
		newStatus.setBorder(BorderFactory.createEtchedBorder());
		return newStatus;
	}

	private void updateFloatingWindow(FloatingWindow fw) {

	}

	private int getDynamicViewId() {
		int id = 0;

		while (dynamicViews.containsKey(new Integer(id)))
			id++;

		return id;
	}

	private void createRootWindow() {
		ADTreeForGui tree = new ADTreeForGui(new ADTreeNode());
		ADTreeView treeView = new ADTreeView(tree, this);
		views[0] = new View("查看攻击树", new ImageIcon(this.getClass().getResource("/icons/trees.png")), treeView);
		treeView.getCanvas().setClear(0);
		ADTreeCanvas canvas = ((ADTreeView) views[0].getComponent()).getCanvas();
		views[1] = new View("ADTerm Edit", new ImageIcon(this.getClass().getResource("/icons/viewTree_16x16.png")),
				new ADTermView(canvas));
		views[2] = new View("查看详细值", new ImageIcon(this.getClass().getResource("/icons/tables.png")),
				new ValuationView());
		views[3] = new View("查看计算方法", new ImageIcon(this.getClass().getResource("/icons/eyes.png")), new DetailsView());
		for (int i = 0; i < views.length; i++) {
			viewMap.addView(i, views[i]);
		}
		MixedViewHandler handler = new MixedViewHandler(viewMap, new ViewSerializer() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void writeView(View view, ObjectOutputStream out) throws IOException {
				int id = ((DynamicView) view).getId();
				out.writeInt(id);
				if (view.getComponent() instanceof DomainView) {
					Domain d = ((DomainCanvas) ((DomainView) view.getComponent()).getCanvas()).getDomain();
					out.writeObject(DomainFactory.updateDomainName(DomainFactory.getClassName(d)));
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public View readView(ObjectInputStream in) throws IOException {
				int id = in.readInt();
				try {
					if (Choices.currentSaveVer == 1) {
						String domainName = DomainFactory.updateDomainName((String) in.readObject());
						ValuationDomain<Ring> vd = new ValuationDomain(DomainFactory.createFromString(domainName));
						valuations.put(new Integer(id), vd);
						View view = getDynamicView(id);
						return view;
					} else {
						if (Choices.currentSaveVer == -1) {
							if (valuations.get(new Integer(id)) != null) {
								View view = getDynamicView(id);
								return view;
							}
						}
					}
				} catch (ClassNotFoundException e) {
					return null;
				}
				return null;
			}
		});
		rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);
		properties.addSuperObject(currentTheme.getRootWindowProperties());
		rootWindow.getRootWindowProperties().addSuperObject(properties);
		rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
		rootWindow.addListener(new DockingWindowAdapter() {
			public void windowAdded(DockingWindow addedToWindow, DockingWindow addedWindow) {
				if (addedWindow instanceof FloatingWindow) {
					updateFloatingWindow((FloatingWindow) addedWindow);
				} else {
					updateWindowsMenu();
				}
			}

			public void windowRemoved(DockingWindow removedFromWindow, DockingWindow removedWindow) {
				updateWindowsMenu();
			}

			public void windowClosing(DockingWindow window) throws OperationAbortedException {

			}

			public void windowUndocking(DockingWindow window) throws OperationAbortedException {

			}

			@SuppressWarnings("rawtypes")
			public void viewFocusChanged(View previouslyFocusedView, View focusedView) {
				if (focusedView != null && focusedView.getComponent() != null) {
					Component c = focusedView.getComponent();
					if (c instanceof ADTreeView) {
						setLastFocused(((ADTreeView) c).getCanvas());
					} else if (c instanceof DomainView) {
						setLastFocused(((DomainView) c).getCanvas());
					} else if (!(c instanceof ValuationView || c instanceof DetailsView)) {
						setLastFocused(null);
					}
				} else if (focusedView != null) {
					setLastFocused(null);
				}
			}
		});
		rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
	}

	private void createActions() {
		fileExportToPdf = new ADAction("PDF") {
			private static final long serialVersionUID = 4325025687838671271L;

			public void actionPerformed(final ActionEvent e) {
				exportTo("pdf");
			}
		};
		fileExportToPdf.setMnemonic(KeyEvent.VK_P);
		fileExportToPdf.setSmallIcon(new ImageIcon(getClass().getResource("/icons/pdf.png")));
		fileExportToPng = new ADAction("PNG image") {
			public void actionPerformed(final ActionEvent e) {
				exportTo("png");
			}
		};
		fileExportToPng.setSmallIcon(new ImageIcon(getClass().getResource("/icons/png.png")));
		fileExportToPng.setMnemonic(KeyEvent.VK_N);

		fileExportToXml = new ADAction("XML") {
			private static final long serialVersionUID = 2724287904022882599L;

			public void actionPerformed(final ActionEvent e) {
				exportTo("xml");
			}
		};
		fileExportToXml.setSmallIcon(new ImageIcon(getClass().getResource("/icons/xml.png")));
		fileExportToXml.setMnemonic(KeyEvent.VK_X);

		fileImportFromXml = new ADAction("XML") {
			private static final long serialVersionUID = -3605440604743377670L;

			public void actionPerformed(final ActionEvent e) {
				importFrom("xml");
				((ADTreeView) views[0].getComponent()).getCanvas().setClear(1);
			}
		};
		fileImportFromXml.setSmallIcon(new ImageIcon(getClass().getResource("/icons/xml.png")));
		fileImportFromXml.setMnemonic(KeyEvent.VK_X);
		fileExit = new ADAction(ButtonTexts.EXIT) {
			private static final long serialVersionUID = -6586817922511469697L;

			public void actionPerformed(final ActionEvent e) {
				WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				frame.dispatchEvent(windowClosing);
			}
		};
		fileExit.setMnemonic(KeyEvent.VK_X);
		fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		fileExit.setSmallIcon(new ImageIcon(getClass().getResource("/icons/exit.png")));
	}

	private void importFrom(String type) {
		FileInputStream in = fh.getImportTreeStream(type);
		if (in != null) {
			ADTXmlImport importer = new ADTXmlImport(this);
			importer.importFrom(in);
		}
	}

	private void exportTo(String type) {
		ADTreeCanvas canvas = ((ADTreeView) views[0].getComponent()).getCanvas();
		if (lastFocused != null) {
			canvas = lastFocused;
		}
		ADTreeNode tempFocus = canvas.getFocused();
		canvas.setFocus(null);
		FileOutputStream out = fh.getExportTreeStream(type);
		if (out != null) {
			if (type.equals("pdf")) {
				canvas.createPdf(out);
			} else if (type.equals("png")) {
				canvas.createImage(out, type);
			} else if (type.equals("xml")) {
				canvas.createXml(out);
			}
		}
		canvas.setFocus(tempFocus);
	}

	private void restoreDefaultLayout() {
		TabWindow tabWindow = new TabWindow(new DockingWindow[] { views[0] });
		tabWindow.add(views[0]);
		rootWindow.setWindow(tabWindow);
		for (View v : dynamicViews.values()) {
			addDomainWindow(v);
		}
	}

	private void showFrame() {
		status = createStatusBar();
		if (isRunningJavaWebStart()) {
			fh = new JWSFileHandler(status, this);
		} else {
			fh = new FileHandler(status, this);
		}
		mouseHandler = new MouseHandler();
		frame.getContentPane().add(getRootWindow(), BorderLayout.CENTER);
		frame.getContentPane().add(status, BorderLayout.SOUTH);
		frame.setJMenuBar(createMenuBar());
		Dimension dim = getScreenSize(1, 1);
		frame.setSize(dim.width, dim.height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tree.png")));
		frame.addWindowListener(new WindowAdapter() {
			public final void windowClosing(final WindowEvent e) {
				final JFrame localFrame = (JFrame) e.getSource();

				final int result = JOptionPane.showConfirmDialog(localFrame, "确定要退出应用吗?", "退出应用",
						JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		});
		frame.setVisible(true);
	}

	// Creates the frame menu bar.
	private JMenuBar createMenuBar() {
		final JMenuBar menu = new JMenuBar();
		menu.add(createFileMenu());
		attrDomainsMenu = new JMenu("查看与分析");       //添加栏目
		//attrDomainsMenu.setMnemonic(KeyEvent.VK_D);
		menu.add(attrDomainsMenu);
		
		createAttrDomainMenu();
		
		return menu;
	}
	
	// Create Attribute Domains menu
	public void createAttrDomainMenu() {
		JMenu menu = attrDomainsMenu;
		menu.removeAll();
		
		
		JMenuItem Item1=new JMenuItem("查看安全属性"); //查看安全属性
		Item1.setIcon(new ImageIcon(getClass().getResource("/icons/attr.png")));    //菜单项设置图标
		Item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFrame frame=new JFrame("安全属性量化指标");
				frame.setBounds(200, 200, 700, 540);// 窗体大小
				frame.setDefaultCloseOperation(2);
				frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tables.png")));  //设置当前窗口的标题图标
				//Container c = frame.getContentPane();
				
				/////////////////////
				String[] columnNames = { "防御措施", "防御代价", "防御成功概率"};// 定义表格列
				String[][] tableValues = new String[50][3];// 定义数组，用来存储表格数据
				/*
				for (int row = 0; row < tableValues.length; row++) {
					for (int column = 0; column < columnNames.length; column++) {
						tableValues[row][column] = columnNames[column] + row;// 给表格赋值
					}
				}
				*/
				ATNode root=new ATNode();
				root=createATree();      //建立与XML的连接
				Test test=new Test();    //自定义类型Test
				test.setRoot(root);     
				tableValues=test.getTableValues(root);
				
				
				/////////////////////////
				JTable table = new JTable(tableValues, columnNames);
				
				JScrollPane sc = new JScrollPane(table);
				//c.add(table, BorderLayout.CENTER);

				table.setSelectionForeground(Color.RED);// 设置前景色，字体颜色
				//table.setBackground(Color.PINK);
				table.setSelectionBackground(Color.yellow);// 背景色
				table.setRowHeight(30);// 设置行高30像素
				

				//System.out.println("表格共有" + table.getRowCount() + "行" + table.getColumnCount() + "列");
				
				frame.add(sc,BorderLayout.CENTER);
				frame.setVisible(true);
		    	
		    	//new JTableTest().initUI();
			}
		});
		menu.add(Item1);
		
		JMenuItem cutset=new JMenuItem("安全效益评估结果"); //javax.swing.JMenuItem构造方法：设置菜单项对象
		cutset.setIcon(new ImageIcon(getClass().getResource("/icons/compute.png")));    //菜单项设置图标
		
		cutset.addActionListener(new ActionListener() {        //为菜单项添加事件监听器
			public void actionPerformed(ActionEvent e) {
			
				//ComputeSet computeSet=new ComputeSet(); 
				
				ATNode root=new ATNode();
				root=createATree();      //建立与XML的连接
				Test test=new Test();    //自定义类型Test
				test.setRoot(root);     
				
				long starttime=System.currentTimeMillis();
				
				ATNode newRoot=new ATNode();
				newRoot.setLabel("n_"+ATNode.getNums());  //设置节点的标签为 “n_当前节点的总数”
				newRoot.getChildren().add(root);    //设置原root为当前newRoot节点的孩子节点
				
				test.equalTree(newRoot);   //等价转换（攻击防御树到原子攻击防御树）
				test.getDefCost(newRoot);  //获取防御代价（存储在hashmap Defcost中）
				
				test.removeNnode(newRoot);    //消除newRoot子节点中的n_节点
				
			    //System.out.println(newRoot.getLogExp());     //逻辑表达式
			    
			    //System.out.println(test.expand(test.getLogExp(newRoot)));   //展开的逻辑表达式
			    
				test.atomtree(test.getLogExp(newRoot));
				
				//long endtime=System.currentTimeMillis();
				String rString=test.showatomtree();    //
				//System.out.println(rString);
				//System.out.println("原子树求割集："+(endtime-starttime)+"ms");
				
				

		        String[] numStrings= {"ma","mb","mc","md","me","af","ag","ah","mi","aj","ak","al","am","an","ao","ap","aq"};
		        String[] reStrings= {"m1","m2","m3","m4","m5","a1","a2","a3","m6","a4","a5","a6","a7","a8","a9","a10","a11"};
		        for(int i=0;i<numStrings.length;i++) {
		        	rString=rString.replaceAll(numStrings[i], reStrings[i]);
		        }
		        
		    	//SetWindow setwindow=new SetWindow();   //弹出结果窗口
		    	//setwindow.initUI();     //初始化结果窗口
		    	//setwindow.setPanel(rString);   //显示结果
		    	
		    	
		    	HashMap<Integer, ResultRelation> necessary_defenseNodes_Map = test.getAllCombination(root);
		    	
		    	
				StringBuffer myStringBuffer = new StringBuffer();
				for(Map.Entry<Integer,ResultRelation> combination:necessary_defenseNodes_Map.entrySet()){
					
					//System.out.println(combination.getKey() + ":");
					
					for(int i=0;i<combination.getValue().getNecessary_defenseNodes().length;i++){
						System.out.print(combination.getValue().getNecessary_defenseNodes()[i]);
						
					}
					System.out.println("");
					
				
					ATNode myNewRoot = createATree();
			    	Test myNewTest=new Test();    
					myNewTest.setRoot(myNewRoot);
					myNewTest.simplify1(myNewRoot, combination.getValue().getNecessary_defenseNodes());
					myNewTest.simplify2(myNewRoot, combination.getValue().getNecessary_defenseNodes());
					myNewTest.simplify3(myNewRoot);
					myNewTest.jiance(myNewRoot);
					System.out.println(myNewRoot.getAttackSuccessProbability()+"");
					//myNewTest.sumProbability=1;
					double sumProbability = myNewTest.getSumProbability(myNewRoot);
					combination.getValue().setDefenseSuccessProbability(1-sumProbability);
					//myNewTest.sumProbability=1;
					//System.out.println("\n\n");
					//System.out.println(sumProbability);
					//myStringBuffer.append(combination.getValue().showResult());
					//setwindow.setPanel(combination.getValue().showResult());   
					//combination.getValue().showResult();
					
					
				
					
				}
				List<Map.Entry<Integer,ResultRelation>> list = new ArrayList<Map.Entry<Integer,ResultRelation>>(necessary_defenseNodes_Map.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<Integer,ResultRelation>>() {
					// 降序排序
					public int compare(Entry<Integer,ResultRelation> o1, Entry<Integer,ResultRelation> o2) {
						return o2.getValue().getDefenseSuccessProbability().compareTo(o1.getValue().getDefenseSuccessProbability());
					}

				});
				
				
				/*
				int myCount=0;
				for(Map.Entry<Integer,ResultRelation> combination : list){
					myCount++;
					myStringBuffer.append(myCount+"：\t");
					myStringBuffer.append(combination.getValue().showResult());     //待定
				}
				
				//myNewTest.simplify(myNewRoot, necessary_defenseNodes);
				//test.getAttackSuccessProbability(myNewRoot);
				
				setwindow.setPanel(myStringBuffer.toString()); 
				*/
				//////////////////////////////////////////////////////////////////////////////////
				JFrame frame=new JFrame("安全效益评估结果");
				frame.setBounds(200, 200, 700, 540);// 窗体大小
				frame.setDefaultCloseOperation(2);
				frame.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/icons/tree.png")));  //设置当前窗口的标题图标
				//Container c = frame.getContentPane();
				
				/////////////////////
				String[] columnNames = { "编号", "必要防御节点的集合", "系统整体防御代价","系统最终目标防御成功概率"};// 定义表格列
				String[][] tableValues = new String[200][4];// 定义数组，用来存储表格数据
				/*
				for (int row = 0; row < tableValues.length; row++) {
					for (int column = 0; column < columnNames.length; column++) {
						tableValues[row][column] = columnNames[column] + row;// 给表格赋值
					}
				}
				*/
				/////////////////////////////////////
				
				int myCount=0;
				for(Map.Entry<Integer,ResultRelation> combination : list){
					myCount++;
					tableValues[myCount-1][0]=myCount+"";
					StringBuffer myBuffer=new StringBuffer();
					for(int i=0;i<combination.getValue().getNecessary_defenseNodes_length();i++){
						myBuffer.append(combination.getValue().getNecessary_defenseNodes()[i]+" ");
					}
					tableValues[myCount-1][1]=myBuffer.toString();
					tableValues[myCount-1][2]=combination.getValue().getDefenseCost()+"";
					tableValues[myCount-1][3]=String.format("%.3f",combination.getValue().getDefenseSuccessProbability());
				}
				
				
				/////////////////////////
				JTable table = new JTable(tableValues, columnNames);
				
				JScrollPane sc = new JScrollPane(table);
				//c.add(table, BorderLayout.CENTER);

				table.setSelectionForeground(Color.RED);// 设置前景色，字体颜色
				//table.setBackground(Color.PINK);
				table.setSelectionBackground(Color.yellow);// 背景色
				table.setRowHeight(30);// 设置行高30像素
				

				//System.out.println("表格共有" + table.getRowCount() + "行" + table.getColumnCount() + "列");
				
				frame.add(sc,BorderLayout.CENTER);
				frame.setVisible(true);
		    	
		    	//new JTableTest().initUI();
		    	
		    	
			}
		});
		menu.add(cutset);
	}

	public void addDomainWindow(DockingWindow window) {
		TabWindow tabWindow = new TabWindow(new DockingWindow[] { window });
		tabWindow.add(window);
		rootWindow.setWindow(tabWindow);
	}


	public HashMap<Integer, DynamicView> getDynamicViews() {
		return dynamicViews;
	}

	
	public Dimension getScreenSize(double scaleY, double scaleX) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		DisplayMode dm = gs[0].getDisplayMode();
		return new Dimension((int) (dm.getWidth() * scaleX), (int) (dm.getHeight() * scaleY));
	}

	/**
	 * Adds new attribute domain.
	 *
	 * @param d
	 *            attribute domain to be added.
	 */
	@SuppressWarnings("rawtypes")
	public View addAttributeDomain(Domain<?> d) {
		@SuppressWarnings("unchecked")
		ValuationDomain<Ring> vd = new ValuationDomain(d);
		int id = getDynamicViewId();
		valuations.put(new Integer(id), vd);
		View view = getDynamicView(id);
		addDomainWindow(view);
		setLastFocused(((DomainView) view.getComponent()).getCanvas());
		status.report("Added a new domain: " + d.getName());
		return view;
	}

	public void closeDynamicViews() {
		for (View v : dynamicViews.values()) {
			v.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public View getDynamicView(int id) {
		View view = dynamicViews.get(new Integer(id));
		if (view == null) {
			if (getValuation(id) != null) {
				ADTreeForGui tree = ((ADTreeView) views[0].getComponent()).getTree();
				DomainCanvas canvas = new DomainCanvas(tree, this, id);
				DomainView dv = new DomainView(this, canvas, id);
				view = new DynamicView((1 + id) + ". " + canvas.getDomain().getName(), dv.getIcon(), dv, id);
				dynamicViews.put(new Integer(id), (DynamicView) view);
			} else {
				status.reportError("Dynamic View with id " + id + " has no associated valuations");
				return null;
			}
		} else {
		}
		return view;
	}

	private void updateWindowsMenu() {
		for (int i = 0; i < windowsItems.length; i++) {
			if (windowsItems[i] != null) {
				windowsItems[i].setEnabled(views[i].getRootWindow() == null);
			}
		}
	}

	private JMenu createFileMenu() {
		JMenuItem menuItem;
		final JMenu fileMenu = new JMenu();
		fileMenu.setText(ButtonTexts.FILE);
		fileMenu.setMnemonic('F');

		JMenu importFrom = new JMenu("打开");
		importFrom.setIcon(new ImageIcon(getClass().getResource("/icons/open.png")));
		importFrom.setMnemonic(KeyEvent.VK_I);
		menuItem = importFrom.add(fileImportFromXml);
		menuItem.addMouseListener(mouseHandler);
		importFrom.add(menuItem);
		fileMenu.add(importFrom);

		JMenu exportTo = new JMenu("保存");
		exportTo.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
		exportTo.setMnemonic(KeyEvent.VK_E);
		menuItem = exportTo.add(fileExportToPdf);
		menuItem.addMouseListener(mouseHandler);
		exportTo.add(menuItem);
		menuItem = exportTo.add(fileExportToPng);
		menuItem.addMouseListener(mouseHandler);
		exportTo.add(menuItem);
		menuItem = exportTo.add(fileExportToXml);
		menuItem.addMouseListener(mouseHandler);
		exportTo.add(menuItem);
		fileMenu.add(exportTo);

		menuItem = fileMenu.add(fileExit);
		menuItem.addMouseListener(mouseHandler);
		fileMenu.add(menuItem);
		return fileMenu;
	}

	/**
	 * ADAction represents an action that is used in application.
	 */
	public abstract class ADAction extends AbstractAction {

		private static final long serialVersionUID = 8109471079193338016L;

		public ADAction(final String text) {
			super(text);
		}

		public final void setAccelerator(final KeyStroke accelerator) {
			putValue(ACCELERATOR_KEY, accelerator);
		}

		public final void setSmallIcon(final Icon icon) {
			putValue(SMALL_ICON, icon);
		}

		public final void setToolTip(final String text) {
			putValue(SHORT_DESCRIPTION, text);
		}

		public final void setDescription(final String text) {
			putValue(LONG_DESCRIPTION, text);
		}

		public final void setMnemonic(final Integer mnemonic) {
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public abstract void actionPerformed(final ActionEvent e);
	}

	public JFrame getFrame() {
		return frame;
	}

	/**
	 * This adapter is constructed to handle mouse over component events.
	 */
	private class MouseHandler extends MouseAdapter {
		public MouseHandler() {
		}

		public void mouseEntered(MouseEvent evt) {
		}
	}

	private boolean isRunningJavaWebStart() {
		return System.getProperty("javawebstart.version", null) != null;
	}
	
	
	
	
	public ATNode reverseToATree(ADTreeForGui tree,ADTreeNode root,ATNode newroot){	
        newroot.setADTreeNode(root);   //ATNode类型的setADTreeNode方法
        String key = UUID.randomUUID().toString();    //随机一个节点的String类型序号标识
		labels.put(key, newroot);    //private HashMap<String, ATNode> labels = new HashMap<>(); 

        List<ADTreeNode> children=tree.getChildrenList(root);    //获取当前ADTreeNode节点的孩子节点集合
        
    	boolean flag=false;
    	if(children.isEmpty()) {
    		newroot.setMincost(Double.POSITIVE_INFINITY);
    		denfCost.put(newroot.getLabel().toLowerCase(),new RealG0());  //private HashMap<String,Ring>  denfCost=new HashMap<>();
    	}
    	
        for(ADTreeNode child:children){
        	if(!flag) {
        		newroot.setMincost(Double.POSITIVE_INFINITY);
        		denfCost.put(newroot.getLabel().toLowerCase(),new RealG0());
        	}
        	
        	if(child.getType()==ADTreeNode.Type.OPPONENT){     //如果孩子节点是ATTACKER
        		flag=true;     
        		newroot.setMincost(((RealG0)this.getValuation(0).getValue(child)).getValue());    //private HashMap<Integer, ValuationDomain<Ring>> valuations = new HashMap<Integer, ValuationDomain<Ring>>(); 
        		newroot.setAttackSuccessProbability(1-(this.getValuation(1).getValue(child)).getValue());   //(RealG0)this.getValuation(0).getValue(child)).getValue()
        		denfCost.put(newroot.getLabel().toLowerCase(),this.getValuation(0).getValue(child));
        		//System.out.println(newroot.getLabel()+" "+ newroot.getMincost() + " " + (1-newroot.getAttackSuccessProbability()));
    			 continue;
    		}
        	ATNode newchild=new ATNode();
        	newchild.setADTreeNode(child);
        	newroot.addChild(newchild);
        	newchild.setParent(newroot);
        	
        	
        	
        	reverseToATree(tree,child,newchild);
        }
        return newroot;
    }
	
	public ATNode createATree(){
		ADTreeCanvas canvas = ((ADTreeView) views[0].getComponent()).getCanvas();
		ADTreeForGui tree=canvas.getTree();
        ATNode newroot=new ATNode();
		return reverseToATree(tree,tree.getRoot(),newroot);
	}
}
