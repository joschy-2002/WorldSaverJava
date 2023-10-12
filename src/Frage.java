public class Frage {

	public String frage;
	public String a1,a2,a3;
	public boolean b1, b2, b3;
	
	public Frage(String frage, String a1, String a2, String a3) {
		this.frage = frage;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.b1 = true;
		this.b2 = false;
		this.b3 = false;
	}

	public String[] getAntworten() {
	    String[] antworten = {a1, a2, a3};
	    return antworten;
	}
}