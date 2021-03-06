package BP.Sys.Frm;

/**
 * 应用类型
 */
public enum AppType
{
	/**
	 * 流程表单
	 */
	Application(0),
	/**
	 * 节点表单
	 */
	Node(1);
	
	private int intValue;
	private static java.util.HashMap<Integer, AppType> mappings;
	
	private synchronized static java.util.HashMap<Integer, AppType> getMappings()
	{
		if (mappings == null)
		{
			mappings = new java.util.HashMap<Integer, AppType>();
		}
		return mappings;
	}
	
	private AppType(int value)
	{
		intValue = value;
		AppType.getMappings().put(value, this);
	}
	
	public int getValue()
	{
		return intValue;
	}
	
	public static AppType forValue(int value)
	{
		return getMappings().get(value);
	}
}