import java.util.ArrayList;
public class RTree2{
	public static int t = 2;
	private static int maxlvl = 0;
	private static ArrayList<Node2> tree = new ArrayList<Node2>();
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
	public static void RTreeHeight(int root, int lvl){
		if(lvl > maxlvl)
			maxlvl = lvl;
		for(int i = 0; i < 15; i++){
			if(tree.get(root).getSon(i) < tree.size())
				RTreeHeight(tree.get(root).getSon(i), lvl+1);
		}
	}
	public static void buscarAux(Rectangle r, int root, int lvl){
		for(int i = 0; i < 2*t; i++){
			Node2 aux = tree.get(tree.get(root).getSon(i));
			if(tree.get(root).rectangles[i] == null)
				continue;
			else if(intersect(r,tree.get(root).rectangles[i]) && aux == null && lvl == maxlvl)
				rectangulos.add(tree.get(root).rectangles[i]);
			else if(intersect(r,tree.get(root).rectangles[i]) && aux != null){
				for(int j = 0; j < 2*t; j++)
					buscarAux(r, tree.get(root).getSon(j), lvl+1);
			}
		}
	}
	public static void buscar(Rectangle r, int root){
		maxlvl = 0;
		RTreeHeight(root, 0);
		buscarAux(r, root, 0);
	}
	public static void insertar(Rectangle r, int root){
		int n = tree.get(root).numRectangles();
		if(tree.get(root).isLeaf()){
			if(n < 4)
				tree.get(root).rectangles[n] = r;
			else
				System.out.println("Mno");
		}
		else{
			int index = minIncrement(r, tree.get(root).rectangles);
			insertar(r, tree.get(root).getSon(index));
		}
	}
	public static Rectangle[] maxIncrement(Rectangle r, Rectangle[] rs){
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
		//max = -1;
		//mininc = -1;
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
	public static int minIncrement(Rectangle r, Rectangle[] rs){
		int result = -1;
		double min = Double.MAX_VALUE;
		double mininc = -1;
		double aux;
		double inc, a1, a2;
		for(int i = 0; i < 4; i++){
			inc = r.getIncreasedArea(rs[i]);
			a1 = r.getArea();
			a2 = rs[i].getArea();
			aux = inc - (a1+a2);
			if(aux < min || (aux == min && mininc > inc)){
				min = aux;
				mininc = inc;
				result = i;
			}
		}
		//max = -1;
		//mininc = -1;
		/*for(int i = 0; i < 4; i++){
			for(int j = i+1; j < 4; j++){
				inc = rs[i].getIncreasedArea(rs[j]);
				a1 = rs[i].getArea();
				a2 = rs[j].getArea();
				aux = inc - (a1+a2);
				if(aux < min || (aux == min && mininc > inc)){
					min = aux;
					mininc = inc;
					result[0] = rs[i];
					result[1] = rs[j];
				}
			}
		}*/
		return result;
	}
	public void QuadraticSplit(Rectangle r, int node){
		
	}
	public static void main(String[] args){
		Node2 node = new Node2(0);
		tree.add(node);
		Rectangle r = new Rectangle(new Vertex(0,0),1,1);
		Rectangle s = new Rectangle(new Vertex(3,3),1,1);
		Rectangle t = new Rectangle(new Vertex(9,9),1,1);
		Rectangle u = new Rectangle(new Vertex(10,10),1,1);
		Rectangle x = new Rectangle(new Vertex(8,8),1,1);
		insertar(r, 0);
		insertar(s, 0);
		insertar(t, 0);
		insertar(u, 0);
		insertar(x, 0);
		//insertar(u, node);
		//buscar(x, 0);
		System.out.println(rectangulos.size());
		Rectangle[] recs;
		recs = maxIncrement(x, node.rectangles);
		System.out.println(recs[0].toString()+' '+ recs[1].toString());
		int index = minIncrement(x, node.rectangles);
		System.out.println(node.rectangles[index].toString());
		//System.out.println(tree.get(node.getSon(2)));
	}
}