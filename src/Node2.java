import java.nio.ByteBuffer;

public class Node2 extends NodeSize{
	long pos;
	long sonsPos[];
	int isLeaf;
	Rectangle rectangles[];
	public Node2(long pos, int isLeaf){
		this.pos = pos;
		this.isLeaf = 1;
		this.rectangles = new Rectangle[2*t];
		this.sonsPos = new long[2*t];
		for(int i = 0; i < 2*t; i++)
			sonsPos[i] = 4*pos+i+1;
	}
	public Node2(byte[] file){
		int bytePos = 0;
		this.pos = ByteBuffer.wrap(file, bytePos, 8).getLong();
		bytePos += 8;
		this.isLeaf = ByteBuffer.wrap(file, bytePos, 4).getInt();
		bytePos += 4;
		this.rectangles =  new Rectangle[2*t];
		/*for (int i = 0; i < this.numRectangles(); i++) {
			rectangles[i] = new MBR(file, bytePos, dimension);
			bytePos += (16*dimension);
		}*/
		bytePos += 32*2*t;
		sonsPos = new long[2*t];
		for (int i = 0; i < 2*t; i++) {
			sonsPos[i] =  ByteBuffer.wrap(file, bytePos, 8).getLong();
			bytePos +=8;
		}
	}
	public int numRectangles(){
		int i = 0;
		while(i < 2*t){
			if(this.rectangles[i] == null)
				break;
			i++;
		}
		return i;
	}
	public boolean isRoot(){
		if((this.pos) == 0)
			return true;
		return false;
	}
	public boolean isLeaf(){
		if(this.isLeaf == 1)
			return true;
		return false;
	}
	/* Obtiene el nodo padre */
	/*public int getFather(){
		if(pos == 0)
			return -1;
		else if(pos%4 == 0)
			return (pos/4)-1;
		else
			return pos/4;
	}*/
	/* Obtiene el nodo hijo en la posición indicada entre 0 y 15 */
	public long getSonPos(int index){
		return this.sonsPos[index];
	}
	public void nodeBufFill(byte[] file){
		int bytePos = 0;
		ByteBuffer.wrap(archivo, pos, 4).putInt(leaf);
		pos += 4;
		ByteBuffer.wrap(archivo, pos, 8).putLong(posArchivo);
		pos += 8;
		myMbr.WriteByte(archivo, pos);
		pos += (16*dimension);
		for (int i = 0; i < numeroNodos; i++) {
			getMBRI(i).WriteByte(archivo, pos);
			pos += (16*dimension);
		}
		pos += (maxElem - numeroNodos) * 16 * dimension;
		
		for (int i = 0; i < numeroNodos; i++) {
			ByteBuffer.wrap(archivo, pos, 8).putLong(getArchivoHijo(i));
			pos +=8;
		}
		ByteBuffer.wrap(file, bytePos, 8).putLong(this.pos);
		bytePos += 8;
		ByteBuffer.wrap(file, bytePos, 4).putInt(this.isLeaf);
		bytePos += 4;
		for (int i = 0; i < numeroNodos; i++) {
			this.rectangles[i].rectBufFill(archivo, pos);
			bytePos += (16*dimension);
		}
		bytepos += (maxElem - numeroNodos) * 16 * dimension;
		sonsPos = new long[2*t];
		for (int i = 0; i < 2*t; i++) {
			sonsPos[i] =  ByteBuffer.wrap(file, bytePos, 8).getLong();
			bytePos +=8;
		}
	}
	public static void main(String[] args) throws Exception{
		Node2 node = new Node2(1,1);
		long s2 = node.getSonPos(0);
		System.out.println(s2);
	}
}
