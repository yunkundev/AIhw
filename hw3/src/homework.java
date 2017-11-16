import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class homework 
{
	//First-order logic resolution
	public static boolean[] resolution(String[] query, String[] sentence)
	{
		boolean[] res = new boolean[query.length];
//		for(String s:query)
//		{
//			System.out.println(s);
//		}
//		System.out.println();
//		for(String s:sentence)
//		{
//			System.out.println(s);
//		}
		
		
		return res;
	}
	
	public static void main(String[] args)
	{
		
		int query_number = 0;
		int sentence_number = 0;
		String[] query = null;
		String[] sentence = null;
		try 
		{
			File file_in = new File("input.txt");
			FileReader fr = new FileReader(file_in);
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine().trim();
			query_number = Integer.parseInt(s);
			query = new String[query_number];
			for(int i = 0; i < query_number; i++)
			{
				s = br.readLine().trim();
				query[i] = s;
			}
			s = br.readLine().trim();
			sentence_number = Integer.parseInt(s);
			sentence = new String[sentence_number];
			for(int i = 0; i < sentence_number; i++)
			{
				s = br.readLine().trim();
				sentence[i] = s;
			}
			br.close();
			fr.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		

		boolean[] res = resolution(query, sentence);
		
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < res.length; i++)
		{
			if(res[i]) sb.append("TRUE");
			else sb.append("FALSE");
			if(i != res.length-1) sb.append("\r\n");
		}
		//System.out.println(sb.toString());
		try
		{
			File file_out = new File("output.txt");
			FileWriter fw = new FileWriter(file_out);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
			fw.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
}
