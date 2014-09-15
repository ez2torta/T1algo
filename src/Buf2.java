import java.io.*;
import java.util.ArrayList;
public class Buf2 extends NodeSize{
	ArrayList<Integer> heap;
	RandomAccessFile raf;
	long ExtDir;
	byte[] RAMBuf;
	public Buf2() throws FileNotFoundException{
		this.heap = new ArrayList<Integer>();
		this.raf = new RandomAccessFile("rtree.obj","rw");
		this.ExtDir = -10L;
		this.RAMBuf = new byte[2*B];
	}
	public Node2 loadNode(long pos) throws IOException{
		readExtMem(pos,RAMBuf);
		Node2 n = new Node2(RAMBuf);
		this.ExtDir = pos; // necesario??
		return n;
	}
	public void saveNode(Node2 n) throws IOException{
		long posnodo = n.pos;
		writeExtMem(ExtDir, RAMBuf);
		n.writeBuffer(bufferRam[freeBuffer]);
		dirBuffer[freeBuffer] = posnodo;
		modificado[freeBuffer] = true;
		
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
		Buf2 buf = new Buf2();
		System.out.println(buf.B);
		System.out.println(buf.ExtDir);
	}
}
