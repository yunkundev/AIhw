package hw1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class homework 
{
	private static int[][] sol;
	// inner class interval
	static class interval
	{
		public int start;
		public int end;
		public int row;
		public interval(int i, int j, int k)
		{
			start = i;
			end = j;
			row = k;
		}
	}
	
	// inner class status
	static class status
	{
		public int[][] board;
		public int p;
		public status(int[][] board_now, int step)
		{
			board = board_now;
			p = step;
		}
	}
	
	//Print the board
	private static void printBoard(int[][] board)
	{
		int n = board.length;
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(board[i][j] < 0)
					System.out.print(" "+ board[i][j]);
				else
					System.out.print("  "+ board[i][j]);
			}
			System.out.println("");
		}
		System.out.println("___________________________");
	}
	
	//Give the board a boundry, change the board n*n to (n+2)*(n+2)
	private static int[][] modifyNursey(int[][] nursey)
	{
		int n = nursey.length;
		int[][] new_nursey = new int[n+2][n+2];
		for(int i = 0; i < n+2; i++)
		{
			new_nursey[0][i] = 2;
			new_nursey[n+1][i] = 2;
			new_nursey[i][0] = 2;
			new_nursey[i][n+1] = 2;
		}
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				new_nursey[i+1][j+1] =  nursey[i][j];
			}
		}
		
		return new_nursey;
	}
	
	private static int[][] recoverNursey(int[][] board)
	{
		int n = board.length-2;
		int[][] recover_nursey = new int[n][n];
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(board[i+1][j+1] < 0)
				{
					recover_nursey[i][j] = 0;
				}
				else
				{
					recover_nursey[i][j] =  board[i+1][j+1];
				}
			}
		}
		return recover_nursey;
	}
	
	
	//calculate the interval of board
	public static List<interval> calInterval(int[][] board)
	{
		List<interval> list = new ArrayList<interval>();
		int n = board.length;
		for(int i = 1; i < n-1; i++)
		{
			int count = 0;
			for(int j = 1; j < n; j++)
			{
				if(board[i][j] == 0)
				{
					count++;
				}
				if(board[i][j] == 2)
				{
					if(count > 0)
					{
						interval itv = new interval(j-count, j -1, i);
						list.add(itv);
					}
					count = 0;
				}
			}
		}
		return list;
	}
	
	
	//sign the board with the pick one : -1
	private static void sign(int[][] board, int x, int y)
	{
		int n = board.length;
		// left

		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y] == 2) break;
			else board[x+i][y]--; 
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y-i] == 2) break;
			else board[x+i][y-i]--;
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y+i] == 2) break;
			else board[x+i][y+i]--;
		}
		
	}
	//undo the sign with the pick one: +1
	private static void undosign(int[][] board, int x, int y)
	{
		int n = board.length;
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y] == 2) break;
			else board[x+i][y]++;
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y+i] == 2) break;
			else board[x+i][y+i]++;
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y-i] == 2) break;
			else board[x+i][y-i]++;
		}
	}
	
	//dfs
	private static boolean dfs(int[][] board, List<interval> intervals, int start, int p)
	{
		if(p == 0) 
		{
			sol = recoverNursey(board);
			return true;
		}
		if(start == intervals.size() ) return false;
		if( p > intervals.size() - start ) return false;
		interval itv = intervals.get(start);
		for(int i = itv.start; i <= itv.end; i++)
		{
			if(board[itv.row][i] < 0) continue;
			else
			{
				sign(board, itv.row, i);
				board[itv.row][i] = 1;
				if(dfs(board, intervals,start+1, p-1)) return true;
				board[itv.row][i] = 0;
				undosign(board, itv.row, i);
			}
			
		}
		if(dfs(board, intervals, start + 1, p)) return true;
		return false;
	}
	
	//DFS meothod
	private static boolean methodDFS(int[][] nursey, int n, int p)
	{
		//printBoard(nursey);
		int[][] board = modifyNursey(nursey);
		//printBoard(board);
		List<interval> intervals = calInterval(board);
//		for(interval itv:intervals)
//		{
//			System.out.println(" "+itv.start+" "+itv.end+" "+itv.row);
//		}
		
		
		if(dfs(board, intervals, 0, p))
		{
			return true;
		}
		return false;
		
	}
	
	private static int[][] sign_return(int[][] old_board, int x, int y)
	{
		int n = old_board.length;
		// left
		int[][] board = new int[n][n];
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n;j++)
			{
				board[i][j] = old_board[i][j];
			}
		}
		
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y] == 2) break;
			else board[x+i][y]--; 
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y-i] == 2) break;
			else board[x+i][y-i]--;
		}
		for(int i = 1; i < n; i++)
		{
			if(board[x+i][y+i] == 2) break;
			else board[x+i][y+i]--;
		}
		board[x][y] = 1;
		return board;
		
	}
	
	
	// bfs implement
	private static boolean bfs(int[][] board, List<interval>intervals, int p)
	{
		//int n = board.length;
		List<status> list = new ArrayList<status>();
		List<status> new_list = new ArrayList<status>();
		status root = new status(board, p);
		list.add(root);
		//int count = 0;
		for(int i = 0; i < intervals.size(); i++)
		{
			interval itv = intervals.get(i);
			new_list = new ArrayList<status>();
			//scount ++;
			//if(count > 1) break;
			//System.out.println(itv.start +" "+itv.end+" "+itv.row);
			//System.out.println(list.size());
			for(status s:list)
			{

				int[][] brd = s.board;
				int step = s.p;
				if(step > intervals.size() - i)
				{
					continue;
				}
				if(step < intervals.size() - i)
					new_list.add(new status(brd, step));
				for(int j = itv.start; j <= itv.end; j++ )
				{
					if(brd[itv.row][j] < 0) continue;
					else
					{
						if(step == 1)
						{
							brd[itv.row][j] = 1;
							sol = recoverNursey(brd);
							return true;
						}
						int[][] brd2 = sign_return(brd, itv.row, j);
						status node = new status(brd2, step-1);
						new_list.add(node);
					}
					
				}
				s = null;
				
			}
			list = new_list;
			
		}
		
		return false;
	}
	
	//BFS method
	private static boolean methodBFS(int[][] nursey, int n, int p)
	{
		int[][] board = modifyNursey(nursey);
		List<interval> intervals = calInterval(board);
		
		if(bfs(board, intervals, p))
		{
			return true;
		}
		return false;
	}
	
	
	
	
	private static void initialBoard(int[][] board, List<interval> intervals, int p)
	{
		for(int i = 0; i < p; i++)
		{
			interval itv = intervals.get(i);
			Random rnd = new Random();
			int y = itv.start + rnd.nextInt(itv.end - itv.start + 1);
			board[itv.row][y] = 1;
			
		}
		
		
	}
	
	//calculate the confilct
	private static int calConflict(int[][] board)
	{
		int n = board.length;
		int conflict = 0;
		for(int i = 1; i < n-1; i++)
		{
			int count = 0;
			for(int j = 1; j < n-1;j ++)
			{
				if(board[i][j] == 1)
				{
					count++;
					if(count > 1) 
					{
						conflict ++;
						//System.out.println("row "+i);
					}
				}
				else if(board[i][j] == 2)
				{
					count = 0;
				}
					
				
			}
			
		}
		for(int i =1; i < n-1; i++)
		{
			int count = 0;
			for(int j = 1; j < n-1; j++)
			{
				if(board[j][i] == 1)
				{
					count ++;
					if(count > 1) 
					{
						conflict ++;
						//System.out.println("colm "+i);
					}
				}
				else if(board[j][i] == 2)
				{
					count = 0;
				}
					
			}
		}
		for(int sum = 2; sum <= 2*n - 4; sum++)
		{
			if( sum < n )
			{
				int count  = 0;
				for(int j = 1; j <= sum-1; j++)
				{
					int i = sum - j;
					if(board[i][j] == 1)
					{
						count ++;
						if(count > 1) 
						{
							conflict ++;
							//System.out.println("skew1 "+sum );
						}
					}
					else if(board[i][j] == 2)
					{
						count = 0;
					}
					
				}
			}
			else
			{
				int count = 0;
				for(int j = sum - n + 1; j < n-1; j++)
				{
					int i = sum - j;
					if(board[i][j] == 1)
					{
						count ++;
						if(count > 1) 
						{
							conflict ++;
							//System.out.println("skew1 "+sum );
						}
					}
					else if(board[i][j] == 2)
					{
						count = 0;
					}
				}
			}
			
		}
		
		for(int dif = -n+3; dif <= n-3; dif++)
		{
			if( dif <= 0 )
			{
				int count = 0;
				for(int i = 1; i < n - 1 + dif  ;i++)
				{
					int j = i - dif;
					if(board[i][j] == 1)
					{
						count ++;
						if(count > 1) 
						{
							conflict ++;
							//System.out.println("skew2 "+dif );
						}
					}
					else if(board[i][j] == 2)
					{
						count = 0;
					}
				}
			}
			else
			{
				int count = 0;
				for(int i = dif+1; i < n-1; i++)
				{
					int j = i - dif;
					if(board[i][j] == 1)
					{
						count ++;
						if(count > 1) 
						{
							conflict ++;
							//System.out.println("skew2 "+dif);
						}
					}
					else if(board[i][j] == 2)
					{
						count = 0;
					}
				}
			}
		}
		
		
		return conflict;
	}
	
	
	
	
	private static int[][] oneStep(int[][] board, double T)
	{
		int old_conflict = calConflict(board);
		Random rdm = new Random();
		int n = board.length;
		int x = 0, y =0;
		while(true)
		{
			x = rdm.nextInt(n-2) + 1;
			y = rdm.nextInt(n-2) + 1;
			if(board[x][y] == 1) break;
		}
		int x2 = x, y2 = y;
		int count = 0;
		while(true)
		{
			x2 = rdm.nextInt(n-2) + 1;
			y2 = rdm.nextInt(n-2) + 1;
			if(board[x2][y2] == 0) break;
			if(count ++ > 100) return board;
			
		}
		board[x][y] = 0;
		board[x2][y2] = 1;
		int new_conflict = calConflict(board);
		if(new_conflict == 0) 
		{
			sol = board; 
			return board;
		}
		
		else if(new_conflict < old_conflict)
		{
			//System.out.println("better");
			return board;
			
		}
		else
		{
			double E = (double)new_conflict - old_conflict ;
			if( rdm.nextDouble() < Math.exp((-1)*E/T))
			{
				//System.out.println("accept");
				return board;
				
			}
			else
			{
				board[x][y] = 1;
				board[x2][y2] = 0;
				//System.out.println("Not change");
				return board;
				
			}
			
		}
			
			
		
		
		
	}
	
	
	//SA metohd
	private static boolean methodSA(int[][] nursey, int n, int p)
	{
		int[][] board = modifyNursey(nursey);
		List<interval> intervals = calInterval(board);
		if(p > intervals.size()) return false;
		initialBoard(board, intervals, p);
		
		int step = 0;
		double T = 1;
		//printBoard(board);
		
		while(true)
		{
			if(calConflict(board) == 0) 
			{
				sol = recoverNursey(board);
				return true;
			}
			step ++;
			T = T*0.99;
			//System.out.println("Step  "+ step);
			if(step > Integer.MAX_VALUE) break;
			
			board = oneStep(board,T);
			//printBoard(board);
			//System.out.println(calConflict(board));
			
		}
		return false;
		
	}
	
	
	//main method
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		File fin = new File("input.txt");
		try
		{
			
			FileReader fr = new FileReader(fin);
			BufferedReader br = new BufferedReader(fr);
			String method = br.readLine().trim();
			//System.out.println("We use " + method + " method.");
			String numn = br.readLine().trim();
			int n = Integer.parseInt(numn);
			String nump = br.readLine().trim();
			int p = Integer.parseInt(nump);
			//System.out.println(n  + "  " + p);
			final int[][] 	nursey= new int[n][n];
			for(int i = 0;  i < n; i++)
			{
				String line = br.readLine().trim();
				for(int j = 0; j < n; j++)
				{
					nursey[i][j] = line.charAt(j) - '0';
					//System.out.print(" " + nursey[i][j]);
				}
				//System.out.println(" ");
			}
			
			br.close();
			
			
			Callable<Boolean> task = new Callable<Boolean>()
			{
				public Boolean call() throws Exception
				{
					boolean result = false;
					if(method.equals("DFS"))
					{
						result = methodDFS(nursey, n, p);
						
					}
					if(method.equals("BFS"))
					{
						result = methodBFS(nursey, n, p);
					}
					if(method.equals("SA"))
					{
						result = methodSA(nursey, n, p);
					}
					
					return result;
				}
				
			};
			
			
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			Future<Boolean> future = executorService.submit(task);
			
			boolean result = false;
			try
			{
				result = future.get(295, TimeUnit.SECONDS);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			} catch(TimeoutException e)
			{
				e.printStackTrace();
			} catch(ExecutionException e)
			{
				e.printStackTrace();
			} finally
			{
				executorService.shutdown();
			}
			
			
			
			

			StringBuffer sb = new StringBuffer();
			if(result)
			{
				sb.append("OK");
				sb.append("\r\n");
				for(int i = 0; i < n; i++)
				{
					for(int j = 0; j < n; j++)
					{
						sb.append(sol[i][j]);
					}
					sb.append("\r\n");
				}
			}
			else
			{
				sb.append("FAIL");
			}
			
			//System.out.println(sb.toString());
			//printBoard(sol);
			
			File fout = new File("output.txt");
			FileWriter fw = new FileWriter(fout);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.flush();
			bw.write(sb.toString());
			
			bw.close();
			
			//System.out.println("work done!");
			
			
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		//System.out.println("Times: " + (endTime - startTime) );
		
		
	}
	
}
