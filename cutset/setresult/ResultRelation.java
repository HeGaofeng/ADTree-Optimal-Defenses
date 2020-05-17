package cutset.setresult;

public class ResultRelation {
	private Double defenseCost;
	private String[] necessary_defenseNodes;
	private Double defenseSuccessProbability;
	
	public ResultRelation(){
		
	}
	
	public void setDefenseCost(Double defenseCost){
		this.defenseCost=defenseCost;
	}
	public Double getDefenseCost(){
		return this.defenseCost;
	}
	
	public void setNecessary_defenseNodes(String[] necessary_defenseNodes){
		this.necessary_defenseNodes=necessary_defenseNodes;
	}
	public String[] getNecessary_defenseNodes(){
		return this.necessary_defenseNodes;
	}
	public int getNecessary_defenseNodes_length(){
		return this.necessary_defenseNodes.length;
	}
	public void setDefenseSuccessProbability(Double defenseSuccessProbability){
		this.defenseSuccessProbability = defenseSuccessProbability;
	}
	public Double getDefenseSuccessProbability(){
		return this.defenseSuccessProbability;
	}
	
	public String showResult(){
		StringBuffer myStringBuffer=new StringBuffer();
		myStringBuffer.append("必须防御的节点集合：");
		
		for(int i=0;i<this.necessary_defenseNodes.length;i++){
			myStringBuffer.append(this.necessary_defenseNodes[i]+" ");
		}
		myStringBuffer.append("\t");
		
		myStringBuffer.append("整体防御代价："+this.defenseCost+"\t" + "最终防御成功概率："+this.getDefenseSuccessProbability()+"\n");
		return myStringBuffer.toString();
		
		
	}
}

