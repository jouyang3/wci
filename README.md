wci
===

Writing Compilers and Interpreters: A Software Engineering Approach

=== BEGIN CH5 OUTPUT ===

001 BEGIN
002 	BEGIN {Temperature conversion.}
003 		five := -1 + 2 -3 + 4 +3;
004 		ratio := five/9.0;
005 		
006 		fahrenheit := 72;
007 		centigrade := (fahrenheit -32)*ratio;
008 		
009 		centigrade := 25;
010 		fahrenheit := centigrade/ratio + 32;
011 		
012 		centigrade := 25;
013 		fahrenheit := 32 + centigrade /ratio;
014 	END;
015 	
016 	{Runtime division by zero error.}
017 	dze := fahrenheit/(ratio - ratio);
018 	
019 	BEGIN {Calculate a square root using Newton's method.}
020 		number := 2;
021 		root := number;
022 		root := (number/root + root)/2;
023 	END;
024 	
025 	ch := 'x';
026 	str := 'hello, world'
027 END.

                  27 source lines.
                   0 syntax errors.
                0.02 seconds total parsing time.

===== INTERMEDIATE CODE =====


<COMPOUND line="1">
    <COMPOUND line="2">
        <ASSIGN line="3">
            <VARIABLE id="five" level="0" />
            <ADD>
                <ADD>
                    <SUBTRACT>
                        <ADD>
                            <NEGATE>
                                <INTEGER_CONSTANT value="1" />
                            </NEGATE>
                            <INTEGER_CONSTANT value="2" />
                        </ADD>
                        <INTEGER_CONSTANT value="3" />
                    </SUBTRACT>
                    <INTEGER_CONSTANT value="4" />
                </ADD>
                <INTEGER_CONSTANT value="3" />
            </ADD>
        </ASSIGN>
        <ASSIGN line="4">
            <VARIABLE id="ratio" level="0" />
            <FLOAT_DIVIDE>
                <VARIABLE id="five" level="0" />
                <REAL_CONSTANT value="9.0" />
            </FLOAT_DIVIDE>
        </ASSIGN>
        <ASSIGN line="6">
            <VARIABLE id="fahrenheit" level="0" />
            <INTEGER_CONSTANT value="72" />
        </ASSIGN>
        <ASSIGN line="7">
            <VARIABLE id="centigrade" level="0" />
            <MULTIPLY>
                <SUBTRACT>
                    <VARIABLE id="fahrenheit" level="0" />
                    <INTEGER_CONSTANT value="32" />
                </SUBTRACT>
                <VARIABLE id="ratio" level="0" />
            </MULTIPLY>
        </ASSIGN>
        <ASSIGN line="9">
            <VARIABLE id="centigrade" level="0" />
            <INTEGER_CONSTANT value="25" />
        </ASSIGN>
        <ASSIGN line="10">
            <VARIABLE id="fahrenheit" level="0" />
            <ADD>
                <FLOAT_DIVIDE>
                    <VARIABLE id="centigrade" level="0" />
                    <VARIABLE id="ratio" level="0" />
                </FLOAT_DIVIDE>
                <INTEGER_CONSTANT value="32" />
            </ADD>
        </ASSIGN>
        <ASSIGN line="12">
            <VARIABLE id="centigrade" level="0" />
            <INTEGER_CONSTANT value="25" />
        </ASSIGN>
        <ASSIGN line="13">
            <VARIABLE id="fahrenheit" level="0" />
            <ADD>
                <INTEGER_CONSTANT value="32" />
                <FLOAT_DIVIDE>
                    <VARIABLE id="centigrade" level="0" />
                    <VARIABLE id="ratio" level="0" />
                </FLOAT_DIVIDE>
            </ADD>
        </ASSIGN>
    </COMPOUND>
    <ASSIGN line="17">
        <VARIABLE id="dze" level="0" />
        <FLOAT_DIVIDE>
            <VARIABLE id="fahrenheit" level="0" />
            <SUBTRACT>
                <VARIABLE id="ratio" level="0" />
                <VARIABLE id="ratio" level="0" />
            </SUBTRACT>
        </FLOAT_DIVIDE>
    </ASSIGN>
    <COMPOUND line="19">
        <ASSIGN line="20">
            <VARIABLE id="number" level="0" />
            <INTEGER_CONSTANT value="2" />
        </ASSIGN>
        <ASSIGN line="21">
            <VARIABLE id="root" level="0" />
            <VARIABLE id="number" level="0" />
        </ASSIGN>
        <ASSIGN line="22">
            <VARIABLE id="root" level="0" />
            <FLOAT_DIVIDE>
                <ADD>
                    <FLOAT_DIVIDE>
                        <VARIABLE id="number" level="0" />
                        <VARIABLE id="root" level="0" />
                    </FLOAT_DIVIDE>
                    <VARIABLE id="root" level="0" />
                </ADD>
                <INTEGER_CONSTANT value="2" />
            </FLOAT_DIVIDE>
        </ASSIGN>
    </COMPOUND>
    <ASSIGN line="25">
        <VARIABLE id="ch" level="0" />
        <STRING_CONSTANT value="x" />
    </ASSIGN>
    <ASSIGN line="26">
        <VARIABLE id="str" level="0" />
        <STRING_CONSTANT value="hello, world" />
    </ASSIGN>
</COMPOUND>

                   0 instructions generated.
                0.00 seconds total code generation time.

=== END CH5 OUTPUT ===