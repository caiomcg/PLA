program Test3; {programa para teste do lexico}
var
   NUMERO, m : integer;
   final, teste : integer;
   bool : boolean;
   NUMB : real;
 
procedure findMin(x, y, z: integer; war: integer);
var
    m : integer;
begin

   if true -> false and bool then
     m := x;
  else
     m := y;

   if x > y then
      m := x;
   else
      m := y;
   
   if z > m then
      m := z+1;
end;
 
begin  {tente gerar um erro usando um caracter nao permitido.. tipo $}
   NUMERO := 3 * 5 + 10 - 9;
end.
