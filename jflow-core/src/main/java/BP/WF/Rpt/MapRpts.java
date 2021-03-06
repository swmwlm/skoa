package BP.WF.Rpt;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;
import BP.Sys.Frm.MapData;

/** 
 报表设计s
*/
public class MapRpts extends EntitiesMyPK
{
		///#region 构造
	/** 
	 报表设计s
	*/
	public MapRpts()
	{
	}
  
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapRpt();
	}
		///#endregion

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapRpt> ToJavaList()
	{
		return (List<MapRpt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapRpt> Tolist()
	{
		ArrayList<MapRpt> list = new ArrayList<MapRpt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapRpt)this.get(i));
		}
		return list;
	}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}