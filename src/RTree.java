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
		this.nextDir = 2*B;
		this.RAMBuf = new byte[2*B];
	}
	public Node nodeToRAM(long pos) throws IOException{
		readExtMem(pos,RAMBuf);
		Node n = new Node(RAMBuf);
		//System.out.println("read "+pos);
		return n;
	}
	public void nodeToExt(Node n) throws IOException{
		n.nodeBufFill(RAMBuf);
		writeExtMem(n.pos, RAMBuf);
		//System.out.println("write "+n.pos);
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
		/*for(int i = 0; i < aux.numRectangles; i++){
			if(aux.rectangles[i] == null)
				System.out.println("paf");
			else
				System.out.println("en buscar: "+aux.rectangles[i].toString());
		}*/
		for(int i = 0; i < aux.numRectangles; i++){
			//long sonDir = aux.sonsPos[i];
			//System.out.println("sonDir: "+sonDir);
			if(intersect(r,aux.rectangles[i]) && aux.isLeaf == 1)
				buscarResult.add(aux.rectangles[i]);
			else if(intersect(r,aux.rectangles[i]) && aux.isLeaf == 0){
				buscar(r, aux.sonsPos[i]);
				//System.out.println("spos "+aux.sonsPos[i]);
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
				this.QuadraticSplit(r, aux);
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
	public void QuadraticSplit(Rectangle r, Node node) throws IOException{
		if(node.numRectangles >= 2*t){
			int[] maxRs = this.maxIncrement(r, node);
			Node son1 = new Node(nextDir,node.isLeaf);
			nextDir += 2*B;
			Node son2 = new Node(nextDir,node.isLeaf);
			nextDir += 2*B;
			/* Si el rect�ngulo a insertarse no es semilla, se mete al arreglo en lugar de la semilla. */
			if(maxRs[0] != -1)
				node.rectangles[maxRs[0]] = r;
			/* Se guardan las semillas en los nodos nuevos. */
			if(maxRs[0] == -1)
				son1.putRectangle(r);
			else
				son1.putRectangle(node.rectangles[maxRs[0]]);
			son2.putRectangle(node.rectangles[maxRs[1]]);
			/* Se borra del arreglo el otro nodo semilla. */
			node.rectangles[maxRs[1]] = null;
			int bestDif = -1;
			int bestInd = 0;
			int g1 = 0;
			int g2 = 0;
			for(int i = 0; i < 2*t; i++){
				if(son1.numRectangles == t+1){
					for(int j = 0; j < 2*t; j++){
						if(node.rectangles[j] != null)
							//System.out.println("lastInsert "+i+": "+j);
							son2.putRectangle(node.rectangles[j]);
					}
					break;
				}
				else if(son2.numRectangles == t+1){
					for(int j = 0; j < 2*t; j++){
						if(node.rectangles[j] != null)
							//System.out.println("lastInsert "+i+": "+j);
							son1.putRectangle(node.rectangles[j]);
					}
					break;
				}
				for(int j = 0; j < 2*t; j++){
					if(node.rectangles[j] == null)
						continue;
					if(son1.numRectangles > 0)
						g1 = son1.getMBR().getIncreasedArea(node.rectangles[j]) - son1.getMBR().getArea();
					if(son2.numRectangles > 0)
						g2 = son2.getMBR().getIncreasedArea(node.rectangles[j]) - son2.getMBR().getArea();
					if(Math.abs(g1-g2) > bestDif){
						bestDif = Math.abs(g1-g2);
						bestInd = j;
					}
				}
				//System.out.println("bestInd "+i+": "+bestInd);
				if(g1 < g2)
					son1.putRectangle(node.rectangles[bestInd]);
				else if(g2 < g1)
					son2.putRectangle(node.rectangles[bestInd]);
				else if(son1.getMBR().getArea() < son2.getMBR().getArea())
					son1.putRectangle(node.rectangles[bestInd]);
				else if(son1.getMBR().getArea() < son2.getMBR().getArea())
					son2.putRectangle(node.rectangles[bestInd]);
				else if(son1.numRectangles < son2.numRectangles)
					son1.putRectangle(node.rectangles[bestInd]);
				else
					son2.putRectangle(node.rectangles[bestInd]);
				node.rectangles[bestInd] = null;
				bestDif = -1;
			}
			if(node.fatherPos == -1){ // si es la raiz
				Node newRoot = new Node(node.pos,0);
				newRoot.fatherPos = node.fatherPos;
				newRoot.putRectangle(son1.getMBR());
				newRoot.putRectangle(son2.getMBR());
				son1.fatherPos = newRoot.pos;
				son2.fatherPos = newRoot.pos;
				newRoot.isLeaf = 0;
				newRoot.sonsPos[0] = son1.pos;
				newRoot.sonsPos[1] = son2.pos;
				this.nodeToExt(newRoot);
				this.nodeToExt(son1); //guardar nodos
				this.nodeToExt(son2);
			}
			else{
				Node aux = this.nodeToRAM(node.fatherPos);
				for(int i = 0; i < aux.numRectangles; i++){
					if(aux.sonsPos[i] == node.pos){
						son1.pos = aux.sonsPos[i];
						this.nodeToExt(son1); //guardar nodos
						this.nodeToExt(son2);
						aux.putRectangle(son1.getMBR());
						if(aux.numRectangles < 2*t)
							aux.putRectangle(son2.getMBR());
						this.QuadraticSplit(son2.getMBR(), aux);
						break;
					}
				}
			}
		}
		else
			node.putRectangle(r);
	}
	public int numRandom(int min, int max){
		return ((int)(Math.random()*max-min+1));
	}
	public static void main(String[] args) throws IOException{
		RTree tree = new RTree();
		Node node = new Node(0,1);
		tree.nodeToExt(node);
		int x1,y1,w,h;
		Rectangle r;
		for(int i = 0; i < 2*tree.t+1; i++){
			x1 = tree.numRandom(0, 90);
			y1 = tree.numRandom(0, 90);
			w = tree.numRandom(0, 10);
			h = tree.numRandom(0, 10);
			r = new Rectangle(new Vertex(x1,y1),w,h);
			tree.insertar(r, 0);
		}
		tree.buscar(new Rectangle(new Vertex(0,0),100,100),0);
		node = tree.nodeToRAM(8192);
		for(int i = 0; i < node.numRectangles; i++)
			System.out.println("Rectangle "+i+": "+node.rectangles[i].toString());
		System.out.println("buscarResultSize: "+tree.buscarResult.size());
		for(int i = 0; i < tree.buscarResult.size(); i++)
			System.out.println("buscarResult "+i+": "+tree.buscarResult.get(i).toString());
		System.out.println(node.sonsPos[0]);
	}
}