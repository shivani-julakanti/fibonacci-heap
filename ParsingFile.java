import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ParsingFile
{
	public static void main(String[] args) throws IOException 
	{
		try{
				FileInputStream f = new FileInputStream(args[0]);
				PrintStream out = new PrintStream(new FileOutputStream("output_file.txt"));
				BufferedReader br = new BufferedReader(new InputStreamReader(f));
				FibonacciHeap H=new FibonacciHeap();
				String line;
				while((line=br.readLine())!=null)
				{
					String t[]=line.split(" ");
					if(t[0].equalsIgnoreCase("STOP"))
					{
						break;
					}
					if(t.length==1 && t[0]!="STOP")
					{
						int n=Integer.parseInt(t[0]);
						List<FHeapNode> tmp = new ArrayList<FHeapNode>();
						for(int j=1;j<=n;j++)
						{
							FHeapNode a=H.removeMax(); 
							if(j<n)
							{									//writing element on output file
								out.print(a.gethashtag());
								out.print(",");
							}
							else if(j==n)
							{
								out.print(a.gethashtag());
								out.println();
							}
							tmp.add(a);											
						}				     
						for(FHeapNode y:tmp)
						{
							H.insert(y.gethashtag(),y.getKey());						
						}
						
					}
					if(t.length>1)
					{
						String hashtag=t[0].substring(1, t[0].length());				//string hashtag
						int frequency=Integer.parseInt(t[1]);
												
						FHeapNode result= H.insert(hashtag,frequency);					//Case 1- node is not present in hashmap
						
						if(result!=null)
						{
							H.increaseKey(result, frequency);
						}					 
					}							
				}
				br.close();
				out.close();
			}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
