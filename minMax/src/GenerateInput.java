import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInput 
{
	public static void main(String args[])
	{
		StringBuffer sb = new StringBuffer();
		//System.out.println(sb.toString());
		
		
		int n = 20;
		int p = 6;
		int t = 200;
		sb.append(n);
		sb.append("\r\n");
		sb.append(p);
		sb.append("\r\n");
		sb.append(t);
		sb.append("\r\n");
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				Random r = new Random();
				int num = r.nextInt(p-1);
				sb.append(num);
			}
			sb.append("\r\n");
		}
		
		
		File file_output = new File("input.txt");
		try
		{
			FileWriter fw = new FileWriter(file_output);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.flush();
			bw.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
