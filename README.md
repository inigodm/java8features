# java8features
Clase con lo nuevo de java 8 que hice en su momento por si tenia que recordar algo...

## STREAMS!!!!
A ver si adivinas que hace este trozo de codigo:
 
         Stream<String> s2 =
         listaPersonas.stream()
         .filter((p)->p.getEdad()==32)
         .map((p)->p.getNombre()) 
         .forEach((p)->System.out.println(p));

Basicamente; 
hace un stream de la lista de personas y le aplica un filtro: que solo queden los que tengan 32 años. Finalmente devuelve los nombres de esas personas en otro Stream y se los va mandando (los nombres) a System.out.
 
Como mola, ehh???? 
 
El caso es que voy a describir que son los Streams, asi tendre que explicar los lambdas y las interfaces funcionales y de ese modo lo mas importante de las novedades de java 8.
 
1. Los Streams son nuevas funcionalidades de java 8 que se fundamentan en el uso de lambdas para tratar colleciones de objetos
 
2. Las lambdas son nuevas funcionalidades de java 8 que se generan sobre interfaces funcionales.....

3. Los interfaces funcionales son nuevas funcionalidades de java 8 que se fundamentan en una nueva cualidad de java 8 que es que en las interfaces se puede meter codigo ademas de declaraciones de funciones a implementar

Vayamos de abajo arriba:

## Funciones con codigo en las interfaces? 
 
 Una nueva funcionalidad de java 8 (la ultima cuando version escribi esto, actualmente se han metido cosas para java 9 y cosicas para java 10) es que, cuando declaras una interfaz, (public Interface Interfaz{...}) puedes hacer a las interfaz implementaciones por defecto para uno o mas metodos (marcados con la palabra reservada default, ej:
 
         default public String method(){...hace n monton de cosas...}).
 
(los metodos default han de ser public y son overrideables, insisto: son implementaciones por defecto.)
 
Bastante parecidas a las Clases abstractas, verdad? SI. Es curioso la de cosas que hay que hacer por evitar la herencia multiple (en java 9 se permiten hasta que sean funciones privadas, estan a un paso de aceptar campos en las interfaces) 

Lo diferente que mola es que te permite heredar 'mejor', de un modo mas parecido a herencia multiple (solo puedes heredar una clase abstracta, pero las interfaces que quieras).

## Interfaz funcional?

Pues bien, una interfaz funcional es (DEFINICION): un subconjunto de interfaces en las que solo hay **UN** metodo **NO** default. Se definen asi. Ademas tienen que estar marcadas con la anotacion @FunctionalInterface (aunque esto es mas bien a efectos de errores de compilacion, no muy emocionante) 

Q ventajas da esto? que solo queda UN cacho por definir en la interfaz para poder convertirla en una clase que pueda ser instanciada...
 
## Y de esto nos aprovechamos para hacer LAMBDAS

Las lambdas no dejan de ser 'funciones inline' que no pertenecen a ninguna clase/objeto. Pero java es POO y las funciones sin clase a la que estar ligadas no tienen sentido.

Metete esta idea en tu cabeza: Las lambdas son 'sintactic sugar', cosas que el compilador transforma en codigo realmente compilable antes de compilar.

Para conseguir poder escribir estas 'funciones sin clase' lo que se hace es, se  coge la notacion lambda, se compilan a un objeto (que implementa una interfaz funcional) y se hace que la lambda sea la implementacion de la funcion que la interfaz funcional NO tiene implementadoa y asi las funciones pasan a ser...

OBJETOS!
  
Se entiende el concepto? Como tenemos una interfaz a la que solo hay que definir un metodo y una expresion lambda que no deja de ser un  metodo... Cerramos el circulo! mola!!!!!

Y asi podemos actuar sobre las funciones como objetos.
  
 ### Notacion Lambda
  
 Se divide, basicamente, en 2 partes:
  
          ((...parametros) -> cuerpo)
  
 Si el cuerpo son varias lineas se puede poner entre corchetes: 
 
         () -> {print("OLA"); print("KE"); print("ASE?")}
  
 Seria un ejemplo de lambda al que no se le pasa ningun parametro y que escribe: "OLA KE ASE?"
  
         (p) -> print(p.getNombre())
  
 Imprime el nombre de la persona que se le pasa como parametro
  
 Facil, verdad?
  
 Bueno, seguimos liando el asunto:
  
 Todas estas lambdas que vemos, como he dicho antes, son OBJETOS, como son objetos seran instancias de alguna clase, no? pues si.
  
 Efectivamente, son objetos de clases creadas de implementan @FunctionalInterface, java trae predefinidas algunas de ellas
  
   https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html
  
 La maquina virtual resuelve, automagicamente, las lambdas a interfaces funcionales si no se lo especificas tu. Me explico con ejemplos, que se ve facil:
 
   
          Predicate<T> Represents a predicate (boolean-valued function) of one
          argument.
  
