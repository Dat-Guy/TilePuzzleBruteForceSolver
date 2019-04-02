public class Main {
    
    //Pieces to arrange
    private static int[][] pieces = new int[][]{ new int[]{10,2,3,7},
            new int[]{2,3,13,9},
            new int[]{1,2,1,5},
            new int[]{0,14,3,0},
            new int[]{4,0,1,11},
            new int[]{4,1,4,13},
            new int[]{6,9,4,2},
            new int[]{2,0,15,2},
            new int[]{15,3,1,7},
            new int[]{8,3,0,5},
            new int[]{5,0,1,3},
            new int[]{1,2,10,0},
            new int[]{1,11,5,4},
            new int[]{2,1,7,6},
            new int[]{2,1,5,0},
            new int[]{11,8,8,3}};
    
    //Width of the puzzle
    private static int width = 4;
    
    //Board to hold arrangement of pieces
    private static int[][] board = new int[pieces.length][];

    //Sort time!
    public static void main(String[] args) {
        
        //Initializes the board
        for (var i = 0; i < pieces.length; i++){
            board[i] = new int[]{0, 0};
        }
        
        //Initializes variables
        var solved = false;
        var currentIndex = 1;
        
        //Loop counter
        long configurationsTested = 0;
    
        //While unsolved
        while (!solved){
        
            //Print the board matrix we are processing this cycle
            
            printMatrix(board, currentIndex + 1);
            System.out.println("-------");
            
            //If we are looking at the first piece...else if we are invalid...else...
            
            if (currentIndex == 0){
                
                //...if piece can be rotated...else if we have more pieces...else...
                
                if (board[currentIndex][1] < 4){
                    
                    //...we already rotated it, return to second piece
                    
                    currentIndex++;
                } else if (board[currentIndex][0] < pieces.length - 1){
                    
                    //...get the next piece, start at initial rotation, return to second piece
                    
                    board[currentIndex][0]++;
                    board[currentIndex][1] = 0;
                    currentIndex++;
                } else {
                    
                    //...say the given is unsolvable, and quit out of the loop
                    
                    System.out.println("Unsolvable");
                    break;
                }
                
                //print out the index we are returning to (should always be 1)
                
                System.out.println(currentIndex);
                
            } else if (!validatePiece(currentIndex)){
                
                //If piece can be rotated...else we have more pieces...else...
                
                if (board[currentIndex][1] < 3){
                    
                    //...rotate the piece
                    
                    board[currentIndex][1]++;
                    
                } else if (board[currentIndex][0] < (pieces.length - 1)){
                    
                    //...get the next piece, reset the rotation
                    board[currentIndex][0]++;
                    board[currentIndex][1] = 0;
                    
                    //While our piece matches any previous pieces...
                    
                    while (currentIndex > 0 && (board[currentIndex-1][0] == board[currentIndex][0] ||
                            !validateBoard(currentIndex + 1))){
                        
                        //Go to the next piece, and if we have no more pieces...
                        
                        board[currentIndex][0]++;
                        if (board[currentIndex][0] >= pieces.length){
                            
                            //...reset current piece, set current to previous, and advance current piece by rotation of 1
                            
                            board[currentIndex][0] = 0;
                            currentIndex--;
                            board[currentIndex][1]++;
                            System.out.println(currentIndex);
                        }
                    }
                } else {
                    
                    //...our current board position has exhaused all possibilities, revert to previous piece, and advance it by 1 rotation
                    
                    board[currentIndex] = new int[]{0, 0};
                    currentIndex--;
                    board[currentIndex][1]++;
                    System.out.println(currentIndex);
                    
                }
            } else {
                
                //...our current progression of the board is valid, advance to the next board piece
                
                currentIndex++;
                
                //If we have not solved the board...
                
                if (currentIndex != board.length) {
                    
                    //While the next piece matches all previous pieces on the board...
                    
                    while (!validateBoard(currentIndex + 1)) {
                        
                        //...advance the piece, and if we have exhausted all possibilities...
                        
                        board[currentIndex][0]++;
                        if (board[currentIndex][0] >= pieces.length) {
                            
                            //...throw an error, as this logically DOESN'T MAKE ANY SENSE!
                            throw new Error("Please enable debugging, set a breakpoint here, and send data. How did you even get here?");
                            
                            
                        }
                    }
                }
                
                System.out.println(currentIndex);
            }
            
            //If we seem to have solved...
            if (currentIndex == board.length){
                
                //If we have actually solved...else...
                
                if (validateBoard(board.length)) {
                    
                    //Say we are solved, which exits the loop
                    
                    solved = true;
                } else {
                    
                    //Return to the last tile, and advance its rotation by 1
                    currentIndex--;
                    board[currentIndex][1]++;
                }
            }
            
            //Update the cycle counter
            configurationsTested++;
        
        }
        
        //Print the final matrix, and number of cycles
        printMatrix(board, board.length);
        System.out.println("\n-------------------------\nCombinations of tiles tested:\n" + configurationsTested);
    }
    
    //Gets the value of a side on a piece given its position on the board, and side relative to the board.
    private static int getPiecePart(int piece, int rot){
        if (piece > -1 && piece < board.length){
            var internalPiece = board[piece][0];
            
            var internalRot = (rot - board[piece][1]) % 4;
            
            internalRot = internalRot < 0 ? 4 + internalRot : internalRot;
            
            return pieces[internalPiece][internalRot];
        } else {
            return -1;
        }
    }
    
    
    //Checks if the piece does not match the one before, and that the sides match those of pieces to the top and to the left of the piece
    private static boolean validatePiece(int piece){
        
        if (piece != 0 && board[piece-1][0] == board[piece][0]){
            board[piece][1] = 3;
            return false;
        }
        if ((piece % width) != 0 && getPiecePart(piece, 3) != getPiecePart(piece - 1, 1)){
            return false;
        }
        
        if (piece >= width && getPiecePart(piece, 0) != getPiecePart(piece - width, 2)){
            return false;
        }
        
        /*if (piece < 12 && getPiecePart(piece, 2) != getPiecePart(piece + width, 0)){
            return false;
        }
        
        if (piece % width != width - 1 && getPiecePart(piece, 1) != getPiecePart(piece + 1, 3)){
            return false;
        }*/
        
        return true;
        
    }
    
    //Reports if any pieces are repeated on the board, starting from an upper index
    private static boolean validateBoard(int top){
        
        for (var i = top - 1; i > 0; i--){
            for (var j = i - 1; j > -1; j--){
                if (board[i][0] == board[j][0]){
                    return false;
                }
            }
        }
        
        return true;
    }
    
    //Prints any primitive matrix (nested arrays) of integers to the console.
    private static void printMatrix(int[][] Matrix, int rows){
        for (var i = 0; i < rows; i++){
            System.out.print("Row " + i + ": ");
            for (var j = 0; j < Matrix[i].length; j++){
                System.out.print(Matrix[i][j] + ", ");
            }
            System.out.println();
        }
    }
}