**Seminar project**

Almost complete app for playing chess.


**HW01 Display**

An app which encodes numbers 0-9 as 7-segment digits.

**HW02 Messaging**

A simple interface implementing message broker system.


**HW02 Templates**

A simple app which parses the text, finds commands inside the text, executes them and returns modified text.

E. g.

Variables:

    name: "Nibbles"

    surname: "Disney"

    names: ("Butch", "Toodles", "Quacker")

Input:

    The name is {{ name }} and the surname is {{ surname }}.
                
    Other known names:
    {{ #for name : names }}
        - {{ name }} {{ surname }}
    {{ #done }}
                
    Now the name is {{ name }} again.
    
Output:

    The name is Nibbles and the surname is Disney.
                
    Other known names:
        - Butch Disney
        - Toodles Disney
        - Quacker Disney
                
    Now the name is Nibbles again.


Disclaimer: Not all code was written by me, some parts were prepared by lecturers.
