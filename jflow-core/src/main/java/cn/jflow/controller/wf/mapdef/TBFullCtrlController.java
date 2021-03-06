package cn.jflow.controller.wf.mapdef;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;
import BP.Sys.Frm.AttachmentUploadType;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.GroupField;
import BP.Sys.Frm.GroupFields;
import BP.Sys.Frm.MapDtl;
import BP.Sys.Frm.MapDtlAttr;
import BP.Sys.Frm.MapExt;
import BP.Sys.Frm.MapExtAttr;
import BP.Tools.StringHelper;

@Controller
@RequestMapping(value = "/WF/TBFullCtrl")
public class TBFullCtrlController {

	// /#region 属性。
	
	public String FK_MapData="";

	public String OperAttrKey="";

	public String ExtType="";
	
	public String RefNo="";

	public String Lab = null;

	@RequestMapping(value = "/Btn_Save_Click", method = RequestMethod.POST)
	public void Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String TB_SQL,String DDL_DBSrc) {
		MapExt me = new MapExt();
		
		FK_MapData=request.getParameter("FK_MapData");
		OperAttrKey=request.getParameter("OperAttrKey");
		ExtType=request.getParameter("ExtType");
		RefNo = request.getParameter("RefNo");
		if(null==ExtType||"null".equals(ExtType)){
			ExtType = MapExtAttr.ExtType;
		}
		
		me.Retrieve(MapExtAttr.FK_MapData, FK_MapData,
				MapExtAttr.ExtType, ExtType, MapExtAttr.AttrOfOper,
				RefNo);
		me.setExtType(this.ExtType);
		me.setDoc(TB_SQL);
		me.setAttrOfOper(RefNo);
		me.setFK_MapData(FK_MapData);
		me.setMyPK(this.FK_MapData + "_" + me.getExtType() + "_" + me.getAttrOfOper());

		// switch (this.DDL_DBSrc.Text)
		// ORIGINAL LINE: case "应用系统主数据库(默认)":
		if (DDL_DBSrc.equals("应用系统主数据库(默认)")) {
			me.setFK_DBSrc("0");
		}
		// ORIGINAL LINE: case "SQLServer数据库":
		else if (DDL_DBSrc.equals("SQLServer数据库")) {
			me.setFK_DBSrc("1");
		}
		// ORIGINAL LINE: case "WebService数据源":
		else if (DDL_DBSrc.equals("WebService数据源")) {
			me.setFK_DBSrc("100");
		}
		// ORIGINAL LINE: case "Oracle数据库":
		else if (DDL_DBSrc.equals("Oracle数据库")) {
			me.setFK_DBSrc("2");
		}
		// ORIGINAL LINE: case "MySQL数据库":
		else if (DDL_DBSrc.equals("MySQL数据库")) {
			me.setFK_DBSrc("3");
		}
		// ORIGINAL LINE: case "Informix数据库":
		else if (DDL_DBSrc.equals("Informix数据库")) {
			me.setFK_DBSrc("4");
		} else {
		}
		me.Save();

	}

	@RequestMapping(value = "/Btn_Delete_Click", method = RequestMethod.POST)
	public void Btn_Delete_Click(HttpServletRequest request,
			HttpServletResponse response) {
		FK_MapData=request.getParameter("FK_MapData");
		OperAttrKey=request.getParameter("OperAttrKey");
		ExtType=request.getParameter("ExtType");
		if(null==ExtType||"null".equals(ExtType)){
			ExtType = MapExtAttr.ExtType;
		}
		RefNo=request.getParameter("RefNo");
		
		MapExt me = new MapExt();

		me.Retrieve(MapExtAttr.FK_MapData, FK_MapData,
				MapExtAttr.ExtType, ExtType, MapExtAttr.AttrOfOper,
				RefNo);
		me.setDoc("");
		me.Update();
		//BP.Sys.PubClass.WinClose();
	}

	/*@RequestMapping(value = "/Btn_FullDtl_Click", method = RequestMethod.POST)
	public void Btn_FullDtl_Click(HttpServletRequest request,
			HttpServletResponse response) {
		Response.Redirect("TBFullCtrl_Dtl.jsp?FK_MapData="
				+ this.getFK_MapData() + "&MyPK=" + this.MyPK + "");
	}

	@RequestMapping(value = "/Btn_FullDDL_Click", method = RequestMethod.POST)
	public void Btn_FullDDL_Click(HttpServletRequest request,
			HttpServletResponse response) {
		// Response.Redirect("TBFullCtrl_List.aspx?FK_MapData=" +
		// this.FK_MapData + "&MyPK=" + this.MyPK + "");
		Response.Redirect("TBFullCtrl_ListNew.jsp?FK_MapData="
				+ this.getFK_MapData() + "&MyPK=" + this.MyPK + "");
	}*/
}
