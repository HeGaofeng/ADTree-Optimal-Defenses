package cutset.tree;

import java.util.Iterator;
import java.util.Vector;

import lu.uni.adtool.adtree.ADTreeNode;
import lu.uni.adtool.adtree.ADTreeNode.Type;

public class ATNode {    //攻击防御树攻击节点类型
	private static int nums = 0;        //静态成员变量——计数（攻击防御树节点的总数）
	
	protected Vector<ATNode> children;   // 孩子节点(ATNode型数组)
	protected ATNode parent;     // 父节点   
	
	private String label;   // 节点的标签 
	private RefinementType refinementType;   // 连接符类型
	private Type type;    //节点类型

	private double mincost;     //最小防御代价
	private double attackSuccessProbability;   //攻击成功概率

	public double getMincost() {               //获取最小防御代价
		return mincost;
	}

	public void setMincost(double mincost) {      //设置最小防御代价
		this.mincost = mincost;
	}
	public double getAttackSuccessProbability() {               //获取攻击成功概率
		return attackSuccessProbability;
	}

	public void setAttackSuccessProbability(double attackSuccessProbability) {      //设置攻击成功概率
		this.attackSuccessProbability = attackSuccessProbability;
	}

	public enum RefinementType {       //枚举的细化类型 逻辑或DISJUNCTIVE　和　逻辑与
		DISJUNCTIVE, CONJUNCTIVE
	}
	public enum Type{ATTACKER, DEFENDER}

	/**
	 * Constructor.
	 * 
	 * @param id the id
	 */
	public ATNode() {          //构造函数
		nums++;
		this.mincost=Double.POSITIVE_INFINITY;
		this.attackSuccessProbability = 0.4;
		children = new Vector<ATNode>();
	}

	public static int getNums() {      //获取当前节点的总数
		return nums;
	}

	public static void setNums(int nums) {  //设置当前节点的总数
		ATNode.nums = nums;
	}

	public ATNode getParent() {  //获取父节点
		return parent;
	}

	public void setParent(ATNode parent) {     //设置父节点
		this.parent = parent;
	}

	/**
	 * Gets the children for this instance.
	 *
	 * @return The children.
	 */
	public Vector<ATNode> getChildren() {    //获取孩子节点
		return children;
	}

	public String getLabel() {    //获取节点的标签
		return label;
	}

	public void setLabel(String label) {    //设置节点的标签
		this.label = label;
	}

	public RefinementType getRefinementType() { //获取细化类型
		return refinementType;
	}

	public void setRefinementType(RefinementType refinementType) {   //设置细化类型
		this.refinementType = refinementType;
	}
	
	public final Type getType()
	{
		
	    return this.type;
	}
	
	public void setType(Type type) {   //设置细化类型
		this.type = type;
	}

	public void setChildren(Vector<ATNode> children) {     //设置孩子节点
		this.children = children;
	}

	public void addChild(ATNode n) {     //添加孩子节点
		if (children == null) {
			children = new Vector<ATNode>();
		}
		children.add(n);
		n.setParent(this);
	}

	public final ATNode getChild(int i) {     //获取指定索引的孩子节点
		return children.elementAt(i);
	}

	public int getNumChildren() {     //获取孩子节点的数量
		return (children == null) ? 0 : children.size();
	}

	// 计算展开后的逻辑表达式
	public String getExp() {
		return getExp1().replace(" ", "+");

	}

