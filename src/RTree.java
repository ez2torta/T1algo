import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.io.*;
public class RTree extends NodeSize{
	int maxlvl;
	ArrayList<Rectangle> buscarResult;
	ArrayList<Long> tree;
	RandomAccessFile raf;
	long nextDir;
	byte[] RAMBuf;
	public RTree() throws FileNotFoundException{
		this.maxlvl = 0;
		this.buscarResult = new ArrayList<Rectangle>();
		this.tree = new ArrayList<Long>();
		this.raf = new RandomAccessFile("rtree.obj","rw");
		this.nextDir = 0;
		this.RAMBuf = new byte[2*B];
	}
	public Node nodeToRAM(long pos) throws IOException{
		readExtMem(pos,RAMBuf);
		Node n = new Node(RAMBuf);
		return n;
	}
	public void nodeToExt(Node n) throws IOException{
		n.nodeBufFill(RAMBuf);
		writeExtMem(n.pos, RAMBuf);
	}
	private void writeExtMem(long pos, byte[] file) throws IOException{
		raf.seek(pos);
		raf.write(file);
	}
	
	private void readExtMem(long pos, byte[] file) throws IOException{
		raf.seek(pos);
		raf.read(file);
	}
	public boolean intersectAux(Vertex p, Rectangle r){
		if(p.xpos >= r.v[0].xpos && p.xpos <= r.v[2].xpos && p.ypos >= r.v[0].ypos && p.ypos <= r.v[2].ypos)
			return true;
		return false;
	}
	public boolean intersect(Rectangle r1, Rectangle r2){
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
	public void buscar(Rectangle r, long pos) throws IOException{
		Node aux = nodeToRAM(pos);
		for(int i = 0; i < aux.numRectangles; i++){
			long sonDir = aux.sonsPos[i];
			if(intersect(r,aux.rectangles[i]) && sonDir < 0 && aux.isLeaf == 1)
				buscarResult.add(aux.rectangles[i]);
			else if(intersect(r,aux.rectangles[i]) && sonDir >= 0 && aux.isLeaf == 0){
				for(int j = 0; j < 2*t; j++)
					buscar(r, aux.sonsPos[j]);
			}
		}
	}
	public void insertar(Rectangle r, long pos) throws IOException{
		Node aux = nodeToRAM(pos);
		if(aux.isLeaf == 1){
			if(aux.numRectangles < 2*t){
				aux.putRectangle(r);
				nodeToExt(aux);
			}
			else{
				System.out.println("kek");
			}
		}
		else{
			int index = this.minIncrement(r, aux);
			insertar(r, aux.sonsPos[index]);
		}
	}
	public int[] maxIncrement(Rectangle r, Node node){
		int[] result = new int[2];
		double max = -1;
		double mininc = -1;
		double aux;
		double inc, a1, a2;
		for(int i = 0; i < node.numRectangles; i++){
			inc = r.getIncreasedArea(node.rectangles[i]);
			a1 = r.getArea();
			a2 = node.rectangles[i].getArea();
			aux = inc - (a1+a2);
			if(aux > max || (aux == max && mininc > inc)){
				max = aux;
				mininc = inc;
				result[0] = -1;
				result[1] = i;
			}
		}
		//max = -1;
		//mininc = -1;
		for(int i = 0; i < 4; i++){
			for(int j = i+1; j < node.numRectangles; j++){
				inc = node.rectangles[i].getIncreasedArea(node.rectangles[j]);
				a1 = node.rectangles[i].getArea();
				a2 = node.rectangles[j].getArea();
				aux = inc - (a1+a2);
				if(aux > max || (aux == max && mininc > inc)){
					max = aux;
					mininc = inc;
					result[0] = i;
					result[1] = j;
				}
			}
		}
		return result;
	}
	public int minIncrement(Rectangle r, Node node){
		int result = -1;
		double min = Double.MAX_VALUE;
		double mininc = -1;
		double aux;
		double inc, a1, a2;
		for(int i = 0; i < node.numRectangles; i++){
			inc = r.getIncreasedArea(node.rectangles[i]);
			a1 = r.getArea();
			a2 = node.rectangles[i].getArea();
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
	public void QuadraticSplit(Rectangle r, Node node){
		int[] maxRs = this.maxIncrement(r, node);
		Node son1 = new Node(nextDir,node.isLeaf);
		nextDir++;
		Node son2 = new Node(nextDir,node.isLeaf);
		nextDir++;
		/* Si el rectángulo a insertarse no es semilla, se mete al arreglo en lugar de la semilla. */
		if(maxRs[0] != -1)
			node.rectangles[maxRs[0]] = r;
		/* Se borra del arreglo el otro nodo semilla. */
		node.rectangles[maxRs[1]] = null;
		/* Se guardan las semillas en los nodos nuevos. */
		if(maxRs[0] == -1)
			son1.putRectangle(r);
		else
			son1.putRectangle(node.rectangles[maxRs[0]]);
		son2.putRectangle(node.rectangles[maxRs[1]]);
		int bestDif = -1;
		int g1, g2;
		for(int i = 0; i < 2*t; i++){
			if(son1.numRectangles == t+1){
				for(int j = i; j < 2*t+1; i++){
					if(node.rectangles[j] != null)
						son2.putRectangle(node.rectangles[j]);
				}
				return;
			}
			else if(son2.numRectangles == t+1){
				for(int j = i; j < 2*t+1; i++){
					if(node.rectangles[j] != null)
						son1.putRectangle(node.rectangles[j]);
				}
				return;
			}
			for(int j = i+1; j < 2*t+1; j++){
				g1 = son1.areaMBR();
			}
		}	
		if(node.pos == 0){ // si es la raiz
			Node newRoot = new Node(0,0);
			newRoot.sonsPos[0] = son1.pos;
			newRoot.sonsPos[1] = son2.pos;
		}
	}
	public int numRandom(int min, int max){
		return ((int)(Math.random()*max-min));
	}
	public static void main(String[] args) throws IOException{
		RTree tree = new RTree();
		Node node = new Node(0,1);
		tree.nodeToExt(node);
		/*Rectangle r = new Rectangle(new Vertex(0,0),4,4);
		Rectangle s = new Rectangle(new Vertex(3,3),1,1);
		Rectangle t = new Rectangle(new Vertex(9,9),1,1);
		Rectangle u = new Rectangle(new Vertex(10,10),1,1);
		Rectangle x = new Rectangle(new Vertex(8,8),1,1);
		tree.insertar(r, 0);
		tree.insertar(s, 0);
		tree.insertar(t, 0);
		tree.insertar(u, 0);
		tree.insertar(x, 0);
		tree.buscar(s, 0);*/
		int x1,y1,w,h;
		Rectangle r;
		for(int i = 0; i < 2*tree.t; i++){
			x1 = tree.numRandom(0, 90);
			y1 = tree.numRandom(0, 90);
			w = tree.numRandom(0, 10);
			h = tree.numRandom(0, 10);
			r = new Rectangle(new Vertex(x1,y1),w,h);
			tree.insertar(r, 0);
		}
		tree.buscar(new Rectangle(new Vertex(0,0),20,20),0);
		node = tree.nodeToRAM(0);
		for(int i = 0; i < 2*tree.t; i++)
			System.out.println("Node "+i+": "+node.rectangles[i].toString());
		for(int i = 0; i < tree.buscarResult.size(); i++)
			System.out.println("buscarResult "+i+": "+tree.buscarResult.get(i));
		/*System.out.println("numRectangles: "+node.numRectangles);
		System.out.println("rect t: "+node.rectangles[2].toString());
		System.out.println("lastSonPos: "+node.sonsPos[29]);
		System.out.println("RAMBuf length: "+tree.RAMBuf.length);
		System.out.println("buscarResult: "+tree.buscarResult.size());
		Rectangle[] recs;
		recs = tree.maxIncrement(x, node);
		System.out.println("maxIncrement: "+recs[0].toString()+" - "+ recs[1].toString());
		int index = tree.minIncrement(x, node);
		System.out.println("minIncrement(x): "+node.rectangles[index].toString());*/
		//System.out.println(tree.numRandom(0,100));
	}
}