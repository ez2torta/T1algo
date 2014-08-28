import java.util.ArrayList;
public class RTree{
	public static int t = 2;
	private static int maxlvl = 0;
	private static ArrayList<Rectangle> rectangulos = new ArrayList<Rectangle>();
	public static boolean intersectAux(Vertex p, Rectangle r){
		if(p.xpos >= r.v[0].xpos && p.xpos <= r.v[2].xpos && p.ypos >= r.v[0].ypos && p.ypos <= r.v[2].ypos)
			return true;
		return false;
	}
	public static boolean intersect(Rectangle r1, Rectangle r2){
		if(r1.v[0].xpos == r2.v[3].xpos || r1.v[0].ypos == r2.v[1].ypos ||
				r2.v[0].xpos == r1.v[3].xpos || r2.v[0].ypos == r1.v[1].ypos)
			return false;
		else if(intersectAux(r1.v[0],r2) || intersectAux(r1.v[1],r2) || 
				intersectAux(r1.v[2],r2) || intersectAux(r1.v[3],r2))
			return true;
		else if(intersectAux(r2.v[0],r1) || intersectAux(r2.v[1],r1) || 
				intersectAux(r2.v[2],r1) || intersectAux(r2.v[3],r1))
			return true;
		return false;
	}
	public static void RTreeHeight(Node root, int lvl){
		if(lvl > maxlvl)
			maxlvl = lvl;
		for(int i = 0; i < 2*t; i++){
			if(root.sons[i] != null)
				RTreeHeight(root.sons[i], lvl+1);
		}
	}
	public static void buscarAux(Rectangle r, Node root, int lvl){
		for(int i = 0; i < 2*t; i++){
			if(root.rectangles[i] == null)
				continue;
			else if(intersect(r,root.rectangles[i]) && root.sons[i] == null && lvl == maxlvl)
				rectangulos.add(root.rectangles[i]);
			else if(intersect(r,root.rectangles[i]) && root.sons[i] != null){
				for(int j = 0; j < 2*t; j++)
					buscarAux(r, root.sons[j], lvl+1);
			}
		}
	}
	public static void buscar(Rectangle r, Node root){
		maxlvl = 0;
		RTreeHeight(root, 0);
		buscarAux(r, root, 0);
	}
	public static void insertar(Rectangle r, Node root){
		int n = root.numRectangles();
		if(root.isLeaf()){
			if(n < 4)
				root.rectangles[n] = r;
			/*else
				root.split();*/
		}
		else{
			/*int index = root.nextSon(r);
			insertar(r, root.sons[index]);*/
		}		
	}
	public Rectangle[] maxIncrement(Rectangle r, Rectangle[] rs){
		Rectangle[] result = new Rectangle[2];
		double max = -1;
		double mininc = -1;
		double aux;
		double inc, a1, a2;
		for(int i = 0; i < 4; i++){
			inc = r.getIncreasedArea(rs[i]);
			a1 = r.getArea();
			a2 = rs[i].getArea();
			aux = inc - (a1+a2);
			if(aux > max || (aux == max && mininc > inc)){
				max = aux;
				mininc = inc;
				result[0] = r;
				result[1] = rs[i];
			}
		}
		max = -1;
		mininc = -1;
		for(int i = 0; i < 4; i++){
			for(int j = i+1; j < 4; j++){
				inc = rs[i].getIncreasedArea(rs[j]);
				a1 = rs[i].getArea();
				a2 = rs[j].getArea();
				aux = inc - (a1+a2);
				if(aux > max || (aux == max && mininc > inc)){
					max = aux;
					mininc = inc;
					result[0] = rs[i];
					result[1] = rs[j];
				}
			}
		}
		return result;
	}
	public void QuadraticSplit(Rectangle r, Node node){
		
	}
	public static void main(String[] args){
		Node node = new Node();
		Rectangle r = new Rectangle(new Vertex(2,2),1,1);
		Rectangle s = new Rectangle(new Vertex(1,2),3,3);
		Rectangle t = new Rectangle(new Vertex(3,2),1,2);
		Rectangle u = new Rectangle(new Vertex(1,2),3,1);
		Rectangle x = new Rectangle(new Vertex(1,3),1,1);
		insertar(r, node);
		insertar(s, node);
		insertar(t, node);
		//insertar(u, node);
		buscar(r, node);
		System.out.println(rectangulos.size());
	}
}