	public String getExp1() {

		if (!children.isEmpty()) {
			if (refinementType == RefinementType.CONJUNCTIVE) {  //逻辑与
				Iterator<ATNode> it = children.iterator();   //迭代器对孩子节点进行迭代
				ATNode node1 = null;
				ATNode node2 = null;
				String s1 = null;
				String s2 = null;
				String temp = null;
				while (it.hasNext()) {
					StringBuffer sb = new StringBuffer();
					if (node1 == null) {
						node1 = (ATNode) it.next();
						s1 = node1.getExp1();    //递归
						if (it.hasNext()) {
							node2 = (ATNode) it.next();
							s2 = node2.getExp1();  //递归
						}
					} else {
						node1 = node2;
						s1 = temp;
						node2 = (ATNode) it.next();
						s2 = node2.getExp1();   //递归
					}
					
					String[] sa1 = s1.split(" ");
					if (!(s2 == null)) {
						String[] sa2 = s2.split(" ");
						for (int i = 0; i < sa1.length; i++) {
							for (int j = 0; j < sa2.length; j++) {
								sb.append(sa1[i] + "*" + sa2[j] + " ");
							}
						}
						temp = sb.substring(0, sb.length() - 1);
					} else
						temp = s1;
				}
				System.out.println(temp);
				return temp;
			} else {                //逻辑或
				Iterator<ATNode> it = children.iterator();
				StringBuffer sb = new StringBuffer();
				while (it.hasNext()) {
					ATNode node = (ATNode) it.next();
					sb.append(node.getExp1() + " ");
				}
				// System.out.println(sb.substring(0, sb.length() - 1));
				return sb.substring(0, sb.length() - 1);
			}
		}
		// System.out.println(this.label);
		return this.label;
	}

	//获取逻辑表达式
	public String getLogExp() {
		StringBuffer sb = new StringBuffer();
		if (!this.children.isEmpty()) {
			Iterator<ATNode> it = children.iterator();
			sb.append("(");
			while (it.hasNext()) {
				ATNode node = (ATNode) it.next();
				sb.append(node.getLogExp());   //递归
				if (refinementType == RefinementType.CONJUNCTIVE) {
					sb.append("*");   //逻辑与
				} else
					sb.append("+");   //逻辑或
			}
			String s = sb.substring(0, sb.length() - 1) + ")";
			// System.out.println(s);
			return s;
		} else {
			// System.out.println(this.label);
			return this.label;
		}
	}

	public String getExp3() {
		return getExp2().replace(" ", "+");

	}

	public String getExp2() {

		if (!children.isEmpty()) {
			if (refinementType == RefinementType.DISJUNCTIVE) {
				Iterator<ATNode> it = children.iterator();
				ATNode node1 = null;
				ATNode node2 = null;
				String s1 = null;
				String s2 = null;
				String temp = null;
				while (it.hasNext()) {
					StringBuffer sb = new StringBuffer();
					if (node1 == null) {
						node1 = (ATNode) it.next();
						s1 = node1.getExp2();
						if (it.hasNext()) {
							node2 = (ATNode) it.next();
							s2 = node2.getExp2();
						}
					} else {
						node1 = node2;
						s1 = temp;
						node2 = (ATNode) it.next();
						s2 = node2.getExp2();
					}
					String[] sa1 = s1.split(" ");
					if (!(s2 == null)) {
						String[] sa2 = s2.split(" ");
						for (int i = 0; i < sa1.length; i++) {
							for (int j = 0; j < sa2.length; j++) {
								sb.append(sa1[i] + "*" + sa2[j] + " ");
							}
						}
						temp = sb.substring(0, sb.length() - 1);
					} else
						temp = s1;
				}
				return temp;
			} else {
				Iterator<ATNode> it = children.iterator();
				StringBuffer sb = new StringBuffer();
				while (it.hasNext()) {
					ATNode node = (ATNode) it.next();
					sb.append(node.getExp2() + " ");
				}
				return sb.substring(0, sb.length() - 1);
			}
		}
		return this.label;
	}

	public void setADTreeNode(ADTreeNode root) {
		this.setLabel(root.getLabel());
		this.setRefinementType(root.getRefinmentType() == ADTreeNode.RefinementType.CONJUNCTIVE ? RefinementType.CONJUNCTIVE : RefinementType.DISJUNCTIVE);

	}
}
