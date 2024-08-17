## Tenth iteration
In this iteration, you will practice creating tests and working with input and output.

1. **Modify** the `Chess.Builder` class to implement the `GameReadable` interface.
    - The `read(InputStream is)` reads data about the board layout from the **open binary input**,
	  instantiates pieces and puts them on the board. The data format is as follows:
	  - Each row of the board is on a separate line.
	  - Pieces are separated from one another by `;` and `_` represents an empty board position (a `null` piece).
	  - For the initial board layout, the data looks like this:
       ```
           ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK
           KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK
           BISHOP,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;BISHOP,BLACK
           QUEEN,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;QUEEN,BLACK
           KING,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KING,BLACK
           BISHOP,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;BISHOP,BLACK
           KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK
           ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK
       ```
	- If the `hasHeader` parameter of the overloaded `read(InputStream is, boolean hasHeader)` method is `false`,
	  then it works exactly the same as the previous method. If the parameter is `true`, then a header line with players is 
	  expected pro processed first. The format of the header line can be easily seen in the next example, where _Mat_ and _Pat_ 
	  are names of the players:
       ```
		   Mat-WHITE;Pat-BLACK
           ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK
           KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK
           BISHOP,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;BISHOP,BLACK
           QUEEN,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;QUEEN,BLACK
           KING,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KING,BLACK
           BISHOP,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;BISHOP,BLACK
           KNIGHT,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;KNIGHT,BLACK
           ROOK,WHITE;PAWN,WHITE;_;_;_;_;PAWN,BLACK;ROOK,BLACK
       ```
    - The other required variants of the `read` method with a `File` input parameter instead of `InputStream` work as expected - they read the same data from a file.
	- All the `read` methods fail with an `IOException` on any I/O error **or data format error**.
	- **Avoid code duplication** by reusing methods.
	- Use _UTF-8_ encoding when reading text data.
2. Modify the `Chess` class to implement the `GameWritable` interface.
    - The output format is the same as in the previous method. The header line with players is always printed.
3. Study the `writeJson` default method from the `GameWritable` interface. It is here to demonstrate the possibility of
   writing the chess data in a structured JSON format. This method is called from the `Main` class, where it prints JSON to the standard output.
   The implementation of your `write` methods is not used by the `Main` class, but the correctness is checked by unit tests.

Hints:
- Only **close** streams/files that you have opened.
- Use try with resources.
- Study the methods `Writer#flush()`, `Reader#ready()`.
- You can create a file using `new File("soubor.txt")`.
- `Main.main` can throw `IOException`.
- Do not forget to use correct **line break separator** instead of `\n`.
- The tests create a `game-out.txt` file. Check it for the content.

