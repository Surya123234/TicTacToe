//TicTacToe
//Created By: Surya Sendhilraj
//Last Modified: 12/06/2020
//Allows user to play tic tac toe against the computer with 2 difficulties

import hsa.Console;
import java.util.*;
import java.io.*; 

public class TicTacToe
{
  static int wins = 0;    //Global variable for the number of user wins
  static int losses = 0;  //Global variable for the number of user losses
  static int draws = 0;   //Global variable for the number of user draws
  static int noEnd = 0;   //Global variable which returns when game is not over
  
  static Console c;                        
  
  // the main method
  public static void main (String[] args) throws IOException
  {
    c = new Console ();       // the output console

    String difficulty;         // difficulty of the game
    
    // Intro to the game
    c.println("Welome to Tic Tac Toe!");
    c.println("**************************");
    
    // getting difficulty from user
    difficulty = getDifficulty();
    
    // infinite loop to keep game running unless broken out of
    while(true)
    {
      for(int i=1;i<=10;i++)    //10 rounds in 1 game
      {
        c.clear();     // clears the console
        
        //displaying round number
        c.println("===== ROUND " + i + " of 10 =====");
        
        //creating the game board
        char [] [] board = { {'_','|','_','|','_'}, {'_','|','_','|','_'}, {' ','|',' ','|',' '} }; 
        
        //prints gameboard on the console
        outputBoard(board);
        
        boolean gameEnd = false;           // game cannot be over on the 1st turn
        
        // Running the game until someone wins
        while(gameEnd != true)
        {
          playerTurn(board);                //user input for piece placement
          
          gameEnd = gameOver(board);        //checking win conidtions       
          
         //When the win condition is met
          if(gameEnd == true)                              
          {
            break;
          }
          // game progresses based on difficulty selection
          if(difficulty.equalsIgnoreCase("hard"))
          {
            cpuTurnHard(board);          // hard difficulty
          }
          else if(difficulty.equalsIgnoreCase("easy"))
          {
            cpuTurn(board);            // easy difficulty
          }
          //checks for win conditions
          gameEnd = gameOver(board);
          
          //breaks out of while loop if round ends
          if(gameEnd == true)
          {
            break;
          }
        }
        gameStats(board); //updates game stats  
        
       //when it is not round 10 
        if(i !=10)        
        {
          String playAgain;
          while(true)
          {
            // checks if user wants to play another ROUND
            try
            {
              // user input
              c.println("Do you want to play the next round? [Y/N]");
              playAgain = c.readLine();
              
              // displays stats and ends program
              if(playAgain.equalsIgnoreCase("n"))
              {
                c.clear();
                fileOutput();
                displayGameStats(false);
                c.println("****************************************");
                c.println("Enter anything to exit the game");
                playAgain=c.readLine();
                System.exit(0);         // terminates the program successfully
              }
              //starting another round
              else if(playAgain.equalsIgnoreCase("y"))
              {
                break;
              }
              else
              {
                c.println("Please enter a valid option [Y/N]");
              }
            }
            catch(Exception e)
            {
              c.println("Please enter a valid option [Y/N]");
            }
          }
        }
        // when a game ends
        else                 
        {
          // displaying game stats 
          fileOutput();         
          displayGameStats(false);
          try
          {
            //checking if user wants to play another game
            String playAgain1;
            c.println("Do you want to play another game? ( Press 'n' to exit and any other key to play another game) ");
            playAgain1 = c.readLine();
            if(playAgain1.equalsIgnoreCase("n"))
            {
              // exits the game
              c.clear();
              System.exit(0);                 // terminates the program successfully
              break;
            }
            else if(playAgain1.equalsIgnoreCase("y"))
            {
              //resets game stats 
              wins = 0;
              losses = 0;
              draws = 0;
              break;
            }
            else
            {
              c.println("Please enter a valid option [Y/N]");
            }
          }
          catch(Exception e)
          {
            c.println("Please enter a valid option [Y/N]");
          }
        }
      }
    }
  } // niam
  
