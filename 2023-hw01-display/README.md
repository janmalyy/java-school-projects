Homework assignment no. 1, Segment Display
====================================

**Publication date:**  March 6, 2023

**Submission deadline:** March 20, 2023

## CHANGELOG

* 6.3.2023: Initial version
* 6.3.2023: Fixed problems with line separators in tests 
* 18.3.2023: Increased number of allowed method parameters (you shouldn't need it!)

General information
-------------------
This section provides general information about the initial structure of the provided codebase.  

### Project Structure
The structure of the project provided as a base for your implementation should meet the following criteria.

1. Package ```cz.muni.fi.pb162.hw01``` contains classes and interfaces provided as a part of the assignment.
- **Do not modify or add any classes or subpackages into this package.**
2. Package  ```cz.muni.fi.pb162.hw01.impl``` should contain your implementation.
- **Anything outside this package will be ignored during evaluation.**


Additionally, the following applies for the initial contents of ``cz.muni.fi.pb162.hw01.impl``

1) The information in **javadoc has precedence over everything**
2) **Interfaces** must be implemented
3) **Interfaces** must keep predefined methods
4) Otherwise, you can modify the code (unless tests are affected) as you see fit
5) When in doubt, **ASK**

**Note:**  
*While a modification of the interface is not strictly prohibited, you don't want to end with [god object](https://en.wikipedia.org/wiki/God_object) implementations.    
On the other hand, you want to adhere to the [single responsibility principle](https://en.wikipedia.org/wiki/Single-responsibility_principle).  
A rule of thumb should be not to modify interfaces unless required by the assignment. Consider extending the interface as an alternative.*

### Names in This Document
Unless fully classified name is provided, all class names are relative to the package ```cz.muni.fi.pb162.hw01``` or ```cz.muni.fi.pb162.hw01.impl``` for classes implemented as a part of your solution.

### Compiling the Project
The project can be compiled and packaged in the same way you already know.

```bash
$ mvn clean install
```

The only difference is that unlike the seminar project, the checks for missing documentation and a style violation will produce an error this time.
You can disable this behavior temporarily when running this command.

```bash
$ mvn clean install -Dcheckstyle.skip
```

You can consult your seminar teacher to help you set the ```checkstyle.skip``` property in your IDE (or just google it).

### Submitting the Assignment
Follow instructions of your tutor because the procedure to submit your solution may differ based on your seminar group. However, there are two ways of submission in general:
* Fork the project, develop your code in a development branch, and finally ask for the merge.
* Submit ```target/homework01-2023-1.0-SNAPSHOT-sources.jar``` to the homework vault.

### Minimal Requirements for Acceptance
- Fulfilling all Java course standards (documentation, conventions, etc.)
- Proper code decomposition
  - Split your code into multiple classes
  - Organize your classes in packages
- Single responsibility
  - Class should ideally have a single purpose
- Extendable code
  - Future support for different symbols...
  - Future support for [different display types](https://en.wikiversity.org/wiki/Segment_display), e.g., three-segment displays.
- All provided tests must pass


Assignment Description
-------------
The goal of this homework is to implement a program which will simulate a virtual
[(seven) segment display](https://en.wikipedia.org/wiki/Seven-segment_display) *(without the optional DP segment)*.


### Supported Symbols
In contrast to a physical segment display, printing to the console possesses certain limitations regarding the character placement.
However, in case of the seven segment display we can make the symbol quite distinguishable while keeping a reasonable size.

When printed to stdout, a seven segment display (capable of displaying a single symbol) is formatted as follows

1) The display is exactly 3 lines high 
2) The display is exactly 3 characters wide 
3) There is no space in between adjacent displays 

*Note: A display in this context means a unit capable of displaying a single symbol*


Bellow is a template of symbols supported by our application. 

```
# Digits:  0123456789 (the empty space at the begining represents an empty display) 
    _     _  _     _     _  _  _ 
   | |  | _| _||_||_ |_   ||_||_|
   |_|  ||_  _|  | _||_|  ||_|  |

# Error symbol: E
 _ 
|_ 
|_ 
```

The *error symbol* is displayed in case of an unsupported character. See the javadoc for more information.  
**Note: Make sure that the formatting of your output is exactly the same as described**

### Implementation Notes
To achieve proper decomposition, you are required to finish the implementation of

- `DisplayApp` while keeping the command line interface.
- `Factory` which provides [factory methods](https://en.wikipedia.org/wiki/Factory_method_pattern) for your implementations

### Command Line Interface
Part of the implementation is a command line Application which should support the flags

| Name         | Short | Long      | Default | Description                                      | Conditions        |
|--------------|-------|-----------|---------|--------------------------------------------------|-------------------|
| Size         | -s    | --size    | 1       | Total size of the display (number of characters) | \>= 1             |

The Application should recognize the following positional arguments

| #   | Description            |
|-----|------------------------|
| 1   | String to be displayed |


### Running the Application
The build descriptor is configured to produce a single runnable jar file located at `target/application.jar`. The application can be run using the following command

```
# With no args a single empty display is  printed (3 lines of 3 spaces)
$ java -jar application.jar
   
   
   

# Display digit 5
$ java -jar application.jar 5
 _
|_
 _|

# Display given symbols on a display of size 6
$ java -jar application.jar -s 6 01234567890E
 _     _  _     _
| |  | _| _||_||_
|_|  ||_  _|  | _|

# Display given symbols on a display of size 12
$ java -jar application.jar -s 12 01234567890E
 _     _  _     _     _  _  _  _  _
| |  | _| _||_||_ |_   ||_||_|| ||_
|_|  ||_  _|  | _||_|  ||_|  ||_||_

# Display content with the unsupported symbol
java -jar application.jar -s 3 1a2                                                                          20:55:46
    _  _
  ||_  _|
  ||_ |_
```
