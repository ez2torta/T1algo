import java.io.*;
import java.util.ArrayList;
public class Buf extends NodeSize{
	ArrayList<Integer> heap;
	RandomAccessFile raf;
	long ExtDir;
	byte[] RAMBuf;
	public Buf() throws FileNotFoundException{
		this.heap = new ArrayList<Integer>();
		this.raf = new RandomAccessFile("rtree.obj","rw");
		this.ExtDir = -10L;
		this.RAMBuf = new byte[2*B];
	}
	public Node loadNode(long pos) throws IOException{
		readExtMem(pos,RAMBuf);
		Node n = new Node(RAMBuf);
		return n;
	}
	public void saveNode(Node n) throws IOException{
		//ExtDir = n.pos;
		n.nodeBufFill(RAMBuf);
		writeExtMem(ExtDir, RAMBuf);
		
	}
	private void writeExtMem(long pos, byte[] file) throws IOException{
		raf.seek(pos);
		raf.write(file);
	}
	
	private void readExtMem(long pos, byte[] file) throws IOException{
		raf.seek(pos);
		raf.read(file);
	}
	public static void main(String[] args) throws Exception{
		Buf buf = new Buf();
		buf.ExtDir = 0;
		Node node = new Node(0,1);
		Rectangle r = new Rectangle(new Vertex(0,0),1,1);
		Rectangle s = new Rectangle(new Vertex(3,3),1,1);
		Rectangle t = new Rectangle(new Vertex(9,9),1,1);
		Rectangle u = new Rectangle(new Vertex(10,10),1,1);
		node.putRectangle(r);
		node.putRectangle(s);
		node.putRectangle(t);
		node.putRectangle(u);
		buf.saveNode(node);
		Node node2 = buf.loadNode(0);
		System.out.println(node.numRectangles);
		System.out.println(node2.numRectangles);
		System.out.println(node.rectangles[0].toString());
		System.out.println(node2.rectangles[0].toString());
		System.out.println(node.sonsPos[29]);
		System.out.println(node2.sonsPos[29]);
		System.out.println(buf.RAMBuf.length);
		//for(int i = 8180; i < 8191; i++)
			//System.out.print((long)(buf.RAMBuf[i]));
	}
}
