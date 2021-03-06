package BP.WF.Data;

import java.util.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 报表集合
*/
public class MyDeptEmps extends BP.En.EntitiesNoName
{
	/** 
	 报表集合
	*/
	public MyDeptEmps()
	{
	}

	@Override
	public Entity getGetNewEntity()
	{
		return new MyDeptEmp();
	}
	@Override
	public int RetrieveAll()
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(MyDeptEmpAttr.FK_Dept, BP.Web.WebUser.getFK_Dept());
		return qo.DoQuery();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MyDeptEmp> ToJavaList()
	{
		return (List<MyDeptEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MyDeptEmp> Tolist()
	{
		ArrayList<MyDeptEmp> list = new ArrayList<MyDeptEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MyDeptEmp)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}