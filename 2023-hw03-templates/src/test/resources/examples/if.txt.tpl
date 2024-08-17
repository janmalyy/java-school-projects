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