  //Getting game difficulty from user input
  //Returns a string storing the difficulty
  public static String getDifficulty () throws IOException
  {
    while(true)
    {
      try
      {
        c.println("Please choose a difficulty of either EASY or HARD (not case sensitive)");
        String diff = c.readLine();    //user input
        
        if(diff.equalsIgnoreCase("hard") || diff.equalsIgnoreCase("easy"))
        {
          c.clear();
          return diff;
        }
        else
        {
          c.println("That is invalid, please try again:");
        }
      }
      catch(Exception e)
      {
        c.println("That is invalid, try again");
      }
    }
  }
  
  //Prints the game board on the console
  // char [] [] board is the game board
  public static void outputBoard (char [] [] board) throws IOException
  {
    //prints game board
    for(int i=0;i<board.length;i++)         
    {
      for(int j=0;j<board[i].length;j++) 
      {
        c.print(board[i][j]);
      }
      c.println();    
    }
  }
  
  // User input for their piece placement on the boxes 1 through 9
  //char [] [] board is the game board
  public static void playerTurn(char [] [] board) throws IOException
  {
    //Variable declarations
    int box = 0;   // the squares on the game board
    int player=1;     // value of user correspinding to a symbol   
    String boxInput;   // string value of user input for piece placement
    
    while(true)
    {
      try
      {
        c.println("Which box (from 1 to 9) do you want to place the symbol in? Enter \"*\" to exit.");
        boxInput = c.readLine();         //user input
        
        //if user wants to end the game
        if (boxInput.equals("*"))
        {
          fileOutput();
          displayGameStats(false);
          c.println("****************************************");
          c.println("Enter anything to exit the game");
          boxInput=c.readLine();
          System.exit(0);                 // terminates the program successfully
        }
        //continuing the round
        else
          box = Integer.parseInt(boxInput);        // integer value of the box 
        if(box>=1 && box<=9)
        {
          break;
        }
        else
        {
          c.println("That is invalid, please try again:");
        }
      }
      catch(NumberFormatException e)               
      {
        c.println("That is invalid, try again");
      }
    }
    // checking validity of the piece placement
    boolean valid = validMove(board,box); 
    while(valid == false)
    {
      c.println("Whoops! That is an invalid move. Please enter a valid move (from boxes 1-9:) ");
      box=c.readInt();
      valid = validMove(board,box);
    }
    c.println("Player moved at box "+box+":");
    boardUpdate(box, player, board);        
  }
  
  //Prints the updated game board once the user or computer has moved
  //box is the user input for piece placement
  //player is the value of the CPU or player corresponding to a symbol
  //char [] [] board is the game board
  public static void boardUpdate (int box, int player, char [] [] board) throws IOException
  {
    char symbol;
    
    if(player == 1) 
    {
      symbol = 'X';                  // User's symbol
    }
    else
    {
      symbol = 'O';                    // CPU's symbol
    }
    
    //placing moves on box 1 through 9 and printing the game board
    switch(box)
    {
      case 1 : board[0][0] = symbol;                  
      outputBoard(board);
      break;
      case 2 : board[0][2] = symbol;                   
      outputBoard(board);
      break;
      case 3 : board[0][4] = symbol;
      outputBoard(board);
      break;
      case 4 : board[1][0] = symbol;
      outputBoard(board);
      break;
      case 5 : board[1][2] = symbol;
      outputBoard(board);
      break;
      case 6 : board[1][4] = symbol;
      outputBoard(board);
      break;
      case 7 : board[2][0] = symbol; 
      outputBoard(board);
      break;
      case 8 : board[2][2] = symbol;
      outputBoard(board);
      break;
      case 9 : board[2][4] = symbol;
      outputBoard(board);
      break;
      default: break;
    }
  }
  
