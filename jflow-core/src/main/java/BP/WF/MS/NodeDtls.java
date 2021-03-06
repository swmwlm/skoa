package BP.WF.MS;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;

/** 
 制度章节
*/
public class NodeDtls extends EntitiesMM
{
	/** 
	 制度章节
	*/
	public NodeDtls()
	{
	}
	/** 
	 制度章节
	 
	 @param NodeID 节点ID
	*/
	public NodeDtls(int NodeID)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeDtlAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 得调用它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeDtl();
	}
}