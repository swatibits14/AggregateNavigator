package AN;
import java.util.ArrayList;

public class TableInfo {

	public String table_name;
	int no_of_rows;
	public int no_of_columns;
	public ArrayList<String> Columns =new ArrayList<String>();
	public String Type;
	public Boolean flag;
	public int noofkeys;
	public ArrayList<String> key=new ArrayList<String>();
	public TableInfo()
	{
		no_of_rows=0;
		no_of_columns=0;
		flag=false;
	}
		
}
