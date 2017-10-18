import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class homework
{
	static class union
	{
		int i;
		int j;
		int count;
		char c;
		union(int i, int j, int count, char c)
		{
			this.i = i;
			this.j = j;
			this.count = count;
			this.c = c;
		}
		union()
		{
			this.i = 0;
			this.j = 0;
			this.count = 0;
			this.c = '0';
		}
	}
	
	public static int dfs(char[][] board, boolean[][] visited, int i, int j, int n, char c)
	{
		int count = 1;
		if( i > 0 && board[i-1][j] == c && !visited[i-1][j] )
		{
			visited[i-1][j] = true;
			count += dfs(board, visited, i-1, j, n, c);
		}
		if(i < n-1 && board[i+1][j] == c && !visited[i+1][j])
		{
			visited[i+1][j] = true;
			count += dfs(board, visited, i+1, j, n, c);
		}
		if(j > 0 && board[i][j-1] == c && !visited[i][j-1])
		{
			visited[i][j-1] = true;
			count += dfs(board, visited, i, j-1, n, c);
		}
		if(j < n-1 && board[i][j+1] == c && !visited[i][j+1])
		{
			visited[i][j+1] = true;
			count += dfs(board, visited, i, j+1, n, c);
		}
		return count;
	}
	
	public static List<union> getUnion(int n, int p, double t, char[][] board)
	{
		List<union> res = new ArrayList<union>();
		boolean[][] visited = new boolean[n][n];
		
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(board[i][j] != '*' && !visited[i][j])
				{
					char c = board[i][j];
					visited[i][j] = true;
					int num = dfs(board, visited, i, j, n, c );
					res.add(new union(i, j, num, c));
				}
			}
		}
		
		Comparator<union> cmp = new Comparator<union>()
		{

			@Override
			public int compare(union u1, union u2) 
			{
				// TODO Auto-generated method stub
				return u2.count - u1.count;
			}
	
		};
		Collections.sort(res, cmp );
		
		
