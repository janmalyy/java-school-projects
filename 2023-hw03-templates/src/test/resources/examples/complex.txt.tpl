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