package cutset.tree;

import java.util.Iterator;
import java.util.Vector;

import lu.uni.adtool.adtree.ADTreeNode;
import lu.uni.adtool.adtree.ADTreeNode.Type;

public class ATNode {    //���������������ڵ�����
	private static int nums = 0;        //��̬��Ա�������������������������ڵ��������
	
	protected Vector<ATNode> children;   // ���ӽڵ�(ATNode������)
	protected ATNode parent;     // ���ڵ�   
	
	private String label;   // �ڵ�ı�ǩ 
	private RefinementType refinementType;   // ���ӷ�����
	private Type type;    //�ڵ�����

	private double mincost;     //��С��������
	private double attackSuccessProbability;   //�����ɹ�����

	public double getMincost() {               //��ȡ��С��������
		return mincost;
	}

	public void setMincost(double mincost) {      //������С��������
		this.mincost = mincost;
	}
	public double getAttackSuccessProbability() {               //��ȡ�����ɹ�����
		return attackSuccessProbability;
	}

	public void setAttackSuccessProbability(double attackSuccessProbability) {      //���ù����ɹ�����
		this.attackSuccessProbability = attackSuccessProbability;
	}

	public enum RefinementType {       //ö�ٵ�ϸ������ �߼���DISJUNCTIVE���͡��߼���
		DISJUNCTIVE, CONJUNCTIVE
	}
	public enum Type{ATTACKER, DEFENDER}

	/**
	 * Constructor.
	 * 
	 * @param id the id
	 */
	public ATNode() {          //���캯��
		nums++;
		this.mincost=Double.POSITIVE_INFINITY;
		this.attackSuccessProbability = 0.4;
		children = new Vector<ATNode>();
	}

	public static int getNums() {      //��ȡ��ǰ�ڵ������
		return nums;
	}

	public static void setNums(int nums) {  //���õ�ǰ�ڵ������
		ATNode.nums = nums;
	}

	public ATNode getParent() {  //��ȡ���ڵ�
		return parent;
	}

	public void setParent(ATNode parent) {     //���ø��ڵ�
		this.parent = parent;
	}

	/**
	 * Gets the children for this instance.
	 *
	 * @return The children.
	 */
	public Vector<ATNode> getChildren() {    //��ȡ���ӽڵ�
		return children;
	}

	public String getLabel() {    //��ȡ�ڵ�ı�ǩ
		return label;
	}

	public void setLabel(String label) {    //���ýڵ�ı�ǩ
		this.label = label;
	}

	public RefinementType getRefinementType() { //��ȡϸ������
		return refinementType;
	}

	public void setRefinementType(RefinementType refinementType) {   //����ϸ������
		this.refinementType = refinementType;
	}
	
	public final Type getType()
	{
		
	    return this.type;
	}
	
	public void setType(Type type) {   //����ϸ������
		this.type = type;
	}

	public void setChildren(Vector<ATNode> children) {     //���ú��ӽڵ�
		this.children = children;
	}

	public void addChild(ATNode n) {     //��Ӻ��ӽڵ�
		if (children == null) {
			children = new Vector<ATNode>();
		}
		children.add(n);
		n.setParent(this);
	}

	public final ATNode getChild(int i) {     //��ȡָ�������ĺ��ӽڵ�
		return children.elementAt(i);
	}

	public int getNumChildren() {     //��ȡ���ӽڵ������
		return (children == null) ? 0 : children.size();
	}

	// ����չ������߼����ʽ
	public String getExp() {
		return getExp1().replace(" ", "+");

	}

	public String getExp1() {

		if (!children.isEmpty()) {
			if (refinementType == RefinementType.CONJUNCTIVE) {  //�߼���
				Iterator<ATNode> it = children.iterator();   //�������Ժ��ӽڵ���е���
				ATNode node1 = null;
				ATNode node2 = null;
				String s1 = null;
				String s2 = null;
				String temp = null;
				while (it.hasNext()) {
					StringBuffer sb = new StringBuffer();
					if (node1 == null) {
						node1 = (ATNode) it.next();
						s1 = node1.getExp1();    //�ݹ�
						if (it.hasNext()) {
							node2 = (ATNode) it.next();
							s2 = node2.getExp1();  //�ݹ�
						}
					} else {
						node1 = node2;
						s1 = temp;
						node2 = (ATNode) it.next();
						s2 = node2.getExp1();   //�ݹ�
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
			} else {                //�߼���
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

	//��ȡ�߼����ʽ
	public String getLogExp() {
		StringBuffer sb = new StringBuffer();
		if (!this.children.isEmpty()) {
			Iterator<ATNode> it = children.iterator();
			sb.append("(");
			while (it.hasNext()) {
				ATNode node = (ATNode) it.next();
				sb.append(node.getLogExp());   //�ݹ�
				if (refinementType == RefinementType.CONJUNCTIVE) {
					sb.append("*");   //�߼���
				} else
					sb.append("+");   //�߼���
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
