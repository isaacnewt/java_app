//A
import java.util.ArrayList;
import java.io.*;
import java.util.*;

class Engine {

  private static final String alphabet = "abcdefg";
  private int gridLength = 7;
  private int gridSize = 49;
  private int [] grid = new int[gridSize];
  private int comCount = 0;


  public String getUserInput(String prompt) {
     String inputLine = null;
     System.out.print(prompt + "  ");
     try {
       BufferedReader is = new BufferedReader(
	 new InputStreamReader(System.in));
       inputLine = is.readLine();
       if (inputLine.length() == 0 )  return null; 
     } catch (IOException e) {
       System.out.println("IOException: " + e);
     }
     return inputLine.toLowerCase();
  }

  
  
 public ArrayList<String> placer(int comSize) {                 // line 19
    ArrayList<String> alphaCells = new ArrayList<String>();
    String [] alphacoords = new String [comSize];      // holds 'f6' type coords
    String temp = null;                                // temporary String for concat
    int [] coords = new int[comSize];                  // current candidate coords
    int attempts = 0;                                  // current attempts counter
    boolean success = false;                           // flag = found a good location ?
    int location = 0;                                  // current starting location
    
    comCount++;                                        // nth dot com to place
    int incr = 1;                                      // set horizontal increment
    if ((comCount % 2) == 1) {                         // if odd dot com  (place vertically)
      incr = gridLength;                               // set vertical increment
    }

    while ( !success & attempts++ < 200 ) {             // main search loop  (32)
	location = (int) (Math.random() * gridSize);      // get random starting point
        //System.out.print(" try " + location);
	int x = 0;                                        // nth position in dotcom to place
        success = true;                                 // assume success
        while (success && x < comSize) {                // look for adjacent unused spots
          if (grid[location] == 0) {                    // if not already used
             coords[x++] = location;                    // save location
             location += incr;                          // try 'next' adjacent
             if (location >= gridSize){                 // out of bounds - 'bottom'
               success = false;                         // failure
             }
             if (x>0 & (location % gridLength == 0)) {  // out of bounds - right edge
               success = false;                         // failure
             }
          } else {                                      // found already used location
              // System.out.print(" used " + location);  
              success = false;                          // failure
          }
        }
    }                                                   // end while

    int x = 0;                                          // turn good location into alpha coords
    int row = 0;
    int column = 0;
    // System.out.println("\n");
    while (x < comSize) {
      grid[coords[x]] = 1;                              // mark master grid pts. as 'used'
      row = (int) (coords[x] / gridLength);             // get row value
      column = coords[x] % gridLength;                  // get numeric column value
      temp = String.valueOf(alphabet.charAt(column));   // convert to alpha
      
      alphaCells.add(temp.concat(Integer.toString(row)));
      x++;

      //System.out.print("  coord "+x+" = " + alphaCells.get(x-1));
      
    }
    // System.out.println("\n");
    
    return alphaCells;
   }
}

//B


class GoDown {
    private ArrayList<String> locationCells;
    
    public void setLocationCells(ArrayList<String> loc)
    {
        locationCells = loc;
    }
    
    public String checkYourself(String userInput)
    {
        String result = "miss";
        int index = locationCells.indexOf(userInput);
        if (index >= 0) {
            locationCells.remove(index);
            if (locationCells.isEmpty()) {
                result = "kill";
            }
            else
            {
                result = "hit";
            }
        }
        return result;
    }

    //TODO:  all the following code was added and should have been included in the book
    private String name;
    public void setName(String string) {
        name = string;
    }
}

//C



public class GoDust {
    private Engine helper = new Engine();
    private ArrayList<GoDown> goDownList = new ArrayList<GoDown>();
    private int numOfGuesses = 0;
    
    private void setUpGame() {
	
        GoDown one = new GoDown();
        one.setName("BaloonOne");
        GoDown two = new GoDown();
        two.setName("BaloonTwo");
        GoDown three = new GoDown();
        three.setName("BaloonThree");
        goDownList.add(one);
        goDownList.add(two);
        goDownList.add(three);
        
        System.out.println("Your goal is to sink three Baloons.");
        System.out.println("BaloonOne, BaloonTwo and BaloonThree ");
        System.out.println("Try to sink them all in the fewest number of guesses");
        
        for (GoDown g : goDownList) {
            ArrayList<String> newLocation = helper.placer(3);
            g.setLocationCells(newLocation);
        }
    }
    
    private void startPlaying() {
	numOfGuesses++;
	
        do{
            String userGuess = helper.getUserInput("Enter a guess");
            checkUserGuess(userGuess);
	  }while(numOfGuesses<31&!goDownList.isEmpty());
        finishGame();
    }

    private void checkUserGuess(String userGuess)
    {
        
        String result = "miss";
        numOfGuesses++;
        for (GoDown ga : goDownList)
        {
	    
	    
            result = ga.checkYourself(userGuess);
            if (result.equals("hit"))
            {
                break;
            }
            if (result.equals("kill"))
            {
                goDownList.remove(ga);
                break;
            }	
	    
        }
        System.out.println(result);
	
	
	
    }	
    
    private void finishGame() {
        
        if (numOfGuesses <= 18) {
	    System.out.println("All Dot Coms are dead!  Your stock is now worthless");
            System.out.println("It only took you " + numOfGuesses + " guesses");
            System.out.println("You got out before your options sank.");
        } 
	else if(numOfGuesses >= 30)
	{
	    System.out.println("It's Gameover. You took more than. " + numOfGuesses + " guesses.");
	}
        else
        {
            System.out.println("Took you long enough. " + numOfGuesses + " guesses.");
            System.out.println("Fish are dancing with your options.");
        }
    }
    
    public static void main(String[] args) {
        GoDust game = new GoDust();
        game.setUpGame();
        game.startPlaying();
    }
}
