Homework assignment no. 3, Simple Templates
====================================

**Publication date:**  May 1st, 2023

**Submission deadline:** May 15th, 2023

CHANGELOG
-------------------
- 2.5.2023: Added missing empty line in `Outputs.IF`. This has no effect on test result.
- 2.5.2023: Fixed issue when repeated calls to `Tokenizer#get()` threw an exception in certain situations.  
- 1.5.2023: Fixed file name `malformed.txt..tpl -> malformed.txt.tpl`
- 29.4.2023: Fixed extension value in FileTemplateTest#shouldFailToLoadMalformedTemplate


### Pulling changes
The following script displays how to pull new changes.
The script adds the assignment repository as a new remote under the name *pb162* and uses  `<impl>` as a placeholder for the implementation branch.

```bash
# Add assignment remote as pb162 (skip if you already have this)
git remote add pb162 https://gitlab.fi.muni.cz/pb162/2023-hw03-templates.git
# Local repo should be clean and everything should be committed in the <impl> branch
git status 
git switch main 
# Pull changes from pb162/main into local main 
git pull pb162 main
# Push those changes to your remote main  on gitlab
git push origin main 
git switch <impl>
# Pull changes from local main into local <impl> brnach.
# The --rebase option will change history so that your commits stay on top of the history
git pull --rebase . main 
# Push those changes to your remote <impl> branch
# CAREFUL: this is a FORCE push, check twice you are pushing to the correct branch
# The --force is needed as the previous command changed history (added new commits which were placed under your commits).  
git push origin <impl> --force
```



General information
-------------------
This section provides general information about the initial structure of the provided codebase.  

### Project Structure
The structure of the project provided as a base for your implementation should meet the following criteria.

1. Package ```cz.muni.fi.pb162.hw03``` contains classes and interfaces provided as a part of the assignment.
   - **Do not modify or add any classes or subpackages into this package.**
   - **Interfaces must be implemented (Unless implementation is also provided).**
2. Package  ```cz.muni.fi.pb162.hw03.impl``` should contain your implementation.
- **Anything outside this package will be ignored during the evaluation.**


Additionally, the following applies for the initial contents of ``cz.muni.fi.pb162.hw03.impl``

1) The information in **javadoc has precedence over everything**
2) You can modify the code (unless tests are affected) as you see fit. 
3) When in doubt, **ASK**


### Names in This Document
Unless fully classified name is provided, all class names are relative to the package ```cz.muni.fi.pb162.hw03``` or ```cz.muni.fi.pb162.hw03.impl``` for classes implemented as a part of your solution.

### Compiling the Project
The project can be compiled and packaged in the same way you already know.

```bash
$ mvn clean install
```

The only difference is that unlike the seminar project, the checks for a missing documentation and a style violation will produce an error this time.
You can disable this behavior temporarily when running this command.

```bash
$ mvn clean install -Dcheckstyle.skip
```

You can consult your seminar teacher to help you set the ```checkstyle.skip``` property in your IDE (or just google it).

### Submitting the Assignment
Follow your tutor's instructions because the procedure to submit your solution may differ based on your seminar group. However, there are two ways of submission in general:
* Fork the project, develop your code in a development branch, and finally ask for the merge.
* Submit ```target/homework02-2023-1.0-SNAPSHOT-sources.jar``` or zip archive of your project to the homework vault.

**Note: DO NOT FORGET to make your fork PRIVATE**

### Minimal Requirements for Acceptance
- Fulfilling all Java course standards (documentation, conventions, etc.)
- Proper code decomposition
  - Split your code into multiple classes
  - Organize your classes in packages
- Single responsibility
  - Each class should ideally have a single purpose
- Extendable code
- All provided tests must pass


