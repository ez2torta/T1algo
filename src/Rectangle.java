public class Rectangle{
	Vertex[] v;
	public Rectangle(Vertex v2, double ancho, double altura){
		v = new Vertex[4];
		this.v[0] = new Vertex(v2.xpos,v2.ypos);
		this.v[1] = new Vertex(v2.xpos,v2.ypos+altura);
		this.v[2] = new Vertex(v2.xpos+ancho,v2.ypos+altura);
		this.v[3] = new Vertex(v2.xpos+ancho,v2.ypos);
	}
	public double getArea(){
		return (this.v[3].xpos-this.v[0].xpos)*(this.v[1].ypos-this.v[0].ypos);
	}
	public double getIncreasedArea(Rectangle r){
		/* Aplicar max o min */
		return (this.v[3].xpos-this.v[0].xpos)*(this.v[1].ypos-this.v[0].ypos);
	}
	public String toString(){
		return "("+v[0].xpos+","+v[0].ypos+") "+"("+v[1].xpos+","+v[1].ypos+") "+"("+v[2].xpos+","+v[2].ypos+") "+"("+v[3].xpos+","+v[3].ypos+")";
	}
}