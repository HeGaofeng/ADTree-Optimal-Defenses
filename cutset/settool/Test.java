package cutset.settool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import com.itextpdf.text.io.TempFileCache;
import com.itextpdf.text.pdf.PdfPublicKeyRecipient;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import cutset.setresult.ResultRelation;
import cutset.tree.ATNode;
import cutset.tree.ATNode.RefinementType;

public class Test {
	private ATNode root;
	private ExprEvaluator util = new ExprEvaluator();

	public ATNode getRoot() {      //��ȡ���ڵ�root
		return root;
	}

	public void setRoot(ATNode root) {   //���ø��ڵ�Ϊroot
		this.root = root;
	}

	private HashMap<String, Double> map = new HashMap<String, Double>();
	private HashMap<String, Double> atommap = new HashMap<String, Double>();   //���һ��
	private HashMap<String, Double> defcost = new HashMap<String, Double>();  //��ȡ��������Hashmap��ֵ�Դ洢
	private Vector<HashSet<ATNode>> allnodes = new Vector<HashSet<ATNode>>();
	private Vector<Vector<ATNode>> btnodes = new Vector<Vector<ATNode>>();
	private Vector<String> ss = new Vector<String>();
	private Vector<String> bt = new Vector<String>();
	private Vector<String> at = new Vector<String>();
	
	private String result="";

	
	public double mincost(ATNode root) {  //��ǰ�ڵ�root
		double mincost = 0;
		Vector<ATNode> children = root.getChildren();   //��ȡ��ǰ�ڵ�root�ĺ��ӽڵ�  ATNode������
		Vector<ATNode> result = new Vector<ATNode>();   //ATNode������ result
		if (children.isEmpty()) {             //�жϺ����Ƿ�Ϊ��
			return root.getMincost();
		} else {
			if (root.getRefinementType() == RefinementType.CONJUNCTIVE) {  //�߼���
				double re = Double.MAX_VALUE;
				for (ATNode child : children) {
					double temp = mincost(child);   //�ݹ�
					if (temp < re) {
						re = temp;       //re���ǻ�ȡ��С���۵ĺ��ӽڵ����С��������
					}
				}
				mincost += re;   //
			} else {     //�߼���
				for (ATNode child : children) {
					mincost += mincost(child);   //
				}
			}
		}

		return mincost;

	}
	
	//��ȡ��������
	public void getDefCost(ATNode root) {
		if (root == null)
			return;
		defcost.put(root.getLabel().toLowerCase(), root.getMincost());  //hashmap��ֵ�ԡ��ڵ��ǩ���������ۡ���
		// System.out.println(root.getLabel().toLowerCase()+defcost.get(root.getLabel().toLowerCase()));
		Vector<ATNode> children = root.getChildren();
		for (ATNode child : children) {
			getDefCost(child);    
		}
	}
	
    
	int i=0;
	
