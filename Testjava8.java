import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 
 *         <h1>STREAMS!!!!</h1>
**/
public class Testjava8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Point t = new Point(1, 2);
		t.toString();
		ArrayList<Persona> milista = new ArrayList<Persona>();
		milista.add(new Persona("Txomin", 31));
		milista.add(new Persona("Alicia", 32));
		milista.add(new Persona("Jokin", 33));
		milista.add(new Persona("Jon", 33));
		milista.add(new Persona("Rober", 32));
		milista.add(new Persona("Ziortza", 31));
		milista.add(new Persona("Amaia", 31));
		milista.add(new Persona("Eneritz", 31));
		milista.add(new Persona("Iker", 32));
		
		System.out.println("SORTED BY NAME----------------------------------------");
		Collections.sort(milista, (Persona p1, Persona p2) ->  p1.getNombre().compareTo(p2.getNombre()));
		for (Persona p : milista) {
			System.out.println(p.getNombre());
		}
		
		System.out.println("SORTED BY AGE----------------------------------------");
		Collections.sort(milista,(Persona p1, Persona p2) ->  p1.getEdad().compareTo(p2.getEdad()));
		for (Persona p : milista) {
			System.out.println(p.getNombre());
		}
		
		System.out.println("FILTER AMAIA----------------------------------------");
		Stream<Object> s = milista.stream().filter((p) -> p.getNombre().equals("Amaia"))
				.map((p)->p.getEdad());
		s.forEach((p)->System.out.println(p));
		
		System.out.println("FILTER 31----------------------------------------");
		Stream<Object> s2 = milista.stream().filter((p) -> p.getEdad() == 31)
				.map((p)->p.getNombre());
		s2.forEach((p)->System.out.println(p));
		
		System.out.println("STRATEGY----------------------------------------");
		MapStrategy<Persona, String> mp = new MapStrategy<Persona, String>();
		mp.putStrategy("NAME", (Persona::getNombre));
		mp.putStrategy("AGE", ((p) -> p.getEdad() + ""));
		for (Persona p : milista) {
			System.out.println(mp.execute("AGE", p) + " " + mp.execute("NAME", p));
		}
		
		System.out.println("FILTER METHODS----------------------------------------");
		System.out.println("---32");
		filtrar(milista, (p)->p.getEdad()==32);
		System.out.println("---33");
		filtrar(milista, (p)->p.getEdad()==33);
		System.out.println("---31");
		filtrar(milista, (p)->p.getEdad()==31);
		
		System.out.println("FILTER METHODS(PARALLEL)----------------------------------------");
		System.out.println("---32");
		filtrarParalelo(milista, (p)->p.getEdad()==32);
		System.out.println("---33");
		filtrarParalelo(milista, (p)->p.getEdad()==33);
		System.out.println("---31");
		filtrarParalelo(milista, (p)->p.getEdad()==31);
		Callable<String> strCallable = (() -> {System.out.println("ddddd"); return "mmmm";});
		System.out.println(strCallable.call()); // prints "Hello world!"
		
		System.out.println("METHOD REFERENCE------------------------------------------------");
		Consumer<String> c = System.out::println;
		c.accept("Lalalalalaa!!!!");/*System.out.println("init");
		Long in =  new Date().getTime();
		for (int i = 0 ; i < 1000; i++){
			filtrar(milista, (p)->p.getEdad()==31);
		}
		Long fi = new Date().getTime();
		System.out.println(" fin " + (fi - in));
		in =  new Date().getTime();
		System.out.println("init");
		for (int i = 0 ; i < 1000; i++){
			filtrarParalelo(milista, (p)->p.getEdad()==31);
		}
		fi =  new Date().getTime();
		System.out.println(" fin " + (fi - in));**/
		
	}
	
	private static void filtrar(ArrayList<Persona> milista, Predicate<Persona> pr){
		Stream<Object> s2 = milista.stream().filter(pr)
				.map((p)->p.getNombre());
		s2.forEach((p)->System.out.println(p));
	}
	private static void filtrarParalelo(ArrayList<Persona> milista, Predicate<Persona> pr){
		Stream<Object> s2 = milista.parallelStream().filter(pr)
				.map((p)->p.getNombre());
		s2.forEach((p)->System.out.println(p));
	}

}



class Persona {
	 
	 private String nombre;
	 private Integer edad;
	public String getNombre() {
	 return nombre;
	 }
	 
	public void setNombre(String nombre) {
	 this.nombre = nombre;
	 }
	 
	public Persona(String nombre, int edad) {
	 super();
	 this.nombre = nombre;
	 this.edad = edad;
	 }

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	}


final class MapStrategy<T, R>{
	Map<String, Function<T, R>> inner = new HashMap<String, Function<T, R>>();
	
	public void putStrategy (String id, Function<T, R> p){
		inner.put(id,p);
	}
	
	public Object execute(String id, T p){
		return inner.get(id).apply(p);
	}
}

final class Lazy<T> {
    private volatile T value;
    public T getOrCompute(Supplier<T> supplier) {
        final T result = value; // Just one volatile read 
        return result == null ? maybeCompute(supplier) : result;
    }
    private synchronized T maybeCompute(Supplier<T> supplier) {
        if (value == null) {
            value = requireNonNull(supplier.get());
        }
        return value;
    }
	private T requireNonNull(T t) {
		return t;
	}
}

class Point {
    private final int x, y;
    private final Lazy<String> lazyToString;
    public Point(int x, int y) {
        this.x = x; 
        this.y = y;
        lazyToString = new Lazy<String>();
    }
    @Override
    public String toString() {
    	System.out.println(lazyToString.getOrCompute( () -> "(" + x + ", " + y + ")").toString());
        return lazyToString.getOrCompute( () -> "(" + x + ", " + y + ")");
    }
}

interface Callable<T> {
    public T call();
}