Assignment Description
-------------
The goal of this homework is to implement a template engine similar to [Jinja2](https://jinja.palletsprojects.com/en/3.1.x/), [Mustache](https://mustache.github.io/), or [Handlebars](https://handlebarsjs.com/). It goes without saying that our implementation will be very basic compared to the mentioned libraries.

### Informal Description
The core of our template engine will be the ability to take a text input, containing markup commands, and a data model (represented by the `TemplateModel` interface) and to produce an output text. The output text is constructed by transforming the input based on the aforementioned markups and filling in the information contained in the model.

Our templates will support the following commands.

#### Print Command
This command will simply look for the value corresponding to the given key in the model and print it as a `String`.

```
The name is {{ cat }} and my nemesis is {{ mouse }}.
```

The example above should transform into the following output (assuming the values `Tom` and `Jerry`).

```
The name is Tom and my nemesis is Jerry.
```

### If Command
This command will evaluate the value of the corresponding key as a boolean (according `TemplateModel#getAsBoolean`). If the value is `true` then the following block is evaluated. If the value is `false` then the `{{ #else }}` block is evaluated or the result is an empty `String` if it is not present. 


```text
Can we do inline with if only? {{ #if yes }} Yes we can!{{ #done }}

Can we do inline with else? {{ #if no }} Yes we can! {{ #else }} Sure we can!{{ #done }}


BTW: The extra empty line is there on purpose. The next whitespace character following a block command (those with #) is considered part of that command.

We can also do multiple lines.
{{ #if yes }}
Tom is great.
{{ #else }}
Jerry is better.
{{ #done }}
Now this will be right under.
```

Evaluating the example above (assuming `yes == true` and `no == false`) results in the following output
```text
Can we do inline with if only? Yes we can!
Can we do inline with else? Sure we can!
            
BTW: The extra empty line is there on purpose. The next whitespace character following a block command (those with #) is considered part of that command.

We can also do multiple lines.
Tom is great.
Now this will be right under.
```

### For Command
This command evaluates the following block for each item in an iterable. 
The item is also added into the model under the specified name.

```text
The name is {{ name }} and the surname is {{ surname }}.

Other known names:
{{ #for name : names }}
    - {{ name }} {{ surname }}
{{ #done }}

Now the name is {{ name }} again.
```

Given a model with values `"Nibbles"`, `"Disney"` and `["Butch", "Toodles", "Quacker"]` the template will evaluate to the following output

```text
The name is Nibbles and the surname is Disney.
                        
Other known names:
    - Butch Disney
    - Toodles Disney
    - Quacker Disney
            
Now the name is Nibbles again.
```
### Formal Description

The following is a grammar in [EBNF](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form) describing the language of our templates (using convention such as "+" to indicate one or more matches, and "?" for optionality).

```
/*
    Basic primitives  (? ... ? here is not optionality but special sequence)
*/
text            =   ? one or more characters excluding <open> ? ;
name            =   ? one or more alphanumerical characters or . ? ;
blank           =   ? zero or more whitespace characters ? ; 
space           =   ? single whitespace character or windows newline (\r\n) ?;

c               =   "#" ;
open            =   "{{", blank ;
close           =   blank, "}}" ;
closeb          =   close, space?;
done            =   open, c, "done", closeb ;


/* 
    {{ variable }} 
*/
cmdPrint        =   open, name , close


/* 
    {{ #if variable }} positive block {{ #else }} negative block {{ #done }}
    {{ #if variable }} positive block {{ #done }}
*/
if              =   open, c, "if", blank, name, closeb ;
else            =   open, c, "else", closeb ;
cmdIf           =   if, template, ( else, template )?, done ;


/*
    {{ #for variable : iterable }} block {{ #done }}
*/
in              =   ":"
for             =   open, c, "for", blank,  name,  blank, in, blank, name, closeb ;
cmdFor          =   for, template, done ;

/*
    all types of commands (print, if, for)
*/
command         =   cmdPrint | cmdIf | cmdFor;

/*
    final template is a sequnce of <text> and <cmd> in arbitrary order
*/
template        =   (text | command )+
```

*Disclaimer: We don't claim that the grammar is 100% formally correct*

### Implementation Notes
This time there is a significant amount of code provided as part of the initial codebase.
While you don't have to use any of the provided code -- except for the interfaces which are strictly required by tests -- it is designed to help you implement the rest of the assignment properly. In any case, understanding the code should be helpful even when you decide not to use it directly.  

### Running the Application
Once again we are creating a library. We encourage you to create a `Demo` class where you can tinker with your template engine. Provided tests should help you understand how everything should work together. 

Although there is not going to be any executable application, you should be now able to combine all you've learned during the course and create one if you want to (at least when using the code provided with the first homework as a starting point). However, this is not part of the assignment! 