//		System.out.println("The union we get >>>>>> ");
//		for(union uni:res)
//		{
//			System.out.println(uni.i+" "+uni.j+":  "+uni.c+"  "+uni.count);
//		}
		return res;
	}
	
	
	public static void sign(char[][] board, boolean[][] visited, int n)
	{
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(visited[i][j] == true)
				{
					board[i][j] = '*';
				}
			}
		}
	}
	
	public static void falldown(char[][] board, int n)
	{
		for(int j = 0; j < n; j++)
		{
			int count = n-1;
			for(int i = n-1; i >= 0; i--)
			{
				if(board[i][j] != '*') 
				{
					board[count][j] = board[i][j];
					count--;
				}
			}
			for(int i = count; i >= 0 ; i--)
			{
				board[i][j] = '*';
			}
		}
		
	}
	
	
	public static void choose(union u, int n, char[][] board)
	{
		boolean[][] visited = new boolean[n][n];
		visited[u.i][u.j] = true;
		dfs(board, visited, u.i, u.j, n, board[u.i][u.j]);
		sign(board, visited, n);
		falldown(board, n);
	}
	
	
	public static union greedySol(List<union> list, int n, int p, double t, char[][] board)
	{
		union max = list.get(0);
		choose(max, n, board);
		return max;
	}
	
	public static void printBoard(int n, char[][] board)
	{
		System.out.println("");
		for(int i = 0; i < n; i++)
		{
			
			for(int j = 0; j < n; j++)
			{
				System.out.print(board[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	
	public static union level2Sol(List<union> list, int n, int p, double t, char[][] board)
	{
		union res = new union();
		int max = Integer.MIN_VALUE;
		if(list.size() == 1) 
		{
			res = list.get(0);
			choose(res, n, board);
			return res;
		}
		
		for(union u:list)
		{
			int count = u.count * u.count;
			if(count < max) break;
			char[][] child = new char[n][n];
			for(int i = 0; i < n; i++)
			{
				for(int j = 0; j < n; j++)
				{
					child[i][j] = board[i][j];
				}
			}
			
			choose(u, n, child);
			//printBoard(n, child);
			List<union> next_level = getUnion(n, p, t, child);
			int m = next_level.get(0).count;
			count -= m * m;
			//System.out.println("Level1: u:"+u.count+"    m:"+m  + "   count:" + count + "    max:" +max );
			if(count > max) 
			{
				max = count;
				res = u;
			}
			
		}
		
		choose(res, n, board);
		return res;
	}
	
	public static union level3Sol(List<union> list, int n, int p, double t, char[][] board)
	{
		union res = new union();
		int max = Integer.MIN_VALUE;
		if(list.size() == 1) 
		{
			res = list.get(0);
			choose(res, n, board);
			return res;
		}
		
		
		for(union u:list)
		{
			int count = u.count * u.count;
			if(count < max) break;
			char[][] child = new char[n][n];
			for(int i = 0; i < n; i++)
			{
				for(int j = 0; j < n; j++)
				{
					child[i][j] = board[i][j];
				}
			}
			
			choose(u, n, child);
			//printBoard(n, child);
			List<union> next_level = getUnion(n, p, t, child);
			union next = level2Sol(next_level, n, p, t, child);
			count -= next.count*next.count;
//			System.out.println("Level2: We choose  i: " + u.i +"  j:"+ u.j + "  count:" + u.count);
//			System.out.println("Next choose  i: "+next.i +"  j:" +next.j  + "  count:" + next.count );
//			System.out.println("diff: " +count +"  max:"+ max);
			if(count > max) 
			{
				max = count;
				res = u;
			}
			
		}
		
		choose(res, n, board);
		
		
		return res;
	}
	
	public static union level4Sol(List<union> list, int n, int p, double t, char[][] board)
	{
		union res = new union();
		int max = Integer.MIN_VALUE;
		if(list.size() == 1) 
		{
			res = list.get(0);
			choose(res, n, board);
			return res;
		}
		
		
		for(union u:list)
		{
			int count = u.count * u.count;
			if(count < max) break;
			char[][] child = new char[n][n];
			for(int i = 0; i < n; i++)
			{
				for(int j = 0; j < n; j++)
				{
					child[i][j] = board[i][j];
				}
			}
			
			choose(u, n, child);
			//printBoard(n, child);
			List<union> next_level = getUnion(n, p, t, child);
			union next = level3Sol(next_level, n, p, t, child);
			count -= next.count*next.count;
//			System.out.println("Level3: We choose  i: " + u.i +"  j:"+ u.j + "  count:" + u.count);
//			System.out.println("Next choose  i: "+next.i +"  j:" +next.j  + "  count:" + next.count );
//			System.out.println("diff: " +count +"  max:"+ max);
			if(count > max) 
			{
				max = count;
				res = u;
			}
			long tmpTime = System.currentTimeMillis();
			int intern = (int)(tmpTime - startTime);
			//System.out.println("Time:"+intern);
			if(intern  > 200 *t) break;
			
		}
		
		choose(res, n, board);
		
		
		return res;
	}
	
	
	static long startTime;
	
	public static void main(String[] args)
	{
		startTime = System.currentTimeMillis();
		//System.out.println("test");
		File file_input = new File("input.txt");
		int n = 0;
		int p = 0;
		double t = 0.0;
		char[][] board = null;
		try
		{
			FileReader fr = new FileReader(file_input);
			BufferedReader br = new BufferedReader(fr);
			n = Integer.parseInt(br.readLine().trim());
			p = Integer.parseInt(br.readLine().trim());
			t = Double.parseDouble(br.readLine().trim());
			board = new char[n][n];
			for(int i = 0; i < n; i++)
			{
				String s = br.readLine();
				board[i] = s.toCharArray();
			}
			br.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(n);
		//System.out.println(p);
		//System.out.println(t);
		//printBoard(n,board);
		
		//t = 200;
		
		
		List<union> union_list = getUnion(n, p, t, board);
		union res = new union();
		//
		//if(t < 60)
		// pruning[0] alpha, pruning[1] beta
		
		if(union_list.size() == 0) 
		{
			
		}
		if(t < 10 || union_list.size() == 1 )
		{
			//System.out.println("Method: Greedy");
			res = greedySol(union_list, n, p, t, board);
		}
		else if( t < 50 || union_list.size() == 2)
		{
			//System.out.println("Method: Two level");
			res = level2Sol(union_list, n, p, t, board);
		}
		else if( t < 150  )
		{
			//System.out.println("Method: Three level");
			res = level3Sol(union_list, n, p, t, board);
		}
		else
		{
			//System.out.println("Method: Four level");
			res = level4Sol(union_list, n, p, t, board);
		}	
		
//		System.out.println("result:   ");
//		System.out.println(res.i + "  " +res.j + " "+ res.count);
//		printBoard(n, board);
		char pos1 = (char) (res.j + 'A');
		int pos2 = res.i + 1;
		String pos = String.valueOf(pos1)+String.valueOf(pos2);
		
		StringBuffer sb = new StringBuffer();
		sb.append(pos);
		sb.append("\r\n");
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				sb.append(board[i][j]);
			}
			sb.append("\r\n");
		}
		
		//System.out.println(sb.toString());
		File file_output = new File("output.txt");
		
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
		//long endTime = System.currentTimeMillis();
		//System.out.println("***Time:"  + (int)(endTime - startTime));
		
	}
}
