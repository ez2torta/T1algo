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
			if(intersect(r,root.rectangles[i]) && root.sons[i] == null && lvl == maxlvl)
				rectangulos.add(root.rectangles[i]);
			if(intersect(r,root.rectangles[i]) && root.sons[i] != null){
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
	public static void insertar2(Rectangle r, Node raiz){
		if(raiz.rectangles[0] == null)
			raiz.rectangles[0] = r;
		else{
			raiz.sons[0] = new Node();
			insertar2(r, raiz.sons[0]);
		}
	}
	public static void main(String[] args){
		Node node = new Node();
		Rectangle r = new Rectangle(new Vertex(2,2),1,1);
		Rectangle s = new Rectangle(new Vertex(1,2),3,3);
		Rectangle t = new Rectangle(new Vertex(3,2),1,2);
		Rectangle u = new Rectangle(new Vertex(1,2),3,1);
		/*node.rectangles[0] = r;
		node.rectangles[1] = s;
		node.rectangles[2] = t;
		node.rectangles[3] = u;*/
		insertar2(r, node);
		insertar2(s, node);
		insertar2(t, node);
		insertar2(u, node);
		buscar(u, node);
		/*System.out.println(node.rectangles[0].v[0].xpos+" "+r.v[0].ypos);
		System.out.println(node.rectangles[0].v[1].xpos+" "+r.v[1].ypos);
		System.out.println(node.rectangles[0].v[2].xpos+" "+r.v[2].ypos);
		System.out.println(node.rectangles[0].v[3].xpos+" "+r.v[3].ypos);*/
		System.out.println(((Rectangle)rectangulos.get(0)).v[0].ypos);
	}
}