
public class test 
{
	private static void printBoard(int[][] board)
	{
		int n = board.length;
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(board[i][j] < 0 || board[i][j] > 9)
					System.out.print(" "+ board[i][j]);
				else
					System.out.print("  "+ board[i][j]);
			}
			System.out.println("");
		}
		System.out.println("___________________________");
	}
	
	public static void main(String[] args)
	{
		int n = 10;
		int[][] board = new int[n][n];
		
		
		for(int sum = 2; sum <= 2*n - 4; sum++)
		{
			if( sum < n )
			{
				for(int j = 1; j <= sum-1; j++)
				{
					int i = sum - j;
					board[i][j] = sum;
				
				}
			}
			else
			{
				for(int j = sum - n + 2; j < n-1; j++)
				{
					int i = sum - j;
					board[i][j] = sum;
				}
			}
			
		}
		printBoard(board);
		
	}
}
