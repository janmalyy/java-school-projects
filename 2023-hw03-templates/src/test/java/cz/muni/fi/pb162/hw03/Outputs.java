package cz.muni.fi.pb162.hw03;

public final class Outputs {

    public static final String PRINT = """
            The name is Tom and my nemesis is Jerry.
            """;
    public static final String PRINT_CS = """
            The cat named Tom says "Příliš žluťoučký kůň úpěl ďábelské ódy".
            """;

    public static final String IF = """
            Can we do inline with if only? Yes we can!
            Can we do inline with else? Sure we can!
                        
            BTW: The extra empty line is there on purpose. The next whitespace character following a block command (those with #) is considered part of that command.
            
            We can also do multiple lines.
            Tom is great.
            Now this will be right under.
            """;

    public static final String FOR = """
            The name is Nibbles and the surname is Disney.
                        
            Other known names:
                - Butch Disney
                - Toodles Disney
                - Quacker Disney
                        
            Now the name is Nibbles again.
            """;

    public static final String IF_NESTED_IN_ELSE = """
            Second condition is false.
            First condition is false.
            """;

    public static final String COMPLEX = """
            Hello,
                        
            this template engine has several built-in features:
                        
            Variables:
                The name is Tom and my nemesis is Jerry.
                The age is 42 and the condition is true.
            Conditions:
                First boolean value is true.
                The list of names is populated.
            ForEach Loop:
                The names are:
                - Butch Disney
                - Toodles Disney
                - Quacker Disney
            """;

    private Outputs() {
        // intentionally private
    }
}
