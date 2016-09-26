package AN;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class QueryParser 
{
   public String query;
   String RegExFieldNames1 ="SELECT(.*)FROM(.*)WHERE(.*)GROUP BY(.*)";
   String RegExFieldNames2 ="WHERE(.*)GROUP BY(.*)";
   public ArrayList<TableAlias> fromstmt=new ArrayList<TableAlias>();
   public ArrayList<AliasField> selectstmt=new  ArrayList<AliasField>();
   public  ArrayList<AliasField> groupbystmt=new ArrayList<AliasField>();
   public  ArrayList<AliasField> wherestmt =new ArrayList<AliasField>();
   public ArrayList<AliasField> compares =new ArrayList<AliasField>();
   
public QueryParser(String sd)
   {
    query=sd;

   }
    void parsequery()
    {
    	Pattern p = Pattern.compile(RegExFieldNames1);
    	Pattern q = Pattern.compile(RegExFieldNames2);
		Matcher m = p.matcher(query);
		Matcher n = q.matcher(query);
		String selectlist = null,fromlist=null,wherelist=null,groupbylist=null;
		if (m.find())
		{

		   selectlist=m.group(1);
		   selectlist=selectlist.trim();
		   fromlist=m.group(2);
		   fromlist=fromlist.trim();
		}
		if (n.find())
		{

		   wherelist=n.group(1);
		   wherelist=wherelist.trim();
		   groupbylist=n.group(2);
		   groupbylist=groupbylist.replace(';', ' ');
		   groupbylist=groupbylist.trim();
		   
		}
    	System.out.println(selectlist);
    	System.out.println(fromlist);
    	System.out.println(wherelist);
    	System.out.println(groupbylist);
    	
    	String[] a=fromlist.split(",");
    	for(String k:a)
    	{
    		String[] w=k.split(" ");
    		TableAlias l=new TableAlias();
    		l.Table_name=w[0];
    		l.Alias=w[1];
    		fromstmt.add(l);
    	}
    	String delimiters = "OR|AND|,";
    	String[] b=wherelist.split(delimiters);
    	for(String k:b)
    	{
    		String deli="=|>=|<=|<>|>|<|+|-|*|/";
    		String[] w=k.split(deli);
    		for(String u:w)
    			{
    				u=u.trim();
    				String[] h=u.split(".");
    				AliasField l=new AliasField();
    				l.Alias=h[0];
    				l.Field_name=h[1];
    				wherestmt.add(l);
    			}
    	}
    	String[] c=groupbylist.split(",");
    	for(String k:c)
    	{
    		k=k.trim();
			String[] h=k.split(".");
			AliasField l=new AliasField();
			l.Alias=h[0];
			l.Field_name=h[1];
			groupbystmt.add(l);
    	}
    	String[] d=selectlist.split(",");
    	for(String k:d)
    	{
    		k=k.trim();
			String[] h=k.split(".");
			AliasField l=new AliasField();
			l.Alias=h[0];
			l.Field_name=h[1];
			selectstmt.add(l);
    	}
    	for(AliasField x: wherestmt)
    	compares.add(x);
    	for(AliasField x: groupbystmt)
        	compares.add(x);
    }
/*
static String replace(String str, String pattern, String replace) 
{
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e + pattern.length();
	    }
	    result.append(str.substring(s));
	    return result.toString();
	}
*/
}

