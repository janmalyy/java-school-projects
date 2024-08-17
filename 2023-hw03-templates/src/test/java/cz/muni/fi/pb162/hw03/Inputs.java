package cz.muni.fi.pb162.hw03;

public final class Inputs {

    public static final String PRINT = """
            The name is {{ cat }} and my nemesis is {{ mouse }}.
            """;

    public static final String PRINT_CS = """
            The cat named {{ cat }} says "Příliš žluťoučký kůň úpěl ďábelské ódy".
            """;
    public static final String IF = """
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
            """;

    public static final String FOR = """
            The name is {{ name }} and the surname is {{ surname }}.
                        
            Other known names:
            {{ #for name : names }}
                - {{ name }} {{ surname }}
            {{ #done }}
                        
            Now the name is {{ name }} again.
            """;

    public static final String IF_NESTED_IN_ELSE = """
            {{ #if no }}
            First condition is true.
            {{ #else }}
            {{ #if no }}
            Second condition is true.
            {{ #else }}
            Second condition is false.
            {{ #done }}
            First condition is false.
            {{ #done }}
            """;

    public static final String IF_ELSE_MISSING_DONE = """
            {{ #if yes }}
            Condition is true.
            {{ #else }}
            Condition is false.
            No done
            """;

    public static final String COMPLEX = """
            Hello,
                        
            this template engine has several built-in features:
                        
            Variables:
                The name is {{ cat }} and my nemesis is {{ mouse }}.
                The age is {{ age }} and the condition is {{ yes }}.
            Conditions:
                First boolean value is {{ #if yes }} true.{{ #else }} false.{{ #done }}
                        
                The list of names is {{ #if names }} populated.{{ #else }} empty.{{ #done }}
                        
            ForEach Loop:
                The names are:
            {{ #for name : names }}
                - {{ name }} {{ surname }}
            {{ #done }}
            """;

    private Inputs() {
        // intentionally private
    }
}
