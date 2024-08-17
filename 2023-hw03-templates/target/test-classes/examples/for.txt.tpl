The name is {{ name }} and the surname is {{ surname }}.

Other known names:
{{ #for name : names }}
    - {{ name }} {{ surname }}
{{ #done }}

Now the name is {{ name }} again.