De modo que si tenemos una lamba cualquiera a la que se le pasa 1 parametro y que devuelve 1 booleano la JVM, que es muy lista, se lo tomara como un Predicate. Predicate, en si, es una interfaz funcional con el metodo 'test(T t)' sin implementar y la funcion lambda sera la que rellene ese hueco para conseguir una clase completamente instanciable y funcional.
  
 De modo que si hacemos:
  
          Predicate<String> p = (t) -> t.equals("ok"); 
          p.test("ok"); // debiera ser true 
          p.test("ko"); // debiera ser false
          
 Siendo 'test(Object o)' te preguntaras? pues es la funcion que NO tiene definida la interfaz funcional 'Predicate' y que, en esta implementacion especifica es nuestra lamba, de modo que al compilar se rellenara ese metodo con ella quedando algo asi:
 
    public boolean test(Object obj){
       return obj.equals("ok")
    }
 
          
Predicate se usa en los filtros, hay mas, por ejemplo Function:
  
          Function<T,R> Represents a function that accepts one argument and
          produces a result.
  
 Una lambda que recibe un parametro de tipo T y devuelve otro de tipo R. p.e:
  
          (p) -> p.getNombre()
  
 Recibe Persona y devuelve String. En la interfaz Function se implementa el metodo apply(T t) con la lambda.
  
En el ejemplo hay una clase MapStrategy, que dependiendo del texto que se le mande, trata a la persona con un lambda (Function) u otro (realmente es una adaptacion que me he hecho del patron 'strategy' a lambdas)
  
A que mola?
  
Pues una ultima cosa, pequeña. Se ha introducido tambien un operador que es analogo al '.' pero que sirve para obtener referencias a FUNCIONES, por ejemplo:
  
          class Hey{ 
            public double square(double num){ 
              return Math.pow(num , 2); 
            } 
          }
 
          Hey hey = new Hey(); 
          Function<Double, Double> square = hey::square;
          double ans = square.apply(21);
  
Devuelve una referencia al metodo 'square' de la clase Hey, que recibe 1 argumentos y devuelve 1 String. Por tanto la puedo asociar a la interfaz funcional Function y ejecutar 'apply'
  
Otro ejemplo mas obvio esta en el codigo:
  
         System.out::println
  
Acepta un parametro de tipo String y no devuelve nada: es un Consumer<String>, luego
  
          Consumer<String> c = System.out::println;
          c.accept("Lalalalalaa!!!!");
  
 Escribe por consola 'Lalalalalaa!!!!' 
 
 
## Streams

Ahora lo de los streams ya no es tan emocionante: solo son conjuntos de datos que se pueden 'serializar' (convertir en un flujo) y hacerlos pasar por filtros, mapearlos, etcetera con lambdas.

Para conseguir un stream solo hay que ejecutar 'stream()' en algo que implemente iterable (creo recordar que es esa interfaz; Lists y Maps pueden ser streameados)

De hecho parallelStream() hace un stream que se ejecuta en paralelo en todas las CPUs de la maquina pudiendo obtenerse una mejora de rendimiento (no se recomienda usar en servidores de aplicaciones, porque se montaria un pollo de threads importante)

## Ejemplo final

Como por ejemplo pongo el primer ejemplo de este texto, pero despues de haber explicado todo seguro que se entiende mejor:

 
         Stream<String> s2 = listaPersonas
         .stream()
         .filter((p)->p.getEdad()==32) // aqui se mete un Predicate
         .map((p)->p.getNombre()) // aqui se mete una Function
         .forEach((p)->System.out.println(p)) // y esto es un Consumer
 
 Y con esto me despido. Ahora chequead el codigo de la clase (ejecutable segun esta) en el que hay mas ejemplitos. Enjoy.
 
 #Symmetry
 
