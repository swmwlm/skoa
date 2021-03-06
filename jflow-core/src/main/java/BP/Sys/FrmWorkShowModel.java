package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;

/** 
 显示格式
 
*/
public enum FrmWorkShowModel
{
	/** 
	 表格
	 
	*/
	Table,
	/** 
	 自由显示
	 
	*/
	Free;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmWorkShowModel forValue(int value)
	{
		return values()[value];
	}
}