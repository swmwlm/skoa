package BP.Sys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.util.ContextHolderUtils;
import BP.DA.DBAccess;
import BP.DA.DBCheckLevel;
import BP.DA.DBType;
import BP.DA.DBUrlType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.En.UIContralType;
import BP.Sys.Frm.GroupField;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDatas;
import BP.Sys.Frm.MapDtl;
import BP.Sys.Frm.MapDtls;
import BP.Web.WebUser;

/**
 * PageBase 的摘要说明。
 */
public class PubClass {
	
	private static final String Color = null;

	/**
	 * 发送邮件
	 * 
	 * @param maillAddr
	 *            地址
	 * @param title
	 *            标题
	 * @param doc
	 *            内容
	 */
	public static void SendMail(String maillAddr, String title, String doc) {

		String host = SystemConfig.GetValByKey("SendEmailHost", "smtp.gmail.com"); // smtp服务器
		String user = SystemConfig.GetValByKey("SendEmailAddress", "ccflow.cn@gmail.com"); // 用户名
		String pwd = SystemConfig.GetValByKey("SendEmailPass", "ccflow123"); // 密码
		// 上述写你的邮箱和密码
		// client.Port = SystemConfig.GetValByKeyInt("SendEmailPort", 587);
		// //使用的端口
		// client.Host = ;
		// client.EnableSsl = true; //经过ssl加密.
		Properties props = new Properties();
		// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
		props.put("mail.smtp.host", host);
		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
		props.put("mail.smtp.auth", "true");
		// 用刚刚设置好的props对象构建一个session
		Session session = Session.getDefaultInstance(props);
		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（你可以在控制台（console)上看到发送邮件的过程）
		session.setDebug(true);
		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 加载发件人地址
			message.setFrom(new InternetAddress(user));
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(maillAddr));
			// 加载标题
			message.setSubject(title);
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(doc);
			multipart.addBodyPart(contentPart);

			// 添加附件
			// BodyPart messageBodyPart = new MimeBodyPart();
			// DataSource source = new FileDataSource(affix);
			// 添加附件的内容
			// messageBodyPart.setDataHandler(new DataHandler(source));
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			// messageBodyPart.setFileName("=?GBK?B?"+
			// enc.encode(affixName.getBytes()) + "?=");
			// multipart.addBodyPart(messageBodyPart);

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();
			// 发送邮件
			Transport transport = session.getTransport("smtp");
			// 连接服务器的邮箱
			transport.connect(host, user, pwd);
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String ToHtmlColor(String colorName) throws Exception {
		try
		{
			if (colorName.startsWith("#"))
			{
				colorName = colorName.replace("#", "");
				//update by dgq 六位颜色不需要转换
				if (colorName.length() == 6||colorName.length() == 3)
				{
					return "#" + colorName;
				}else if(colorName.length() == 8){
					return "#" + colorName.substring(2, 8);
				}else{
					return "#" + colorName;
				}
			}else{
				return colorName;
			}
		}
		catch (java.lang.Exception e)
		{
			return "black";
		}
	}

	public static void InitFrm(String fk_mapdata) {
		// 删除数据.
		BP.Sys.Frm.FrmLabs labs = new BP.Sys.Frm.FrmLabs();
		try {
			labs.Delete(BP.Sys.Frm.FrmLabAttr.FK_MapData, fk_mapdata);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BP.Sys.Frm.FrmLines lines = new BP.Sys.Frm.FrmLines();
		try {
			lines.Delete(BP.Sys.Frm.FrmLabAttr.FK_MapData, fk_mapdata);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BP.Sys.Frm.MapData md = new BP.Sys.Frm.MapData();
		md.setNo(fk_mapdata);
		if (md.RetrieveFromDBSources() == 0) {
			BP.Sys.Frm.MapDtl mdtl = new BP.Sys.Frm.MapDtl();
			mdtl.setNo(fk_mapdata);
			if (mdtl.RetrieveFromDBSources() == 0) {
				throw new RuntimeException("@对:" + fk_mapdata + "的映射信息不存在.");
			} else {
				md.Copy(mdtl);
			}
		}

		BP.Sys.Frm.MapAttrs mattrs = new BP.Sys.Frm.MapAttrs(fk_mapdata);
		BP.Sys.Frm.GroupFields gfs = new BP.Sys.Frm.GroupFields(fk_mapdata);

		int tableW = 700;
		int padingLeft = 3;
		int leftCtrlX = 700 / 100 * 20;
		int rightCtrlX = 700 / 100 * 60;

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmss");
		String keyID = sdf.format(new Date());
		// table 标题。
		int currX = 0;
		int currY = 0;
		BP.Sys.Frm.FrmLab lab = new BP.Sys.Frm.FrmLab();
		lab.setText(md.getName());
		lab.setFontSize(20);
		lab.setX(200);
		currY += 30;
		lab.setY(currY);
		lab.setFK_MapData(fk_mapdata);
		lab.setFontWeight("Bold");
		lab.setMyPK("Lab" + keyID + "1");
		lab.Insert();

		// 表格头部的横线.
		currY += 20;
		BP.Sys.Frm.FrmLine lin = new BP.Sys.Frm.FrmLine();
		lin.setX1(0);
		lin.setX2(tableW);
		lin.setY1(currY);
		lin.setY2(currY);
		lin.setBorderWidth(2);
		lin.setFK_MapData(fk_mapdata);
		lin.setMyPK("Lin" + keyID + "1");
		lin.Insert();
		currY += 5;

		boolean isLeft = false;
		int i = 2;
		for (Object gf : gfs) {
			i++;
			lab = new BP.Sys.Frm.FrmLab();
			lab.setX(0);
			lab.setY(currY);
			lab.setText(((GroupField) gf).getLab());
			lab.setFK_MapData(fk_mapdata);
			lab.setFontWeight("Bold");
			lab.setMyPK("Lab" + keyID + (new Integer(i)).toString());
			lab.Insert();

			currY += 15;
			lin = new BP.Sys.Frm.FrmLine();
			lin.setX1(padingLeft);
			lin.setX2(tableW);
			lin.setY1(currY);
			lin.setY2(currY);
			lin.setFK_MapData(fk_mapdata);
			lin.setBorderWidth(3);
			lin.setMyPK("Lin" + keyID + (new Integer(i)).toString());
			lin.Insert();

			isLeft = true;
			int idx = 0;
			for (Object attr : mattrs) {
				if (((GroupField) gf).getOID() != ((MapAttr) attr).getGroupID() || !((MapAttr) attr).getUIVisible()) {
					continue;
				}

				idx++;
				if (isLeft) {
					lin = new BP.Sys.Frm.FrmLine();
					lin.setX1(0);
					lin.setX2(tableW);
					lin.setY1(currY);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + (new Integer(i)).toString() + idx);
					lin.Insert();
					currY += 14; // 画一横线.

					lab = new BP.Sys.Frm.FrmLab();
					lab.setX(lin.getX1() + padingLeft);
					lab.setY(currY);
					lab.setText(((MapAttr) attr).getName());
					lab.setFK_MapData(fk_mapdata);
					lab.setMyPK("Lab" + keyID + (new Integer(i)).toString() + idx);
					lab.Insert();

					lin = new BP.Sys.Frm.FrmLine();
					lin.setX1(leftCtrlX);
					lin.setY1(currY - 14);

					lin.setX2(leftCtrlX);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + (new Integer(i)).toString() + idx + "R");
					lin.Insert(); // 画一 竖线

					((MapAttr) attr).setX(leftCtrlX + padingLeft);
					((MapAttr) attr).setY(currY - 3);
					((MapAttr) attr).setUIWidth(150);
					((MapAttr) attr).Update();
					currY += 14;
				} else {
					currY = currY - 14;
					lab = new BP.Sys.Frm.FrmLab();
					lab.setX(tableW / 2 + padingLeft);
					lab.setY(currY);
					lab.setText(((MapAttr) attr).getName());
					lab.setFK_MapData(fk_mapdata);
					lab.setMyPK("Lab" + keyID + (new Integer(i)).toString() + idx);
					lab.Insert();

					lin = new BP.Sys.Frm.FrmLine();
					lin.setX1(tableW / 2);
					lin.setY1(currY - 14);

					lin.setX2(tableW / 2);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + (new Integer(i)).toString() + idx);
					lin.Insert(); // 画一 竖线

					lin = new BP.Sys.Frm.FrmLine();
					lin.setX1(rightCtrlX);
					lin.setY1(currY - 14);
					lin.setX2(rightCtrlX);
					lin.setY2(currY);
					lin.setFK_MapData(fk_mapdata);
					lin.setMyPK("Lin" + keyID + (new Integer(i)).toString() + idx + "R");
					lin.Insert(); // 画一 竖线

					((MapAttr) attr).setX(rightCtrlX + padingLeft);
					((MapAttr) attr).setY(currY - 3);
					((MapAttr) attr).setUIWidth(150);
					((MapAttr) attr).Update();
					currY += 14;
				}
				isLeft = !isLeft;
			}
		}
		// table bottom line.
		lin = new BP.Sys.Frm.FrmLine();
		lin.setX1(0);
		lin.setY1(currY);

		lin.setX2(tableW);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eR");
		lin.Insert();

		currY = currY - 28 - 18;
		// 处理结尾. table left line
		lin = new BP.Sys.Frm.FrmLine();
		lin.setX1(0);
		lin.setY1(50);
		lin.setX2(0);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eRr");
		lin.Insert();

		// table right line.
		lin = new BP.Sys.Frm.FrmLine();
		lin.setX1(tableW);
		lin.setY1(50);
		lin.setX2(tableW);
		lin.setY2(currY);
		lin.setFK_MapData(fk_mapdata);
		lin.setBorderWidth(3);
		lin.setMyPK("Lin" + keyID + "eRr4");
		lin.Insert();
	}

	// public static String ColorToStr(Color color)
	// {
	// try
	// {
	// String color_s = System.Drawing.ColorTranslator.ToHtml(color);
	// color_s = color_s.substring(1, color_s.length());
	// return "#" + String.valueOf(Integer.parseInt(color_s, 16) + 40000, 16);
	// }
	// catch (java.lang.Exception e)
	// {
	// return "black";
	// }
	// }
	/**
	 * 处理字段
	 * 
	 * @param fd
	 * @return
	 */
	public static String DealToFieldOrTableNames(String fd) {
		String keys = "~!@#$%^&*()+{}|:<>?`=[];,./～！＠＃￥％……＆×（）——＋｛｝｜：“《》？｀－＝［］；＇，．／";
		char[] cc = keys.toCharArray();
		for (char c : cc) {
			fd = fd.replace((new Character(c)).toString(), "");
		}
		String s = fd.substring(0, 1);
		try {
			int a = Integer.parseInt(s);
			fd = "F" + fd;
		} catch (java.lang.Exception e) {}
		return fd;
	}

	private static String _KeyFields = null;

	public static String getKeyFields() {
		if (_KeyFields == null) {
			_KeyFields = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfData() + "/Sys/FieldKeys.txt");
		}
		return _KeyFields;
	}

	public static boolean IsNum(String str) {
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";

		Pattern pattern = Pattern.compile(cn_Regex);
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			strResult = true;
		} else {
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsCN(String str) {
		boolean strResult;
		String cn_Regex = "^[\\u4e00-\\u9fa5]+$";
		Pattern pattern = Pattern.compile(cn_Regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			strResult = true;
		} else {
			strResult = false;
		}
		return strResult;
	}

	public static boolean IsImg(String ext) {
		ext = ext.replace(".", "").toLowerCase();
		if (ext.equals("gif")) {
			return true;
		} else if (ext.equals("jpg")) {
			return true;
		} else if (ext.equals("bmp")) {
			return true;
		} else if (ext.equals("png")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按照比例数小
	 * 
	 * @param ObjH
	 *            目标高度
	 * @param factH
	 *            实际高度
	 * @param factW
	 *            实际宽度
	 * @return 目标宽度
	 */
	public static int GenerImgW_del(int ObjH, int factH, int factW, int isZeroAsWith) {
		if (factH == 0 || factW == 0) {
			return isZeroAsWith;
		}

		int d = ObjH / (factH * factW);

		try {
			return d;
		} catch (RuntimeException ex) {
			throw new RuntimeException(d + ex.getMessage());
		}
	}

	/**
	 * 按照比例数小
	 * 
	 * @param ObjH
	 *            目标高度
	 * @param factH
	 *            实际高度
	 * @param factW
	 *            实际宽度
	 * @return 目标宽度
	 */
	public static int GenerImgH(int ObjW, int factH, int factW, int isZeroAsWith) {
		if (factH == 0 || factW == 0) {
			return isZeroAsWith;
		}

		int d = ObjW / (factW * factH);

		try {
			return d;
		} catch (RuntimeException ex) {
			throw new RuntimeException(d + ex.getMessage());
		}
	}

	public static String FilesViewStr(String enName, Object pk) {
		String url = "/WF/Comm/FileManager.jsp?EnsName=" + enName + "&PK=" + pk.toString();
		String strs = "";
		SysFileManagers ens = new SysFileManagers(enName, pk.toString());
		String path = BP.Sys.Glo.getRequest().getRemoteHost();

		for (Object file : ens) {
			strs += "<img src='/WF/Img/FileType/" + ((SysFileManager) file).getMyFileExt().replace(".", "") + ".gif' border=0 /><a href='" + path
					+ ((SysFileManager) file).getMyFilePath() + "' target='_blank' >" + ((SysFileManager) file).getMyFileName()
					+ ((SysFileManager) file).getMyFileExt() + "</a>&nbsp;";
			if (WebUser.getNo().equals(((SysFileManager) file).getRec())) {
				strs += "<a title='打开它' href=\"javascript:DoAction('" + path + "Comm/Do.jsp?ActionType=1&OID=" + ((SysFileManager) file).getOID()
						+ "&EnsName=" + enName + "&PK=" + pk + "','删除文件《" + ((SysFileManager) file).getMyFileName()
						+ ((SysFileManager) file).getMyFileExt() + "》')\" ><img src='" + path
						+ "../Img/Btn/delete.gif' border=0 alt='删除此附件' /></a>&nbsp;";
			}
		}
		return strs;
	}

	public static String GenerLabelStr(String title) {
		String path = BP.Sys.Glo.getRequest().getRemoteHost();
		if (path.equals("") || path.equals("/")) {
			path = "..";
		}

		String str = "";
		str += "<TABLE  height='100%' cellPadding='0' background='" + path + "/Images/DG_bgright.gif'>";
		str += "<TBODY>";
		str += "<TR   >";
		str += "<TD  >";
		str += "<IMG src='" + path + "/Images/DG_Title_Left.gif' border='0'></TD>";
		str += "<TD style='font-size:14px'  vAlign='bottom' noWrap background='" + path + "/Images/DG_Title_BG.gif'>&nbsp;";
		str += " &nbsp;<b>" + title + "</b>&nbsp;&nbsp;";
		str += "</TD>";
		str += "<TD>";
		str += "<IMG src='" + path + "/Images/DG_Title_Right.gif' border='0'></TD>";
		str += "</TR>";
		str += "</TBODY>";
		str += "</TABLE>";
		return str;
		// return str;
	}

	/**
	 * 将汉字转换成拼音
	 * 
	 * @param str
	 *            要转换的汉字
	 * @return 返回的拼音
	 */
	public final String Chs2Pinyin(String str) {
		return BP.Tools.chs2py.convert(str);
	}

	public static String GenerTablePage(DataTable dt, String title) {

		String str = "<Table id='tb' class=Table >";

		str += "<caption>" + title + "</caption>";

		// 标题
		str += "<TR>";
		for (DataColumn dc : dt.Columns) {
			str += "<TD class='DGCellOfHeader" + BP.Web.WebUser.getStyle() + "' nowrap >" + dc.ColumnName + "</TD>";
		}
		str += "</TR>";

		// 内容
		for (DataRow dr : dt.Rows) {
			str += "<TR>";

			for (DataColumn dc : dt.Columns) {
				// string doc=dr[dc.ColumnName];
				str += "<TD nowrap=true >&nbsp;" + dr.getValue(dc.ColumnName) + "</TD>";
			}
			str += "</TR>";
		}
		str += "</Table>";
		return str;
	}

	/**
	 * 产生临时文件名称
	 * 
	 * @param hz
	 * @return
	 */
	public static String GenerTempFileName(String hz) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddhhmmss");
		return BP.Web.WebUser.getNo() + formatter.format(new Date()) + "." + hz;
	}

	public static void DeleteTempFiles() {
		File file = new File(SystemConfig.getPathOfTemp());
		File[] strs = file.listFiles();

		for (File s : strs) {
			s.delete();
		}
	}

	/**
	 * 重新建立索引
	 */
	public static void ReCreateIndex() {
		java.util.ArrayList als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Object obj : als) {
			Entity en = (Entity) obj;
			if (en.getEnMap().getEnType() == EnType.View) {
				continue;
			}
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable() + "') <BR> DROP TABLE "
					+ en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}

	}

	public static void DBIOToAccess() {
		java.util.ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Entities");
		PubClass.DBIO(DBType.Access, al, false);
	}

	/**
	 * 检查所有的物理表
	 */
	public static void CheckAllPTable(String nameS) {
		java.util.ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Entities");
		for (Object ens : al) {
			if (!ens.toString().contains(nameS)) {
				continue;
			}

			try {
				Entity en = ((Entities) ens).getGetNewEntity();
				en.CheckPhysicsTable();
			} catch (java.lang.Exception e) {
				
			}

		}

	}

	/**
	 * 数据传输
	 * 
	 * @param dbtype
	 *            对象
	 * @return
	 */
	public static void DBIO(BP.DA.DBType dbtype, java.util.ArrayList als, boolean creatTableOnly) {
		for (Object ens : als) {
			Entity myen = ((Entities) ens).getGetNewEntity();
			if (myen.getEnMap().getEnType() == EnType.View) {
				continue;
			}

			// create table
			switch (dbtype) {

			case Oracle:
				try {

					// DBAccessOfOracle.RunSQL("drop table " +
					// myen.getEnMap().getPhysicsTable());
				} catch (java.lang.Exception e) {}
				try {
					// DBAccessOfOracle.RunSQL(SqlBuilder.GenerCreateTableSQLOfOra_OK(myen));
				} catch (java.lang.Exception e2) {

				}
				break;
			case MSSQL:
				try {
					if (myen.getEnMap().getPhysicsTable().contains(".")) {
						continue;
					}

					// if
					// (DBAccessOfMSMSSQL.IsExitsObject(myen.getEnMap().getPhysicsTable()))
					// {
					// continue;
					// }
					//
					// DBAccessOfMSMSSQL.RunSQL("drop table " +
					// myen.getEnMap().getPhysicsTable());
				} catch (java.lang.Exception e3) {}
				// DBAccessOfMSMSSQL.RunSQL(SqlBuilder.GenerCreateTableSQLOfMS(myen));
				break;
			case Informix:
				try {
					if (myen.getEnMap().getPhysicsTable().contains(".")) {
						continue;
					}

					// if
					// (DBAccessOfMSMSSQL.IsExitsObject(myen.getEnMap().getPhysicsTable()))
					// {
					// continue;
					// }
					//
					// DBAccessOfMSMSSQL.RunSQL("drop table " +
					// myen.getEnMap().getPhysicsTable());
				} catch (java.lang.Exception e4) {}
				// DBAccessOfMSMSSQL.RunSQL(SqlBuilder.GenerCreateTableSQLOfInfoMix(myen));
				break;
			case Access:
				try {
					// DBAccessOfOLE.RunSQL("drop table " +
					// myen.getEnMap().getPhysicsTable());
				} catch (java.lang.Exception e5) {}
				// DBAccessOfOLE.RunSQL(SqlBuilder.GenerCreateTableSQLOf_OLE(myen));
				break;
			default:
				throw new RuntimeException("error :");

			}

			if (creatTableOnly) {
				return;
			}

			try {
				QueryObject qo = new QueryObject(((Entities) ens));
				qo.DoQuery();
				// ens.RetrieveAll(1000);
			} catch (java.lang.Exception e6) {
				continue;
			}

			// insert data
			for (Object en : ((Entities) ens)) {
				try {
					switch (dbtype) {
					case Oracle:
					case Informix:
						// DBAccessOfOracle.RunSQL(SqlBuilder.Insert(en));
						break;
					case MSSQL:
						// DBAccessOfMSMSSQL.RunSQL(SqlBuilder.Insert(en));
						break;
					case Access:
						// DBAccessOfOLE.RunSQL(SqlBuilder.InsertOFOLE(en));
						break;
					default:
						break;
					}
				} catch (RuntimeException ex) {
					Log.DefaultLogWriteLineError(dbtype.toString() + "bak出现错误：" + ex.getMessage());
				}
			}
		}
	}

	/**
	 * 获取datatable.
	 * 
	 * @param uiBindKey
	 * @return
	 */
	public static DataTable GetDataTableByUIBineKey(String uiBindKey) {
		DataTable dt = new DataTable();
		if (uiBindKey.contains(".")) {
			Entities ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}

			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}
			if (ens == null) {
				throw new RuntimeException("类名错误:" + uiBindKey + ",不能转化成ens.");
			}

			ens.RetrieveAllFromDBSource();
			dt = ens.ToDataTableField(uiBindKey);
			return dt;
		} else {

			String sql = "SELECT No,Name FROM " + uiBindKey;
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = uiBindKey;
			return dt;
		}
	}

	/**
	 * 获取数据源
	 * 
	 * @param uiBindKey
	 *            绑定的外键或者枚举
	 * @return
	 */
	public static DataTable GetDataTableByUIBineKeyForCCFormDesigner(String uiBindKey) {
		int topNum = 40;

		DataTable dt = new DataTable();
		if (uiBindKey.contains(".")) {
			Entities ens = BP.En.ClassFactory.GetEns(uiBindKey);
			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}

			if (ens == null) {
				ens = BP.En.ClassFactory.GetEns(uiBindKey);
			}
			if (ens == null) {
				throw new RuntimeException("类名错误:" + uiBindKey + ",不能转化成ens.");
			}

			BP.En.QueryObject qo = new QueryObject(ens);
			return qo.DoQueryToTable(topNum);
		} else {
			String sql = "";

			switch (BP.Sys.SystemConfig.getAppCenterDBType()) {
			case Oracle:
				sql = "SELECT No,Name FROM " + uiBindKey + " where rowNum <= " + topNum;
				break;
			case MSSQL:
				sql = "SELECT top " + topNum + " No,Name FROM " + uiBindKey;
				break;
			default:
				sql = "SELECT  No,Name FROM " + uiBindKey;
				break;
			}
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			dt.TableName = uiBindKey;
			return dt;
		}
	}

	// 系统调度
	public static String GenerDBOfOreacle() {
		java.util.ArrayList als = ClassFactory.GetObjects("BP.En.Entity");
		String sql = "";
		for (Object obj : als) {
			Entity en = (Entity) obj;
			sql += "IF EXISTS( SELECT name  FROM  sysobjects WHERE  name='" + en.getEnMap().getPhysicsTable() + "') <BR> DROP TABLE "
					+ en.getEnMap().getPhysicsTable() + "<BR>";
			sql += "CREATE TABLE " + en.getEnMap().getPhysicsTable() + " ( <BR>";
			sql += "";
		}
		// DA.Log.DefaultLogWriteLine(LogType.Error,msg.Replace("<br>@","\n") );
		return sql;
	}

	public static String DBRpt(DBCheckLevel level) {
		// 取出全部的实体
		java.util.ArrayList als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		for (Object obj : als) {
			Entities ens = (Entities) obj;
			try {
				msg += DBRpt1(level, ens);
			} catch (RuntimeException ex) {
				msg += "<hr>" + ens.toString() + "体检失败:" + ex.getMessage();
			}
		}

		MapDatas mds = new MapDatas();
		mds.RetrieveAllFromDBSource();
		for (Object md : mds) {
			try {
				try {
					((MapData) md).getHisGEEn().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PubClass.AddComment(((MapData) md).getHisGEEn());
			} catch (RuntimeException ex) {
				msg += "<hr>" + ((MapData) md).getNo() + "体检失败:" + ex.getMessage();
			}
		}

		MapDtls dtls = new MapDtls();
		dtls.RetrieveAllFromDBSource();
		for (Object dtl : dtls) {
			try {
				try {
					((MapDtl) dtl).getHisGEDtl().CheckPhysicsTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PubClass.AddComment(((MapDtl) dtl).getHisGEDtl());
			} catch (RuntimeException ex) {
				msg += "<hr>" + ((MapDtl) dtl).getNo() + "体检失败:" + ex.getMessage();
			}
		}

		// 检查处理必要的基础数据 Pub_Day .
		String sql = "";
		String sqls = "";
		sql = "SELECT count(*) Num FROM Pub_Day";
		try {
			String d = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 1; i <= 31; i++) {

					if (i <= 9)
						d = PubClass.addZeroForNum(String.valueOf(i), 2);
					else
						d = String.valueOf(i);
					sqls += "@INSERT INTO Pub_Day(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		} catch (java.lang.Exception e) {}

		sql = "SELECT count(*) Num FROM Pub_YF";
		try {
			String d1 = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 1; i <= 12; i++) {
					if (i <= 9)
						d1 = PubClass.addZeroForNum(String.valueOf(i), 2);
					else
						d1 = String.valueOf(i);
					sqls += "@INSERT INTO Pub_YF(No,Name)VALUES('" + d1.toString() + "','" + d1.toString() + "')";
				}
			}
		} catch (java.lang.Exception e2) {}

		sql = "SELECT count(*) Num FROM Pub_ND";
		try {
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 2010; i < 2015; i++) {
					String d = (new Integer(i)).toString();
					sqls += "@INSERT INTO Pub_ND(No,Name)VALUES('" + d.toString() + "','" + d.toString() + "')";
				}
			}
		} catch (java.lang.Exception e3) {

		}
		sql = "SELECT count(*) Num FROM Pub_NY";
		try {
			String d2 = "";
			if (DBAccess.RunSQLReturnValInt(sql) == 0) {
				for (int i = 2010; i < 2015; i++) {

					for (int yf = 1; yf <= 12; yf++) {
						if (i <= 9)
							d2 = (new Integer(i)).toString() + "-" + PubClass.addZeroForNum(String.valueOf(i), 2);
						else
							d2 = (new Integer(i)).toString() + "-" + String.valueOf(i);

						sqls += "@INSERT INTO Pub_NY(No,Name)VALUES('" + d2 + "','" + d2 + "')";
					}
				}
			}
		} catch (java.lang.Exception e4) {}

		try {
			DBAccess.RunSQLs(sqls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 检查处理必要的基础数据。
		return msg;
	}

	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			// sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	private static void RepleaceFieldDesc(Entity en) {
		String tableId = DBAccess.RunSQLReturnVal("select ID from sysobjects WHERE name='" + en.getEnMap().getPhysicsTable() + "' AND xtype='U'")
				.toString();

		if (tableId == null || tableId.equals("")) {
			return;
		}

		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

		}
	}

	/**
	 * 为表增加注释
	 * 
	 * @return
	 */
	public static String AddComment() {
		// 取出全部的实体
		java.util.ArrayList als = ClassFactory.GetObjects("BP.En.Entities");
		String msg = "";
		Entity en = null;
		Entities ens = null;
		for (Object obj : als) {
			try {
				ens = (Entities) obj;
				en = ens.getGetNewEntity();
				if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp) {
					continue;
				}
			} catch (java.lang.Exception e) {
				continue;
			}
			msg += AddComment(en);
		}
		return msg;
	}

	public static String AddComment(Entity en) {
		try {
			switch (en.getEnMap().getEnDBUrl().getDBType()) {
			case Oracle:
				AddCommentForTable_Ora(en);
				break;
			default:
				AddCommentForTable_MS(en);
				break;
			}
			return "";
		} catch (RuntimeException ex) {
			return "<hr>" + en.toString() + "体检失败:" + ex.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void AddCommentForTable_Ora(Entity en) throws Exception {
		en.RunSQL("comment on table " + en.getEnMap().getPhysicsTable() + " IS '" + en.getEnDesc() + "'");
		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			switch (attr.getMyFieldType()) {
			case PK:
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + " - 主键'");
				break;
			case Normal:
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + "'");
				break;
			case Enum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ",枚举类型:"
						+ ses.ToDesc() + "'");
				break;
			case PKEnum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ", 主键:枚举类型:"
						+ ses.ToDesc() + "'");
				break;
			case FK:
				Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS " + attr.getDesc() + ", 外键:对应物理表:"
						+ myen.getEnMap().getPhysicsTable() + ",表描述:" + myen.getEnDesc());
				break;
			case PKFK:
				Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				en.RunSQL("comment on column  " + en.getEnMap().getPhysicsTable() + "." + attr.getField() + " IS '" + attr.getDesc() + ", 主外键:对应物理表:"
						+ myen1.getEnMap().getPhysicsTable() + ",表描述:" + myen1.getEnDesc() + "'");
				break;
			default:
				break;
			}
		}
	}

	private static void AddColNote(Entity en, String table, String col, String note) {
		return;
//		try {
//			String sql = "execute  sp_dropextendedproperty 'MS_Description','user',dbo,'table','" + table + "','column'," + col;
//			en.RunSQL(sql);
//		} catch (RuntimeException ex) {} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String sql = "execute  sp_addextendedproperty 'MS_Description', '" + note + "', 'user', dbo, 'table', '" + table + "', 'column', '" + col
//					+ "'";
//			en.RunSQL(sql);
//		} catch (RuntimeException ex) {} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 为表增加解释
	 * 
	 * @param en
	 */
	public static void AddCommentForTable_MS(Entity en) {
		if (en.getEnMap().getEnType() == EnType.View || en.getEnMap().getEnType() == EnType.ThirdPartApp) {
			return;
		}

		try {
			String sql = "execute  sp_dropextendedproperty 'MS_Description','user',dbo,'table','" + en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		} catch (RuntimeException ex) {} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String sql = "execute  sp_addextendedproperty 'MS_Description', '" + en.getEnDesc() + "', 'user', dbo, 'table', '"
					+ en.getEnMap().getPhysicsTable() + "'";
			en.RunSQL(sql);
		} catch (RuntimeException ex) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		SysEnums ses = new SysEnums();
		for (Attr attr : en.getEnMap().getAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}
			if (attr.getKey().equals(attr.getDesc())) {
				continue;
			}

			switch (attr.getMyFieldType()) {
			case Normal:
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc());
				// en.RunSQL("comment on table "+
				// en.EnMap.PhysicsTable+"."+attr.Field
				// +" IS '"+en.EnDesc+"'");
				break;
			case Enum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				// en.RunSQL("comment on table "+
				// en.EnMap.PhysicsTable+"."+attr.Field +" IS '"++"'" );
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ",枚举类型:" + ses.ToDesc());
				break;
			case PKEnum:
				ses = new SysEnums(attr.getKey(), attr.UITag);
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ",主键:枚举类型:" + ses.ToDesc());
				// en.RunSQL("comment on table "+
				// en.EnMap.PhysicsTable+"."+attr.Field
				// +" IS '"+en.EnDesc+", 主键:枚举类型:"+ses.ToDesc()+"'" );
				break;
			case FK:
				Entity myen = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 外键:对应物理表:" + myen.getEnMap().getPhysicsTable()
						+ ",表描述:" + myen.getEnDesc());
				// en.RunSQL("comment on table "+
				// en.EnMap.PhysicsTable+"."+attr.Field +" IS "+ );
				break;
			case PKFK:
				Entity myen1 = attr.getHisFKEn(); // ClassFactory.GetEns(attr.UIBindKey).GetNewEntity;
				AddColNote(en, en.getEnMap().getPhysicsTable(), attr.getField(), attr.getDesc() + ", 主外键:对应物理表:" + myen1.getEnMap().getPhysicsTable()
						+ ",表描述:" + myen1.getEnDesc());
				// en.RunSQL("comment on table "+
				// en.EnMap.PhysicsTable+"."+attr.Field +" IS '"+ );
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 产程系统报表，如果出现问题，就写入日志里面。
	 * 
	 * @return
	 */
	public static String DBRpt1(DBCheckLevel level, Entities ens) {
		Entity en = ens.getGetNewEntity();
		if (en.getEnMap().getEnDBUrl().getDBUrlType() != DBUrlType.AppCenterDSN) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.ThirdPartApp) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.View) {
			return null;
		}

		if (en.getEnMap().getEnType() == EnType.Ext) {
			return null;
		}

		// 检测物理表的字段。
		try {
			en.CheckPhysicsTable();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		PubClass.AddComment(en);

		String msg = "";
		// if (level == DBLevel.High)
		// {
		// try
		// {
		// DBAccess.RunSQL("update pub_emp set AuthorizedAgent='1' WHERE AuthorizedAgent='0' ");
		// }
		// catch
		// {
		// }
		// }
		String table = en.getEnMap().getPhysicsTable();
		Attrs fkAttrs = en.getEnMap().getHisFKAttrs();
		if (fkAttrs.size() == 0) {
			return msg;
		}
		int num = 0;
		String sql;
		// string msg="";
		for (Attr attr : fkAttrs) {
			if (attr.getMyFieldType() == FieldType.RefText) {
				continue;
			}

			String enMsg = "";
			try {
				// 更新他们，去掉左右空格，因为外键不能包含左右空格。
				if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
					// 如果是高中级别,就去掉左右空格
					if (attr.getMyDataType() == DataType.AppString) {
						DBAccess.RunSQL("UPDATE " + en.getEnMap().getPhysicsTable() + " SET " + attr.getField() + " = rtrim( ltrim("
								+ attr.getField() + ") )");
					}
				}

				// 处理关联表的情况.
				Entities refEns = attr.getHisFKEns(); // ClassFactory.GetEns(attr.UIBindKey);
				Entity refEn = refEns.getGetNewEntity();

				// 取出关联的表。
				String reftable = refEn.getEnMap().getPhysicsTable();
				// sql="SELECT COUNT(*) FROM "+en.EnMap.PhysicsTable+" WHERE "+attr.Key+" is null or len("+attr.Key+") < 1 ";
				// 判断外键表是否存在。

				sql = "SELECT COUNT(*) FROM  sysobjects  WHERE  name = '" + reftable + "'";
				// num=DA.DBAccess.RunSQLReturnValInt(sql,0);
				if (!DBAccess.IsExitsObject(reftable)) {
					// 报告错误信息
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",字段 " + attr.getKey() + " , 字段描述:" + attr.getDesc() + " , 外键物理表:" + reftable + "不存在:"
							+ sql;
				} else {
					Attr attrRefKey = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()); // 去掉主键的左右 空格．
					if (attrRefKey.getMyDataType() == DataType.AppString) {
						if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
							// 如果是高中级别,就去掉左右空格
							DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefKey.getField() + " = rtrim( ltrim(" + attrRefKey.getField()
									+ ") )");
						}
					}

					Attr attrRefText = refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyText()); // 去掉主键 Text 的左右 空格．

					if (level == DBCheckLevel.Middle || level == DBCheckLevel.High) {
						// 如果是高中级别,就去掉左右空格
						DBAccess.RunSQL("UPDATE " + reftable + " SET " + attrRefText.getField() + " = rtrim( ltrim(" + attrRefText.getField() + ") )");
					}

				}
				// 外键的实体是否为空
				switch (en.getEnMap().getEnDBUrl().getDBType()) {
				case Oracle:
					sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " is null or length("
							+ attr.getField() + ") < 1 ";
					break;
				default:
					sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " is null or len("
							+ attr.getField() + ") < 1 ";
					break;
				}

				num = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0) {} else {
					enMsg += "<br>@检查实体：" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现" + attr.getKey() + "," + attr.getDesc()
							+ "不正确,共有[" + num + "]行记录没有数据。" + sql;
				}
				// 是否能够对应到外键
				// 是否能够对应到外键。
				sql = "SELECT COUNT(*) FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN ( SELECT "
						+ refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField() + " FROM " + reftable + "	 ) ";
				num = BP.DA.DBAccess.RunSQLReturnValInt(sql, 0);
				if (num == 0) {} else {
					// 如果是高中级别.
					String delsql = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + attr.getField() + " NOT IN ( SELECT "
							+ refEn.getEnMap().GetAttrByKey(attr.getUIRefKeyValue()).getField() + " FROM " + reftable + "	 ) ";
					// int i =DBAccess.RunSQL(delsql);
					enMsg += "<br>@" + en.getEnDesc() + ",物理表:" + en.getEnMap().getPhysicsTable() + "出现" + attr.getKey() + "," + attr.getDesc()
							+ "不正确,共有[" + num + "]行记录没有关联到数据，请检查物理表与外键表。" + sql + "如果您想删除这些对应不上的数据请运行如下SQL: " + delsql + " 请慎重执行.";
				}

				// 判断 主键
				// DBAccess.IsExits("");
			} catch (RuntimeException ex) {
				enMsg += "<br>@" + ex.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!enMsg.equals("")) {
				msg += "<BR><b>-- 检查[" + en.getEnDesc() + "," + en.getEnMap().getPhysicsTable() + "]出现如下问题,类名称:" + en.toString() + "</b>";
				msg += enMsg;
			}
		}
		return msg;
	}

	// 转化格式 chen
	/**
	 * 将某控件中的数据转化为Excel文件
	 * 
	 * @param ctl
	 */
	// public static void ToExcel(System.Web.UI.Control ctl, String filename)
	// {
	// HttpContext.Current.Response.Charset ="GB2312";
	// HttpContext.Current.Response.AppendHeader("Content-Disposition","attachment;filename="+
	// filename +".xls");
	// HttpContext.Current.Response.ContentEncoding
	// =System.Text.Encoding.GetEncoding("GB2312");
	// HttpContext.Current.Response.ContentType ="application/ms-excel";
	// //"application/ms-excel";
	// //image/JPEG;text/HTML;image/GIF;application/ms-msword
	// ctl.Page.EnableViewState =false;
	// System.IO.StringWriter tw = new System.IO.StringWriter();
	// System.Web.UI.HtmlTextWriter hw = new System.Web.UI.HtmlTextWriter (tw);
	// ctl.RenderControl(hw);
	// HttpContext.Current.Response.Write(tw.toString());
	// HttpContext.Current.Response.End();
	// }
	/**
	 * 将某控件中的数据转化为Word文件
	 * 
	 * @param ctl
	 * @throws IOException
	 */
	// public static void ToWord(System.Web.UI.Control ctl, String filename)
	// {
	// filename = HttpUtility.UrlEncode(filename);
	// HttpContext.Current.Response.Charset ="GB2312";
	// HttpContext.Current.Response.AppendHeader("Content-Disposition","attachment;filename="+
	// filename +".doc");
	// HttpContext.Current.Response.ContentEncoding
	// =System.Text.Encoding.GetEncoding("GB2312");
	// HttpContext.Current.Response.ContentType ="application/ms-msword";
	// //image/JPEG;text/HTML;image/GIF;application/ms-excel
	// ctl.Page.EnableViewState =false;
	// System.IO.StringWriter tw = new System.IO.StringWriter();
	// System.Web.UI.HtmlTextWriter hw = new System.Web.UI.HtmlTextWriter (tw);
	// ctl.RenderControl(hw);
	// HttpContext.Current.Response.Write(tw.toString());
	// }

	 public static void OpenExcel(String filepath, String tempName) throws IOException
	 {
		 /*tempName = HttpUtility.UrlEncode(tempName);
		 HttpContext.Current.Response.Charset = "GB2312";
		 HttpContext.Current.Response.AppendHeader("Content-Disposition",
		 "attachment;filename=" + tempName);
		 HttpContext.Current.Response.ContentEncoding =
		 System.Text.Encoding.GetEncoding("GB2312");
		 HttpContext.Current.Response.ContentType = "application/ms-excel";
		 HttpContext.Current.Response.WriteFile(filepath);
		 HttpContext.Current.Response.End();
		 HttpContext.Current.Response.Close();*/
		 
		// 设置文件MIME类型
			HttpServletResponse response = ContextHolderUtils.getResponse();
			HttpServletRequest request = ContextHolderUtils.getRequest();

			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "inline;filename=" + new String(tempName.getBytes("UTF-8"), "ISO-8859-1"));

			BufferedOutputStream bos = null;
			FileInputStream fis = null;
			try {
				File file = new File(filepath);
				//System.out.println("文件路径:"+filepath);
				if (!file.exists()) {
					throw new Exception("找不到指定文件：" + filepath);
				}
				// 开始下载
				fis = new FileInputStream(file);
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] b = new byte[8192];
				int data = 0;
				while ((data = fis.read(b)) != -1) {
					bos.write(b, 0, data);
				}
				// 刷新流
				bos.flush();
			} catch (Exception e) {} finally {
				if (bos != null)
					bos.close();
				if (fis != null)
					fis.close();
			}
	 }

	public static String toUtf8String(String s) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					// exceptionUtil.error("将文件名中的汉字转为UTF8编码的串时错误，输入的字符串为：" +
					// s);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	// 不同浏览器编码问题 add by qin 15.10.20
	public static String toUtf8String(HttpServletRequest request, String s) {
		String agent = request.getHeader("User-Agent");
		try {
			boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
			if (isFireFox) {
				s = new String(s.getBytes("UTF-8"), "ISO8859-1");
			} else {
				s = toUtf8String(s);
				if ((agent != null && agent.indexOf("MSIE") != -1)) {
					// see http://support.microsoft.com/default.aspx?kbid=816868
					if (s.length() > 150) {
						// 根据request的locale 得出可能的编码
						s = new String(s.getBytes("UTF-8"), "ISO8859-1");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void DownloadFile(String filepath, String tempName) throws IOException {

		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		tempName = toUtf8String(request, tempName);

		// String agent = request.getHeader("User-Agent");

		// if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox")
		// > 0)
		// tempName = new String(tempName.getBytes("UTF-8"), "ISO8859-1");//
		// firefox浏览器
		// else if
		// (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
		// // tempName = URLEncoder.encode(tempName, "UTF-8");// IE浏览器
		// tempName = new String(tempName.getBytes("UTF-8"), "ISO8859-1");//
		// firefox浏览器
		// }
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + tempName);
		response.setHeader("Connection", "close");
		// 读取目标文件，通过response将目标文件写到客户端
		// 读取文件
		InputStream in = new FileInputStream(new File(filepath));
		OutputStream out = response.getOutputStream();
		// 写文件
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		in.close();
		out.close();
	}

	//
	// public static void OpenWordDoc(String filepath, String tempName)
	// {
	// tempName = HttpUtility.UrlEncode(tempName);
	//
	// HttpContext.Current.Response.Charset = "GB2312";
	// HttpContext.Current.Response.AppendHeader("Content-Disposition",
	// "attachment;filename=" + tempName);
	// HttpContext.Current.Response.ContentEncoding =
	// System.Text.Encoding.GetEncoding("GB2312");
	// HttpContext.Current.Response.ContentType = "application/ms-msword";
	// //image/JPEG;text/HTML;image/GIF;application/ms-excel
	// //HttpContext.Current.EnableViewState =false;
	// HttpContext.Current.Response.WriteFile(filepath);
	// HttpContext.Current.Response.End();
	// HttpContext.Current.Response.Close();
	// }
	public static void OpenWordDocV2(String filepath, String tempName) throws IOException {
		// 设置文件MIME类型
		HttpServletResponse response = ContextHolderUtils.getResponse();
		HttpServletRequest request = ContextHolderUtils.getRequest();

		response.setContentType("application/msword;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "inline;filename=" + new String(filepath.getBytes("GB2312"), "ISO-8859-1"));

		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				throw new Exception("找不到指定文件：" + filepath);
			}
			// 开始下载
			fis = new FileInputStream(file);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] b = new byte[8192];
			int data = 0;
			while ((data = fis.read(b)) != -1) {
				bos.write(b, 0, data);
			}
			// 刷新流
			bos.flush();
		} catch (Exception e) {} finally {
			if (bos != null)
				bos.close();
			if (fis != null)
				fis.close();
		}
	}

	// public static void To(String url)
	// {
	// System.Web.HttpContext.Current.Response.Redirect(url,true);
	// }
	public static void Print(String url) {
		try {
			ContextHolderUtils
					.getResponse()
					.getWriter()
					.write("<script language='JavaScript'> var newWindow =window.open('"
							+ url
							+ "','p','width=0,top=10,left=10,height=1,scrollbars=yes,resizable=yes,toolbar=yes,location=yes,menubar=yes') ; newWindow.focus(); </script> ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过request 设置Entity值
	 * 
	 * @param en
	 * @param reqest
	 * @return
	 */
	public static BP.En.Entity copyFromRequest(BP.En.Entity en, HttpServletRequest reqest) {
		ArrayList<String> requestKeys = new ArrayList<String>();

		Enumeration enu = reqest.getParameterNames();
		while (enu.hasMoreElements()) {
			// 判断是否有内容，hasNext()
			String key = (String) enu.nextElement();
			requestKeys.add(key);
		}

		// 给每个属性值.
		Attrs attrs = en.getEnMap().getAttrs();
		for (Attr item : attrs) {
			String relKey = null;
			switch (item.getUIContralType()) {
			case TB:
				relKey = "TB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case CheckBok:
				relKey = "CB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case DDL:
				relKey = "DDL_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			case RadioBtn:
				relKey = "RB_" + item.getKey(); // 不要改成小写，否则表单数据无法保存
				break;
			default:
				break;
			}

			if (requestKeys.contains(relKey)) {

				int index = requestKeys.indexOf(relKey);
				// 说明已经找到了这个字段信息。
				String myK = requestKeys.get(index);

				if (myK == null || myK.equals("")) {
					continue;
				}

				if (item.getUIContralType() == UIContralType.CheckBok) {
					String val = reqest.getParameter(myK);
					if (val.equals("on") || val.equals("1")) {
						en.SetValByKey(item.getKey(), 1);
					} else {
						en.SetValByKey(item.getKey(), 0);
					}
				} else {
					String value = reqest.getParameter(myK).trim();
					en.SetValByKey(item.getKey(), value);
				}
				continue;
			} else {
				if (null != relKey && relKey.contains("CB_")) {
					en.SetValByKey(item.getKey(), 0);
				}
			}
		}
		return en;
	}

	public static void WinClose() {
		String clientscript = "<script language='javascript'> window.close(); </script>";
		try {
			ContextHolderUtils.getResponse().setContentType("text/html; charset=utf-8");
			ContextHolderUtils.getResponse().getWriter().write(clientscript);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public static void WinCloseAndReParent(String returnVal)
	// {
	// String clientscript =
	// "<script language='javascript'> window.opener.location.reload(); window.close(); </script>";
	// System.Web.HttpContext.Current.Response.Write(clientscript);
	// }
	// public static void WinClose()
	// {
	// System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'>  window.close(); </script> ");
	// }
	// public static void Open(String url)
	// {
	// //
	// System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'> newWindow =window.open('"
	// + url + "','" + winName + "','width=" + width + ",top=" + top +
	// ",scrollbars=yes,resizable=yes,toolbar=false,location=false') ; newWindow.focus(); </script> ");
	// System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'> var newWindow =window.open('"
	// + url + "','p' ) ; newWindow.focus(); </script> ");
	// }
	// public static void WinReload()
	// {
	// System.Web.HttpContext.Current.Response.Write("<script language='JavaScript'>window.parent.main.document.location.reload(); </script> ");
	// }
	public static void WinOpen(HttpServletResponse response, String url) throws IOException {
		java.text.DateFormat df = new java.text.SimpleDateFormat("MMddHHmmss");
		PubClass.WinOpen(response, url, "", "msg" + df.format(new java.util.Date()), 300, 300);
	}

	public static void WinOpen(HttpServletResponse response, String url, int w, int h) throws IOException {
		java.text.DateFormat df = new java.text.SimpleDateFormat("MMddHHmmss");
		PubClass.WinOpen(response, url, "", "msg" + df.format(new java.util.Date()), w, h);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, String winName, int width, int height) throws IOException {
		PubClass.WinOpen(response, url, title, winName, width, height, 100, 200);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, int width, int height) throws IOException {
		PubClass.WinOpen(response, url, title, "ActivePage", width, height, 100, 200);
	}

	public static void WinOpen(HttpServletResponse response, String url, String title, String winName, int width, int height, int top, int left)
			throws IOException {
		url = url.replace("<", "[");
		url = url.replace(">", "]");
		url = url.trim();
		title = title.replace("<", "[");
		title = title.replace(">", "]");
		title = title.replace("\"", "‘");
		if (top == 0 && left == 0) {
			response.getWriter().write(
					"<script language='JavaScript'> var newWindow =window.open('" + url + "','" + winName + "','width=" + width + ",top=" + top
							+ ",scrollbars=yes,resizable=yes,toolbar=false,location=false') ; </script> ");
			response.getWriter().flush();
		} else {
			response.getWriter().write(
					"<script language='JavaScript'> var newWindow =window.open('" + url + "','" + winName + "','width=" + width + ",top=" + top
							+ ",left=" + left + ",height=" + height + ",scrollbars=yes,resizable=yes,toolbar=false,location=false');</script>");
			response.getWriter().flush();
		}
	}

	// /**
	// 输出到页面上红色的警告。
	//
	// @param msg 消息
	// */
	// protected final void ResponseWriteRedMsg(String msg)
	// {
	// //this.Response.Write("<BR><font color='red' size='"+MsgFontSize.ToString()+"' > <b>"+msg+"</b></font>");
	// //if (msg.Length < 200)
	// // return ;
	// msg = msg.replace("@", "<BR>@");
	// System.Web.HttpContext.Current.Session["info"] = msg;
	// String url = "/WF/Comm/Port/ErrorPage.jsp";
	// WinOpen(url, "警告", msg + new java.util.Date().ToString("mmss"), 500, 400,
	// 150, 270);
	// }
	// /**
	// 输出到页面上蓝色的信息。
	//
	// @param msg 消息
	// */
	public static void ResponseWriteBlueMsg(String msg) throws Exception {
		//
		// if (SystemConfig.getIsBSsystem())
		// {
		// msg=msg.replace("@","<BR>@");
		// System.Web.HttpContext.Current.Session["info"]=msg;
		// String url= "/WF/Comm/Port/InfoPage.jsp";
		// WinOpen(url, "信息", "sysmsg", 500, 400, 150, 270);
		// }
		// else
		// {
		// Log.DebugWriteInfo(msg);
		// }
		throw new Exception("");
	}

	// /**
	// 保存成功
	//
	// */
	// public static void ResponseWriteBlueMsg_SaveOK()
	// {
	// //this.Alert("保存成功!");
	//
	// ResponseWriteBlueMsg("保存成功!");
	// }
	// /**
	// ResponseWriteBlueMsg_DeleteOK
	//
	// */
	// public static void ResponseWriteBlueMsg_DeleteOK()
	// {
	// //this.Alert("删除成功!");
	// ResponseWriteBlueMsg("删除成功!");
	// }
	// /**
	// ResponseWriteBlueMsg_UpdataOK
	//
	// */
	// public static void ResponseWriteBlueMsg_UpdataOK()
	// {
	// // this.Alert("更新成功!");
	// ResponseWriteBlueMsg("更新成功!");
	// }
	// /**
	// 输出到页面上黑色的信息。
	//
	// @param msg 消息
	// */
	// public static void ResponseWriteBlackMsg(String msg)
	// {
	// System.Web.HttpContext.Current.Response.Write("<font color='Black' size=5 ><b>"+msg+"</b></font>");
	// }
	// public static void ResponseSript(String Sript)
	// {
	// System.Web.HttpContext.Current.Response.Write(Sript);
	// }
	// public static void ToSignInPage()
	// {
	// System.Web.HttpContext.Current.Response.Redirect(BP.Sys.Glo.getRequest().ApplicationPath+"/SignIn.jsp?url=/Wel.jsp");
	// }
	// public static void ToWelPage()
	// {
	// System.Web.HttpContext.Current.Response.Redirect(BP.Sys.Glo.getRequest().ApplicationPath+"/Wel.jsp");
	// }
	/**
	 * 切换到信息也面。
	 * 
	 * @param mess
	 */
	public static void ToErrorPage(String mess, HttpServletResponse response) {
		BP.Sys.Glo.getRequest().getSession().setAttribute("info", mess);
		try {
			response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.jsp?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切换到信息也面。
	 * 
	 * @param mess
	 */
	public static void ToMsgPage(String mess, HttpServletResponse response) {
		mess = mess.replace("@", "<BR>@");
		BP.Sys.Glo.getRequest().getSession().setAttribute("info", mess);
		try {
			response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /**
	// 转到一个页面上。 '_top'
	//
	// @param mess
	// @param target '_top'
	// */
	// public static void ToErrorPage(String mess, String target)
	// {
	// System.Web.HttpContext.Current.Session["info"]=mess;
	//
	// String path = BP.Sys.Glo.getRequest().ApplicationPath;
	// if (path.equals("/") || path.equals(""))
	// {
	// path = "";
	// }
	//
	// System.Web.HttpContext.Current.Response.Redirect(path+"Comm/Port/InfoPage.jsp target='_top'");
	// }
	// public static void AlertSaveOK()
	// {
	// "保存成功";
	// }

	/**
	 * 不用page 参数，show message
	 * 
	 * @param mess
	 */
	public static void Alert(String mess, HttpServletResponse response) {
		String script = "<script language=JavaScript>alert('" + mess + "');</script>";
		try {
			response.setCharacterEncoding("gbk");
			response.getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ResponseWriteScript(String script) {
		script = "<script language=JavaScript> " + script + "</script>";
		try {
			ContextHolderUtils.getResponse().getWriter().write(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}