  // Computer's piece placement on EASY difficulty
  // char [] [] board is the game board
  public static void cpuTurn(char [] [] board) throws IOException
  {
    int cpuBox;        // box of computer
    int cpu=0;         // value of CPU corresponding to a symbol
    
    // CPU moves and checking for validity
    cpuBox = (int) (Math.random()*9+1); 
    boolean valid = validMove(board,cpuBox);
    while(valid == false)
    {
      cpuBox = (int) (Math.random()*9+1);
      valid = validMove(board,cpuBox);
    }
    c.println("Computer moved at box "+cpuBox+":");
    boardUpdate(cpuBox,cpu, board);                 // updates game board
  }
  
  // Computer's piece placement of HARD difficulty
    // char [] [] board is the game board
  public static void cpuTurnHard(char [] [] board)  throws IOException 
  {
    int cpuBox;
    int cpu = 0;
    
    //blocking or winning moves on the horizontals
    if(board[0][0]=='O' && board[0][2]=='O' && board[0][4]=='_' || board[0][0]=='X' && board[0][2]=='X' && board[0][4]=='_')
    {
      cpuBox = 3;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if( board[0][0]=='O' && board[0][2]=='_' && board[0][4]=='O' || board[0][0]=='X' && board[0][2]=='_' && board[0][4]=='X')
    {
      cpuBox = 2;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][0]=='_' && board[0][2]=='O' && board[0][4]=='O' || board[0][0]=='_' && board[0][2]=='X' && board[0][4]=='X')
    {
      cpuBox = 1;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[1][0]=='O' && board[1][2]=='O' && board[1][4]=='_' || board[1][0]=='X' && board[1][2]=='X' && board[1][4]=='_')
    {
      cpuBox = 6;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[1][0]=='O' && board[1][2]=='_' && board[1][4]=='O' || board[1][0]=='X' && board[1][2]=='_' && board[1][4]=='X')
    {
      cpuBox = 5;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[1][0]=='_' && board[1][2]=='O' && board[1][4]=='O' || board[1][0]=='_' && board[1][2]=='X' && board[1][4]=='X')
    {
      cpuBox = 4;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[2][0]=='O' && board[2][2]=='O' && board[2][4]=='_' || board[2][0]=='X' && board[2][2]=='X' && board[2][4]=='_')
    {
      cpuBox = 9;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[2][0]=='O' && board[2][2]=='_' && board[2][4]=='O' || board[2][0]=='X' && board[2][2]=='_' && board[2][4]=='X')
    {
      cpuBox = 8;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[2][0]=='_' && board[2][2]=='O' && board[2][4]=='O' || board[2][0]=='_' && board[2][2]=='X' && board[2][4]=='X')
    {
      cpuBox = 7;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    
    //blocking or winning moves on the verticals
    else if(board[0][0]=='O' && board[1][0]=='O' && board [2][0]==' ' || board[0][0]=='X' && board[1][0]=='X' && board [2][0]==' ')
    {
      cpuBox = 7;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][0]=='O' && board[1][0]=='_' && board [2][0]=='O' || board[0][0]=='X' && board[1][0]=='_' && board [2][0]=='X')
    {
      cpuBox = 4;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][0]=='_' && board[1][0]=='O' && board [2][0]=='O' || board[0][0]=='_' && board[1][0]=='X' && board [2][0]=='X')
    {
      cpuBox = 1;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][2]=='O' && board[1][2]=='O' && board [2][2]==' ' || board[0][2]=='X' && board[1][2]=='X' && board [2][2]==' ')
    {
      cpuBox = 8;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][2]=='O' && board[1][2]=='_' && board [2][2]=='O' || board[0][2]=='X' && board[1][2]=='_' && board [2][2]=='X')
    {
      cpuBox = 5;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][2]=='_' && board[1][2]=='O' && board [2][2]=='O' || board[0][2]=='_' && board[1][2]=='X' && board [2][2]=='X')
    {
      cpuBox = 2;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][4]=='O' && board[1][4]=='O' && board [2][4]==' ' || board[0][4]=='X' && board[1][4]=='X' && board [2][4]==' ')
    {
      cpuBox = 9;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][2]=='O' && board[1][2]=='_' && board [2][2]=='O' || board[0][2]=='X' && board[1][2]=='_' && board [2][2]=='X')
    {
      cpuBox = 6;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][2]=='_' && board[1][2]=='O' && board [2][2]=='O' || board[0][2]=='_' && board[1][2]=='X' && board [2][2]=='X')
    {
      cpuBox = 3;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    
    //blocking or winning moves on the diagonals
    else if(board[0][0] == 'O' && board[1][2] == 'O' && board[2][4] == ' ' || board[0][0] == 'X' && board[1][2] == 'X' && board[2][4] == ' ')
    {
      cpuBox = 9;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][0] == 'O' && board[1][2] == '_' && board[2][4] == 'O' || board[0][0] == 'X' && board[1][2] == '_' && board[2][4] == 'X')
    {
      cpuBox = 5;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][0] == '_' && board[1][2] == 'O' && board[2][4] == 'O' || board[0][0] == '_' && board[1][2] == 'X' && board[2][4] == 'X')
    {
      cpuBox = 1;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][4] == 'O' && board[1][2] == 'O' && board[2][0] == ' ' || board[0][4] == 'X' && board[1][2] == 'X' && board[2][0] == ' ')
    {
      cpuBox = 7;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][4] == 'O' && board[1][2] == '_' && board[2][0] == 'O' || board[0][4] == 'X' && board[1][2] == '_' && board[2][0] == 'X')
    {
      cpuBox = 5;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    else if(board[0][4] == '_' && board[1][2] == 'O' && board[2][0] == 'O' || board[0][4] == '_' && board[1][2] == 'X' && board[2][0] == 'X')
    {
      cpuBox = 3;
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu,board);
    }
    // if previous conditions are not met
    else
    {
      cpuBox = (int) (Math.random()*9+1);
      boolean valid = validMove(board,cpuBox);
      while(valid == false)
      {
        cpuBox = (int) (Math.random()*9+1);
        valid = validMove(board,cpuBox);
      }
      c.println("Computer moved at box "+cpuBox+":");
      boardUpdate(cpuBox,cpu, board);
    }
  }
  
  // Checks if a box already has a piece on it (validity)
  //Returns true if box is not occupied
  //Returns false if box is occupied
  //char [] [] board is the game board
  //box is the user's or computer's move area
  public static boolean validMove (char [] [] board, int box) throws IOException
  {
    switch(box)
    {
      case 1: if(board [0][0] =='_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 2: if(board [0][2] == '_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 3: if(board [0][4] =='_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 4: if(board [1][0] =='_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 5: if(board [1][2] =='_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 6: if(board [1][4] =='_')
      {
        return true;
      }
      else
      {
        return false;
      }
      case 7: if(board [2][0] ==' ') 
      {
        return true;
      }
      else
      {
        return false;
      }
      case 8: if(board [2][2] ==' ') 
      {
        return true;
      }
      else
      {
        return false;
      }
      case 9: if(board [2][4] ==' ')
      {
        return true;
      }
      else
      {
        return false;
      }
      default: return false; 
    }
  }
  
  //Checls for winning/losing/drawing conidtions for the game to end
  //Returns true if game ending conidtion is met
  //Returns false if game ending condition is not met 
  //char [] [] board is the game board
  public static boolean gameOver(char [] [] board) throws IOException
  {
    //Vertical Game Ending Conditions
    if(board[0][0] == 'X' && board[1][0] == 'X' && board[2][0] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[0][2] == 'X' && board[1][2] == 'X' && board[2][2] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[0][4] == 'X' && board[1][4] == 'X' && board[2][4] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    if(board[0][0] == 'O' && board[1][0] == 'O' && board[2][0] == 'O')
    {
      c.println("Unlucky! The computer won this time!");
      return true;
    }
    else if(board[0][2] == 'O' && board[1][2] == 'O' && board[2][2] == 'O')
    {
      c.println("Unlucky! The computer won this time!");
      return true;
    }
    else if(board[0][4] == 'O' && board[1][4] == 'O' && board[2][4] == 'O')
    {
      c.println(" Unlucky! The computer won this time!");
      return true;
    }
    
    //Horizontal Game Ending Conditions
    else if(board[0][0] == 'X' && board[0][2] == 'X' && board[0][4] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[1][0] == 'X' && board[1][2] == 'X' && board[1][4] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[2][0] == 'X' && board[2][2] == 'X' && board[2][4] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[0][0] == 'O' && board[0][2] == 'O' && board[0][4] == 'O')
    {
      c.println("Unlucky! The computer won this time!");
      return true;
    }
    else if(board[1][0] == 'O' && board[1][2] == 'O' && board[1][4] == 'O')
    {
      c.println(" Unlucky! The computer won this time!");
      return true;
    }
    else if(board[2][0] == 'O' && board[2][2] == 'O' && board[2][4] == 'O')
    { 
      c.println("Unlucky! The computer won this time!");
      return true;
    }
    
    //Diagonal Game Ending Conditions
    else if(board[0][0] == 'X' && board[1][2] == 'X' && board[2][4] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[0][4] == 'X' && board[1][2] == 'X' && board[2][0] == 'X')
    {
      c.println(" Nice job! You win!");
      return true;
    }
    else if(board[0][0] == 'O' && board[1][2] == 'O' && board[2][4] == 'O')
    {
      c.println(" Unlucky! The computer won this time!");
      return true;
    }
    else if(board[0][4] == 'O' && board[1][2] == 'O' && board[2][0] == 'O')
    {
      c.println(" Unlucky! The computer won this time!");
      return true;
    }
    
    //Drawing Game Ending Condition
    else if(board[0][0] != '_' && board[0][2] != '_' && board[0][4] != '_' && board[1][0] != '_' && board[1][2] != '_' && board[1][4] != '_' && board[2][0] != ' ' && board[2][2] != ' ' && board[2][4] != ' ')
    {
      c.println("You didn't win, but didn't lose either! It's a draw!");
      return true;
    }
    return false;
  }
  
  //Tracks user's number of wins, losses, and draws
  //Returns the new value of the number of wins/losses/draws
  //Returns noEnd if the game is not over
  //char [] [] board is the game board
  public static int gameStats(char [] [] board)throws IOException
  {
    //Vertical Win
    if(board[0][0] == 'X' && board[1][0] == 'X' && board[2][0] == 'X')
    {
      wins++;
      return wins;
    }
    else if(board[0][2] == 'X' && board[1][2] == 'X' && board[2][2] == 'X')
    {
      wins++;
      return wins;
    }
    else if(board[0][4] == 'X' && board[1][4] == 'X' && board[2][4] == 'X')
    {
      wins++;
      return wins;
    }
    
    //Vertical Loss
    if(board[0][0] == 'O' && board[1][0] == 'O' && board[2][0] == 'O')
    {
      losses++;
      return losses;
    }
    else if(board[0][2] == 'O' && board[1][2] == 'O' && board[2][2] == 'O')
    {
      losses++;
      return losses;
    }
    else if(board[0][4] == 'O' && board[1][4] == 'O' && board[2][4] == 'O')
    {
      losses++;
      return losses;
    }
    
    //Horizontal Win
    else if(board[0][0] == 'X' && board[0][2] == 'X' && board[0][4] == 'X')
    {
      wins++;
      return wins;
    }
    else if(board[1][0] == 'X' && board[1][2] == 'X' && board[1][4] == 'X')
    {
      wins++;
      return wins;
    }
    else if(board[2][0] == 'X' && board[2][2] == 'X' && board[2][4] == 'X')
    {
      wins++;
      return wins;
    }
    
    //Horizontal Loss
    else if(board[0][0] == 'O' && board[0][2] == 'O' && board[0][4] == 'O')
    {
      losses++;
      return losses;
    }
    else if(board[1][0] == 'O' && board[1][2] == 'O' && board[1][4] == 'O')
    {
      losses++;
      return losses;
    }
    else if(board[2][0] == 'O' && board[2][2] == 'O' && board[2][4] == 'O')
    { 
      losses++;
      return losses;
    }
    
    //Diagonal Win
    else if(board[0][0] == 'X' && board[1][2] == 'X' && board[2][4] == 'X')
    {
      wins++;
      return wins;
    }
    else if(board[0][4] == 'X' && board[1][2] == 'X' && board[2][0] == 'X')
    {
      wins++;
      return wins;
    }
    
    //Diagonal Loss
    else if(board[0][0] == 'O' && board[1][2] == 'O' && board[2][4] == 'O')
    {
      losses++;
      return losses;
    }
    else if(board[0][4] == 'O' && board[1][2] == 'O' && board[2][0] == 'O')
    {
      losses++;
      return losses;
    }
    
    //Drawing scenario
    else if(board[0][0] != '_' && board[0][2] != '_' && board[0][4] != '_' && board[1][0] != '_' && board[1][2] != '_' && board[1][4] != '_' && board[2][0] != ' ' && board[2][2] != ' ' && board[2][4] != ' ')
    {
      draws++;
      return draws;
    }
    return noEnd;
  }
  
  //Prints game statistics on the console (# wins, # losses, # draws, overall winner)
  //Can also Terminate Program if boolean allowExit is true
  //boolean allowExit is true when user wants to stop playing
  public static void displayGameStats(boolean allowExit) throws IOException
  {
    c.clear();                   // Clears the console
    c.println("GAME STATS: ");
    c.println("Game stats are ALSO displayed in a file called \"GameStat.txt\"");
    c.println();
    c.println("ROUNDS PLAYED: "+ (wins+losses+draws));
    if(wins+draws > losses+draws)
    {
      c.println("YOU were the OVERALL WINNER!");
    }
    else if(wins+draws < losses+draws)
    {
      c.println("The COMPUTER was the OVERALL WINNER!");
    }
    else if(wins+draws ==0 && losses+draws ==0)
    {
      c.println("No rounds were finished!");
    }
    else if(wins+draws == losses+draws)
    {
      c.println("You and the computer TIED!");
    }
    c.println("WINS: " + wins);
    c.println("LOSSES: " + losses);
    c.println("DRAWS: " + draws);
    
    if (allowExit==true)                                        
    {
      
      System.exit(0);   //successfully terminates program
    }
  }
  
  //Outputs Game Stats on a file named "GameStat.txt" (# wins, # losses, # draws, overall winner)
  public static void fileOutput() throws IOException
  {
    //Creating output file
    PrintWriter output;
    output = new PrintWriter(new FileWriter("GameStat.txt"));
    
    //Outputting stats on file
    output.println("GAME STATS: ");
    output.println();
    output.println("ROUNDS PLAYED: "+ (wins+losses+draws));
    if(wins+draws > losses+draws)
    {
      output.println("YOU were the OVERALL WINNER!");
    }
    else if(wins+draws < losses+draws)
    {
      output.println("The COMPUTER was the OVERALL WINNER!");
    }
    else if(wins+draws ==0 && losses+draws ==0)
    {
      c.println("No rounds were finished!");
    }
    else if(wins+draws == losses+draws)
    {
      output.println("You and the computer TIED!");
    }
    output.println("WINS: " + wins);
    output.println("LOSSES: " + losses);
    output.println("DRAWS: " + draws);
  }
}