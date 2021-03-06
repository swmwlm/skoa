package BP.WF.Template;

import BP.DA.Depositary;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.Sys.Frm.EventListOfNode;
import BP.Sys.Frm.FrmEventAttr;
import BP.Tools.StringHelper;

/**
 * 消息推送
 */
public class PushMsg extends EntityMyPK
{
	// 基本属性
	/**
	 * 事件
	 */
	public final String getFK_Event()
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Event);
	}
	
	public final void setFK_Event(String value)
	{
		this.SetValByKey(PushMsgAttr.FK_Event, value);
	}
	
	public final int getPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.PushWay);
	}
	
	public final void setPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.PushWay, value);
	}
	
	/**
	 * 节点
	 */
	public final int getFK_Node()
	{
		return this.GetValIntByKey(PushMsgAttr.FK_Node);
	}
	
	public final void setFK_Node(int value)
	{
		this.SetValByKey(PushMsgAttr.FK_Node, value);
	}
	
	public final String getPushDoc()
	{
		String s = this.GetValStringByKey(PushMsgAttr.PushDoc);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	
	public final void setPushDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.PushDoc, value);
	}
	
	public final String getTag()
	{
		String s = this.GetValStringByKey(PushMsgAttr.Tag);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	
	public final void setTag(String value)
	{
		this.SetValByKey(PushMsgAttr.Tag, value);
	}
	
	// 事件消息.
	public final boolean getMobilePushEnable()
	{
		return this.GetValBooleanByKey(PushMsgAttr.MobilePushEnable);
	}
	
	public final void setMobilePushEnable(boolean value)
	{
		this.SetValByKey(PushMsgAttr.MobilePushEnable, value);
	}
	
	/**
	 * 是否启用邮件发送?
	 */
	public final boolean getMsgMailEnable()
	{
		return this.GetValBooleanByKey(PushMsgAttr.MsgMailEnable);
	}
	
	public final void setMsgMailEnable(boolean value)
	{
		this.SetValByKey(PushMsgAttr.MsgMailEnable, value);
	}
	
	public final String getMailTitle()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
		} else if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			return "新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
		} else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
			return "移交来的新工作{{Title}},移交人@WebUser.No,@WebUser.Name";
		} else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			return "被退回来{{Title}},退回人@WebUser.No,@WebUser.Name";
		} else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
			return "工作被撤销{{Title}},发送人@WebUser.No,@WebUser.Name";
		} else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter))
		{
			return "加签新工作{{Title}},发送人@WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)){
			return "流程{{Title}}已经结束,处理人@WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.AfterFlowDel)){
			return "流程{{Title}}已经删除,处理人@WebUser.No,@WebUser.Name";
		}
		else
		{
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
		
	}
	
	/**
	 * 邮件标题
	 */
	public final String getMailTitle_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		return str;
	}
	
	public final void setMailTitle_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailTitle, value);
	}
	
	/**
	 * 邮件内容
	 */
	public final String getMailDoc_Real()
	{
		return this.GetValStrByKey(PushMsgAttr.MailDoc);
	}
	
	public final void setMailDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailDoc, value);
	}
	
	public final String getMailDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive)) {
			str += "\t\n您好:";
			str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作{Url} .";
			str += "\t\n致! ";
			str += "\t\n    @WebUser.No, @WebUser.Name";
			str += "\t\n    @RDT";
		}else if (this.getFK_Event().equals(EventListOfNode.SendSuccess)){
			str += "\t\n您好:";
			str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作{Url} .";
			str += "\t\n致! ";
			str += "\t\n    @WebUser.No, @WebUser.Name";
			str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			str += "\t\n您好:";
			str += "\t\n    工作{{Title}}被退回来了, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    @WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    @WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    @WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter)) // 加签.
		{
			str += "\t\n您好:";
			str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
			str += "\t\n 致! ";
			str += "\t\n    @WebUser.No,@WebUser.Name";
			str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) //流程结束后.
		{
			  str += "\t\n您好:";
              str += "\t\n    工作{{Title}}已经结束, 点击这里打开工作报告{Url} .";
              str += "\t\n 致! ";
              str += "\t\n    @WebUser.No,@WebUser.Name";
              str += "\t\n    @RDT";
		} else if (this.getFK_Event().equals(EventListOfNode.AfterFlowDel)) //流程删除后.
		{
			str += "\t\n您好:";
            str += "\t\n    工作{{Title}}已经删除.";
            str += "\t\n 致! ";
            str += "\t\n    @WebUser.No,@WebUser.Name";
            str += "\t\n    @RDT";
		}
		else
		{
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
		return str;
	}
	
	/**
	 * 是否启用短信发送
	 */
	public final boolean getSMSEnable()
	{
		return this.GetValBooleanByKey(PushMsgAttr.SMSEnable);
	}
	
	public final void setSMSEnable(boolean value)
	{
		this.SetValByKey(PushMsgAttr.SMSEnable, value);
	}
	
	/**
	 * 短信模版内容
	 */
	public final String getSMSDoc_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		return str;
	}
	
	public final void setSMSDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	
	/**
	 * 短信模版内容
	 */
	public final String getSMSDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			str = "有新工作{{Title}}需要您处理, 发送人:@WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			str = "有新工作{{Title}}需要您处理, 发送人:@WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			str = "工作{{Title}}被退回,退回人:@WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
			str = "移交工作{{Title}},移交人:@WebUser.No, @WebUser.Name,打开{Url} .";
		} else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
			str = "工作撤销{{Title}},撤销人:@WebUser.No, @WebUser.Name,打开{Url}.";
		} else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter)) // 加签.
		{
			str = "工作加签{{Title}},加签人:@WebUser.No, @WebUser.Name,打开{Url}.";
		} 
		else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) // 流程结束后.
		{
			str = "流程{{Title}}已经结束,最后处理人:@WebUser.No, @WebUser.Name,打开{Url}.";
		} 
		else if (this.getFK_Event().equals(EventListOfNode.AfterFlowDel)) // 流程删除后.
		{
			str = "流程{{Title}}已经删除,最后处理人:@WebUser.No, @WebUser.Name";
		} 
		else
		{
			throw new RuntimeException("@该事件类型没有定义默认的消息模版:"
					+ this.getFK_Event());
		}
		return str;
	}
	
	public final void setSMSDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	
	// 构造方法
	/**
	 * 消息推送
	 */
	public PushMsg()
	{
		
	}
	
	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		
		Map map = new Map("WF_PushMsg");
		map.setEnDesc("消息推送");
		
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		
		map.AddMyPK();
		
		map.AddTBInt(PushMsgAttr.FK_Node, 0, "节点", true, false);
		map.AddTBString(PushMsgAttr.FK_Event, null, "FK_Event", true, false, 0,
				15, 10);
		// map.AddTBInt(PushMsgAttr.PushWay, 0, "推送方式", true, false);
		map.AddDDLSysEnum(PushMsgAttr.PushWay, 0, "推送方式", true, false,
				PushMsgAttr.PushWay,
				"@0=按照指定节点的工作人员@1=按照指定的工作人员@2=按照指定的工作岗位@3=按照指定的部门@4=按照指定的SQL@5=按照系统指定的字段");
		
		// 设置内容.
		map.AddTBString(PushMsgAttr.PushDoc, null, "推送保存内容", true, false, 0,
				3500, 10);
		map.AddTBString(PushMsgAttr.Tag, null, "Tag", true, false, 0, 500, 10);
		
		map.AddTBInt(PushMsgAttr.SMSPushWay, 0, "短信发送方式", true, true);
		map.AddTBString(PushMsgAttr.SMSField, null, "短信字段", true, false, 0, 100, 10);
		map.AddTBStringDoc(PushMsgAttr.SMSDoc, null, "短信内容模版", true, false, true);

		// 消息设置.
		map.AddTBInt(PushMsgAttr.MailPushWay, 0, "邮件发送方式",true, true);
		map.AddBoolean(FrmEventAttr.MsgMailEnable, true,
				"是否启用邮件发送？(如果启用就要设置邮件模版，支持ccflow表达式。)", true, true, true);
		map.AddTBString(FrmEventAttr.MailTitle, null, "邮件标题模版", true, false, 0,
				200, 20, true);
		map.AddTBStringDoc(FrmEventAttr.MailDoc, null, "邮件内容模版", true, false,true);
		map.AddTBString(PushMsgAttr.MailAddress, null, "邮件字段", true, false, 0, 100, 10);

		
		// 是否启用手机短信？
		map.AddBoolean(FrmEventAttr.SMSEnable, false,
				"是否启用短信发送？(如果启用就要设置短信模版，支持ccflow表达式。)", true, true, true);
		map.AddTBStringDoc(FrmEventAttr.SMSDoc, null, "短信内容模版", true, false,true);
		map.AddBoolean(FrmEventAttr.MobilePushEnable, true, "是否推送到手机、pad端。",true, true, true);
		// 消息设置.
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		/*this.setMyPK(this.getFK_Event() + "_" + this.getFK_Node() + "_"
				+ this.getPushWay());*/
		return super.beforeUpdateInsertAction();
	}
	/** 
	 短信接收人字段
	 
	*/
	public final String getSMSField() {
		return this.GetValStringByKey(PushMsgAttr.SMSField);
	}
	public final void setSMSField(String value) {
		this.SetValByKey(PushMsgAttr.SMSField, value);
	}
	/**
	 * 接受短信的节点
	 */
	public final String getSMSNodes() {
		return this.GetValStringByKey(PushMsgAttr.SMSNodes);
	}
	public final void setSMSNodes(String value) {
		this.SetValByKey(PushMsgAttr.SMSNodes, value);
	}
	/** 
	 短信提醒方式
	 
	*/
	public final int getSMSPushWay() {
		return this.GetValIntByKey(PushMsgAttr.SMSPushWay);
	}
	public final void setSMSPushWay(int value) {
		this.SetValByKey(PushMsgAttr.SMSPushWay, value);
	}


	/** 
	 发送消息标签
	 
	*/
	public final String getSMSPushWayText() {
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "发送给当前节点的所有处理人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "发送给下一个节点的所有接受人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "发送给被退回的节点处理人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.UndoneAfter)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "撤销工作后通知的节点接收人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "流程结束后通知的节点接收人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.AfterFlowDel)) {
			if (this.getSMSPushWay() == 0) {
				return "不发送";
			}

			if (this.getSMSPushWay() == 1) {
				return "流程删除后通知的节点接收人";
			}

			if (this.getSMSPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getSMSPushWay() == 3) {
				return "其他节点的处理人";
			}
		}

		return "未知";
	}
	/** 
	 邮件地址
	 
	*/
	public final String getMailAddress() {
		return this.GetValStringByKey(PushMsgAttr.MailAddress);
	}
	public final void setMailAddress(String value) {
		this.SetValByKey(PushMsgAttr.MailAddress, value);
	}
	/** 
	  推送邮件的节点s
	 
	 */
	public final String getMailNodes() {
		return this.GetValStringByKey(PushMsgAttr.MailNodes);
	}
	public final void setMailNodes(String value) {
		this.SetValByKey(PushMsgAttr.MailNodes, value);
	}

	/** 
	 邮件推送方式
	 
	*/

	public final String getMailPushWayText() {
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "发送给当前节点的所有处理人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "发送给下一个节点的所有接受人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "发送给被退回的节点处理人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.UndoneAfter)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "撤销工作后通知的节点接收人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "流程结束后通知的节点接收人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		if (this.getFK_Event().equals(EventListOfNode.AfterFlowDel)) {
			if (this.getPushWay() == 0) {
				return "不发送";
			}

			if (this.getPushWay() == 1) {
				return "流程删除后通知的节点接收人";
			}

			if (this.getPushWay() == 2) {
				return "向指定的字段发送";
			}
			if (this.getPushWay() == 3) {
				return "其他节点的处理人";
			}
		}
		

		return "未知";
	}

}