	public void getExp(String exp, HashSet<ATNode> nodes) {
//		if(!at.contains(exp)) {
//			i++;
//			System.out.println(i+exp);
//		}
		if (allnodes.contains(nodes)||at.contains(exp))
			return;
		String temp = "";
		try {
			ExprEvaluator util = new ExprEvaluator();
			IExpr expr = util.eval(exp);
			IExpr result = util.eval(F.Expand(expr));
			temp = result.toString();
		} catch (SyntaxError e) {
			// catch Symja parser errors here
			System.out.println(e.getMessage());
		} catch (MathException me) {
			// catch Symja math errors here
			System.out.println(me.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (final StackOverflowError soe) {
			System.out.println(soe.getMessage());
		} catch (final OutOfMemoryError oome) {
			System.out.println(oome.getMessage());
		}
		if (bt.contains(temp))
			return;
		String[] xq = temp.split("\\+");
		for (String x : xq) {
			String ts = x.replace("*", " ,");
			ts = "{" + ts + "}";
			if (ss.contains(ts) || ts.indexOf('(') != -1) {
				continue;
			}
			ss.add(ts);
			String[] hq = x.split("\\*");
			double re = 0;
			for (String y : hq) {
				re = re + defcost.get(y.toLowerCase()).doubleValue();
				if (re == Double.POSITIVE_INFINITY)
					break;
			}
			if (re != Double.POSITIVE_INFINITY) {
				map.put(ts, re);
			}
		}
		for (ATNode node : nodes) {
			if (node.getChildren().isEmpty()) {
				continue;
			} else {
				String s1 = "";
				HashSet<ATNode> newnodes = new HashSet<ATNode>(nodes);
				newnodes.remove(node);
				if (node.getRefinementType() == RefinementType.CONJUNCTIVE) {
					for (ATNode child : node.getChildren()) {
						s1 += "+" + child.getLabel();
						newnodes.add(child);
					}
				} else {
					for (ATNode child : node.getChildren()) {
						s1 += "*" + child.getLabel();
						newnodes.add(child);
					}
				}
				s1 = s1.substring(1);
				s1 = "(" + s1 + ")";
				String newexp = exp.replaceAll(node.getLabel(), s1);
				getExp(newexp, newnodes);
				allnodes.add(newnodes);
			}
		}
		bt.add(temp);
		at.add(exp);
	}

	
	public String getLogExp(ATNode root) {
		Vector<ATNode> children=root.getChildren();
		String resultString="";
		if (!children.isEmpty()) {
			String temp="";
			if(root.getRefinementType()==RefinementType.CONJUNCTIVE) {		//�߼���
				for(ATNode child:children) {
					//temp=temp+"+"+"("+getLogExp(child)+")";
					temp=temp+"+"+getLogExp(child);
				}
			}else {
				for(ATNode child:children) {      //�߼���
//					temp=temp+"*"+"("+getLogExp(child)+")";
					temp=temp+"*"+getLogExp(child);
				}
			}
			resultString+=temp.substring(1);
			return "("+resultString+")";
		} else {
//		System.out.println(root.getLabel());
//			if(!root.getLabel().contains("n_"))
					return root.getLabel();
//			else return "";
		}
	}
	
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String expand(String s) {
		try {
			ExprEvaluator util = new ExprEvaluator();
			IExpr expr = util.eval(s);
			IExpr result = util.eval(F.Expand(expr));
			return result.toString();

		} catch (SyntaxError e) {
			// catch Symja parser errors here
			System.out.println(e.getMessage());
		} catch (MathException me) {
			// catch Symja math errors here
			System.out.println(me.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (final StackOverflowError soe) {
			System.out.println(soe.getMessage());
		} catch (final OutOfMemoryError oome) {
			System.out.println(oome.getMessage());
		}
		return "";
	}

	public void show() {
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			// ��������
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		});
//	    	String result="";
		for (Entry<String, Double> mapping : list) {
			System.out.println(mapping.getKey() + mapping.getValue());
		}
		System.out.println("\n\n");
	}

	
	public void atomtree(String s) {

		String exp = expand(s);

		String[] xq = exp.split("\\+");
		for (String x : xq) {
			String ts = x.replace("*", " ,");
			ts = "{" + ts + "}";
			
			String[] hq = x.split("\\*");
			double re = 0;
			for (String y : hq) {
				re = re + defcost.get(y.toLowerCase()).doubleValue();
				if (re == Double.POSITIVE_INFINITY)
					break;
			}
			if (re != Double.POSITIVE_INFINITY) {
				atommap.put(ts, re);
			}
		}

	}

	public String showatomtree() {
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(atommap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			// ��������
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		});
		String result = "";
		for (Entry<String, Double> mapping : list) {
			//System.out.println(mapping.getKey() + mapping.getValue());
			
			result+=mapping.getKey() +":"+mapping.getValue()+"\n";
		}
		return result;
	}
	
	

	

//	public void moveDown(ATNode root) {
//		double rootvalue = root.getMincost();
//		Vector<ATNode> children = root.getChildren();
//		if (!children.isEmpty()) {
//			if (root.getRefinementType() == RefinementType.CONJUNCTIVE) {
//					double temp=Double.POSITIVE_INFINITY;
//					for(ATNode child:children) {
//						double childcost=child.getMincost();
//						if(childcost<temp) {
//							temp=childcost;
//						}
//					}
//					root.setMincost(Double.POSITIVE_INFINITY);
//					if(temp<rootvalue) {
//						for(ATNode child:children) {
//							//child.setMincost(temp);
//							moveDown(child);
//						}
//					}else {
//						for(ATNode child:children) {
//							child.setMincost(rootvalue);
//							moveDown(child);
//						}
//					}
//			}else {
//				double temp=0;
//				for(ATNode child:children) {
//					double childcost=child.getMincost();
//					temp+=childcost;
//				}
//				root.setMincost(Double.POSITIVE_INFINITY);
//				if(temp<rootvalue) {
//					for(ATNode child:children) {
//						moveDown(child);
//					}
//				}else {
////					children.get(0).setMincost(rootvalue-children.size());
////					moveDown(children.get(0));
//					for(int i=0;i<children.size();i++) {
//						children.get(i).setMincost(rootvalue);
//						moveDown(children.get(i));
//					}
//				}
//			}
//		}
//	}
//	

	public void showDefCost(ATNode root) {
		if (root == null)
			return;
		System.out.println(root.getLabel().toLowerCase() + ": " + root.getMincost());
		Vector<ATNode> children = root.getChildren();
		for (ATNode child : children) {
			showDefCost(child);
		}
	}

	public HashMap<Integer,ResultRelation> getAllCombination(ATNode root){
		
		List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(atommap.entrySet());
		Integer count=0;
		HashMap<Integer,ResultRelation> necessary_defenseNodes_Map =new HashMap<Integer,ResultRelation>();
		for (Entry<String, Double> mapping : list) {
			String myTempString = mapping.getKey().substring(1,mapping.getKey().length()-1);
			String[] necessary_defenseNodes = myTempString.split(" ,");
			count++;
			ResultRelation myResultRelation = new ResultRelation();
			myResultRelation.setNecessary_defenseNodes(necessary_defenseNodes);
			myResultRelation.setDefenseCost(mapping.getValue());
			necessary_defenseNodes_Map.put(count , myResultRelation);
		
		}
		
		return necessary_defenseNodes_Map;
	}
	
	public void simplify1(ATNode root , String[] necessary_defenseNodes){
		if(root.getChildren().isEmpty()) {
			return;
		}
		
		Vector<ATNode> children = root.getChildren();   
		if (!children.isEmpty()) {
			for (ATNode child : children) {
				for(String necessary_defenseNode:necessary_defenseNodes){
					if(child.getLabel().equals(necessary_defenseNode)){
						child.setAttackSuccessProbability(child.getAttackSuccessProbability()+1.0);
					}
				}
				simplify1(child,necessary_defenseNodes);
				
			}
		}					
	}
	
	
	
	public void simplify2(ATNode root , String[] necessary_defenseNodes){
		
		if(root.getChildren().isEmpty()) {
			return;
		}
		
		Vector<ATNode> children = root.getChildren();   
		if (!children.isEmpty()) {
			for (ATNode child : children) {
				if(child.getAttackSuccessProbability()>1) {
					child.setAttackSuccessProbability(child.getAttackSuccessProbability()-1);
				}
				else{
					child.setAttackSuccessProbability(1.0);
				}
				simplify2(child,necessary_defenseNodes);
				
			}
		}					
	}
	public void simplify3(ATNode root){
		root.setAttackSuccessProbability(1.0);
		return;
	}
	
	public void jiance(ATNode root){
		if(root.getChildren().isEmpty()){
			//System.out.println(root.getLabel()+ ":"+root.getAttackSuccessProbability());
			return;
		}
		Vector<ATNode> children = root.getChildren();   
		if (!children.isEmpty()) {
			for(ATNode child:children){
				System.out.println(child.getLabel()+ " ��������:"+child.getMincost() + "  �����ɹ�����:"+(1-child.getAttackSuccessProbability()));
				jiance(child);
			}
			
		}	
	}
	private int ans=0;
	private String[][] tableValues = new String[50][3];// �������飬�����洢�������
	public String[][] getTableValues(ATNode root) {
		getInformation(root);
		return tableValues;
	}
	public void getInformation(ATNode root) {
		if(ans!=0) {
			tableValues[ans-1][0]="��Խڵ�"+root.getLabel()+"�������";   //System.out.println(tableValues[ans][0]);
			tableValues[ans-1][1]=root.getMincost()+"";  //System.out.println(tableValues[ans][1]);
			tableValues[ans-1][2]=String.format("%.3f",1-root.getAttackSuccessProbability()); //System.out.println(tableValues[ans][2]);
			//System.out.println(root.getLabel()+ " ��������:"+root.getMincost() + "  �����ɹ�����:"+(1-root.getAttackSuccessProbability()));
		}
		ans++;
		if(root.getChildren().isEmpty()){
			
			return;
		}
		Vector<ATNode> children = root.getChildren();   
		if (!children.isEmpty()) {
			for(ATNode child:children){
				getInformation(child);
			}
			
		}	
	}
	
	
	//public double sumProbability=1;
	public double getSumProbability(ATNode root){
		if(root.getChildren().isEmpty()) {
			//sumProbability=1;
			//System.out.println(root.getLabel()+":"+root.getRefinementType()+" sumProbability:"+root.getAttackSuccessProbability());
			return root.getAttackSuccessProbability();
			//System.out.println(sumProbability);
			//return sumProbability;
		}else{
			Vector<ATNode> children = root.getChildren();   
			if(root.getRefinementType()==RefinementType.CONJUNCTIVE) {  //�߼���
				double tempProbability = 1.0;
				for(ATNode child:children){
					tempProbability *= getSumProbability(child);
				}
				tempProbability = root.getAttackSuccessProbability() * tempProbability;	
				//System.out.println(root.getLabel()+":"+root.getRefinementType()+" sumProbability:"+tempProbability);
				return tempProbability;
			}else{
				double tempProbability=1.0;
				for(ATNode child:children){
					tempProbability *= (1-getSumProbability(child));
				}
				tempProbability = 1-tempProbability;
				tempProbability = root.getAttackSuccessProbability()*tempProbability;
				//System.out.println(root.getLabel()+":"+root.getRefinementType()+" sumProbability:"+tempProbability);
				return tempProbability;
			}	
			
			
		}
		//return 1.0;
		
				
	}
	

	//�ȼ�ת�����ɹ�����������ԭ�ӹ�����������
	public void equalTree(ATNode root) {
		if(root.getChildren().isEmpty()) {
			return;
		}
		
		Vector<ATNode> children = root.getChildren();     //
		Vector<ATNode> newchildren = new Vector<ATNode>();
		
		if (!children.isEmpty()) {
			for (ATNode child : children) {
				if (child.getMincost() != Double.POSITIVE_INFINITY) {   //�����ǰ�ڵ� ���ڷ����ڵ�
					ATNode newparent = new ATNode();  //���½�һ���ڵ�
					newparent.setRefinementType(child.getRefinementType());
					newparent.setLabel("n_" + ATNode.getNums());
					newparent.setChildren(child.getChildren());
					equalTree(newparent);   //�ݹ�
					
//					equalTree(child);
					ATNode ppareAtNode = new ATNode();
					ppareAtNode.setRefinementType(RefinementType.CONJUNCTIVE);
					ppareAtNode.setLabel("n_" + ATNode.getNums());
					child.setChildren(new Vector<ATNode>());
					ppareAtNode.addChild(child);
					ppareAtNode.addChild(newparent);
					newchildren.add(ppareAtNode);

				} else {
					equalTree(child);
					newchildren.add(child);
				}
			}
			root.setChildren(newchildren);
		}

	}

	public void getEqualTree(ATNode root) {
		ATNode newRoot = new ATNode();
		newRoot.getChildren().add(root);
		equalTree(newRoot);
	}
	
	public void removeNnode(ATNode root) {
		Vector<ATNode> children=root.getChildren();
		if (!children.isEmpty()) {
			for(int i=0;i<children.size();i++) {
				ATNode child=children.get(i);
				if(child.getLabel().contains("n_")&&child.getChildren().isEmpty())
							children.remove(child);
				else
				            removeNnode(child);
			}
		}
	}

}
