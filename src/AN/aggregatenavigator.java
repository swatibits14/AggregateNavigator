package AN;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class aggregatenavigator{

   static  final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/aggregatenavigator";
   static final String USER = "root";
   static final String PASS = "root";
  
   static ArrayList<String> allfields= new ArrayList<String>();
   static ArrayList<TableInfo> tf=new ArrayList<TableInfo>();
    public static void main(String args[])
    {
    	String s;
    	 s="SELECT d.Store_Name,d.Store_No FROM dimstores d,store s WHERE d.srore_key=f.Store_key GROUP BY d.sd ;"; 
    	 QueryParser qp=new QueryParser(s);
    	 qp.parsequery();
    	 int countdim=0;
    	 BufferedReader reader;
    	 try {
				reader = new BufferedReader(new FileReader("C:\\Users\\swatisharma\\workspace\\AN\\src\\AN\\metadata.txt"));
				
				String lines = reader.readLine();
				while(lines!=null)
				{
					String delims = "[ (,]+";
					String[] line = lines.split(delims);
					TableInfo df = new TableInfo();
		    		 df.table_name=line[0];
		    		 
		    		 if(df.table_name.contains("dim"))
		     		{
		     			df.Type="dimension";
		     			df.noofkeys=1;
		     			countdim++;
		     		}
		    	    if(df.table_name.contains("fact_table"))
			     	{
			     			df.Type="fact";
			     			df.noofkeys=countdim;
			     	}
		     		if(df.table_name.contains("aggdim"))
		     		{
		     			df.Type="agg_dim";
		     			df.noofkeys=1;
		     		}
		     		if(df.table_name.contains("agg1"))
		     		{
		     			df.Type="aggfact1";
		     			df.noofkeys=countdim;
		     		}
		     		if(df.table_name.contains("agg2"))
		     		{
		     			df.Type="aggfact2";
		     			df.noofkeys=countdim;
		     		}
		     		if(df.table_name.contains("agg3"))
		     		{
		     			df.Type="aggfact3";
		     			df.noofkeys=countdim;
		     		}
		    		 if(df.Type=="dim"||df.Type=="agg_dim")
		    		 {
		    			 
						df.key.add(line[1]);
		    			 
		    			 for(int j=2;j<line.length;j++)
						{
						df.Columns.add(line[j]);
						}
		    			 df.no_of_columns=line.length-2;
		    	    }
		    		 else
		    		 {
		    			 for(int i=1;i<df.noofkeys;i++)
		    			 {
						df.key.add(line[i]);
		    			 }
		    			 for(int j=df.noofkeys;j<line.length;j++)
						{
						df.Columns.add(line[j]);
						}
		    			 df.no_of_columns=line.length-df.noofkeys-1;
		    	    }
		    		/*	 
			        try{
			        	 Connection conn = null;
				     	 Statement stmt = null;
			        	 String j="select count(*) y from ";
			        	 j.concat(line[0]);
			    	      Class.forName("com.mysql.jdbc.Driver");
			    	      System.out.println("Connecting to database...");
			    	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
			    	      stmt = conn.createStatement();
			    	      ResultSet rs = stmt.executeQuery(j);
					int x= rs.getInt("y");
					df.no_of_rows=x;
			         	rs.close();
		    	       stmt.close();
		    	       conn.close();
		    	   }catch(SQLException se){
		    		      se.printStackTrace();
		    		   }catch(Exception e){
		    		      e.printStackTrace();
		    		   }*/
			    	tf.add(df);
					lines=reader.readLine();
					
				}
			}catch (FileNotFoundException e) {
				System.out.println("File NOT Found");
				e.printStackTrace();
			}
			catch(IOException e)
			{
				System.out.print("IO Exception");
			}
    	
    	 
    	// int count=0;
    	 ArrayList<CorrespondingAggregate> csa=new ArrayList<CorrespondingAggregate>();
    	for(TableInfo d: tf)
    	{
    		if(d.Type=="Dim")
    		{
    			
    			for(TableInfo g: tf)
    	    	{
    			
    			if(g.Type=="agg_dim")
    			{ 
    				
    				for(String gh:g.Columns)
    				if(d.Columns.contains(gh))
    				{
    					CorrespondingAggregate ca=new CorrespondingAggregate();
    					ca.s=d;
    					ca.t=g;
    					csa.add(ca);
    				}
    			}
    	   }
    			//count++;
    		}
    	}
    	String Tablename=null;
    	int c=0;
    	for(AliasField sg:qp.compares)
    	{
 	      for(TableAlias q : qp.fromstmt)
 			if(q.Alias.equals(sg.Alias))
 					{
 					Tablename=q.Table_name;
 				      break;
 					}
 	      for(TableInfo as :tf)
 	      {
 	    	 if(as.table_name.equals(Tablename))
 	    		if(as.Type=="Dim")
 	    		{
 	    			for(CorrespondingAggregate km: csa)
 	    			{
 	    				if(km.s.table_name.equals(as.table_name))
 	    				{
 	    					for(String za : as.Columns)
 	    					{
 	    						
 	    						if(za.equals(sg.Field_name))
 	    						{
 	    							km.s.flag=false;
 	    							km.t.flag=true;
 	    							break;
 	    						}
 	    						c++;
 	    					}
 	    					if(c==as.no_of_columns)
 	    					{
 	    						km.t.flag=false;
 	    						km.s.flag=true;
 	    					}	
 	    					
 	    				}
 	    			}
 	    		}
 	      }
 							
 			
 		}
 	   Boolean[] ar={false};
    	int o=0;
    
    	for(TableInfo g: tf)
    	{
    		if(g.Type=="Agg_dim")
    		{
    			ar[o]=g.flag;
    		}
    		o++;
    	}
 	    String f;
 	    if((ar[0]==false) && (ar[1]==false )&&(ar[2]==true))
 	    {
 	    	f=s.replaceAll("Dimtime", "DimMonth");
 	    	f=f.replaceAll("time_key", "month_key");
 	    	f=f.replaceAll("salesfacttable", "Aggregate_by_month");
 	    	System.out.println(f);
 	    }
 	   if((ar[0]==false) &&(ar[1]==true)&&(ar[2]==false))
	    {
 		  f=s.replaceAll("Dimstore", "DimRegion");
	    	f=s.replaceAll("store_key", "region_key");
	    	f=f.replaceAll("salesfacttable", "Aggregate_by_region");
	    	System.out.println(f);
	    }
 	  if((ar[0]==true) && (ar[1]==false)&&(ar[2]==false))
	    {
 		 f=s.replaceAll("Dimproduct", "DimCat");
	    	f=s.replaceAll("product_key", "Category_key");
	    	f=f.replaceAll("salesfacttable", "Aggregate_by_cat");
	    	System.out.println(f);
	    }
 	 if((ar[0]==false) && (ar[1]==true)&&(ar[2]==true))
	    {
 		f=s.replaceAll("Dimtime", "DimMonth");
	    	f=s.replaceAll("time_key", "month_key");
	    	f=s.replaceAll("Dimstore", "DimRegion");
	    	f=s.replaceAll("store_key", "region_key");
	    	f=f.replaceAll("salesfacttable", "Aggregate_by_region_month");
	    	System.out.println(f);
 		
	    }
 	 if((ar[0]==true) && (ar[1]==false )&&(ar[2]==true))
	    {
 		f=s.replaceAll("Dimtime", "DimMonth");
	    	f=s.replaceAll("time_key", "month_key");
	    	f=s.replaceAll("Dimproduct", "DimCat");
	    	f=s.replaceAll("product_key", "Category_key");
	    	f=f.replaceAll("salesfacttable", "Aggregate_by_cat_month");
	    	System.out.println(f);
	    }
 	 if((ar[0]==true) && (ar[1]==true )&&(ar[2]==false))
	    {
 		f=s.replaceAll("Dimstore", "DimRegion");
    	f=s.replaceAll("store_key", "region_key");
    	f=s.replaceAll("Dimproduct", "DimCat");
    	f=s.replaceAll("product_key", "Category_key");
    	f=f.replaceAll("salesfacttable", "Aggregate_by_cat_region");
    	System.out.println(f);
	    }
 	 if((ar[0]==true) && (ar[1]==true )&&(ar[2]==true))
	    {
 		f=s.replaceAll("Dimstore", "DimRegion");
    	f=s.replaceAll("store_key", "region_key");
    	f=s.replaceAll("Dimproduct", "DimCat");
    	f=s.replaceAll("product_key", "Category_key");
    	f=s.replaceAll("Dimtime", "DimMonth");
    	f=s.replaceAll("time_key", "month_key");
    	f=f.replaceAll("salesfacttable", "Aggregate_by_cat_region_month");
    	System.out.println(f);
	    }
	   
 	 }
}