import java.nio.ByteBuffer;

public class Node extends NodeSize{
	long pos;
	int isLeaf;
	int numRectangles;
	Rectangle rectangles[];
	long sonsPos[];
	public Node(long pos, int isLeaf){
		this.pos = pos;
		this.isLeaf = 1;
		this.numRectangles = 0;
		this.rectangles = new Rectangle[2*t];
		this.sonsPos = new long[2*t];
		for(int i = 0; i < 2*t; i++)
			sonsPos[i] = -1L;
	}
	public Node(byte[] file){
		int bytePos = 0;
		int xmin,ymin,xmax,ymax;
		this.pos = ByteBuffer.wrap(file, bytePos, 8).getLong();
		bytePos += 8;
		this.isLeaf = ByteBuffer.wrap(file, bytePos, 4).getInt();
		bytePos += 4;
		this.numRectangles = ByteBuffer.wrap(file, bytePos, 4).getInt();
		bytePos += 4;
		this.rectangles =  new Rectangle[2*t];
		for (int i = 0; i < this.numRectangles; i++) {
			xmin = ByteBuffer.wrap(file, bytePos, 4).getInt();
			bytePos += 4;
			ymin = ByteBuffer.wrap(file, bytePos, 4).getInt();
			bytePos += 12;
			xmax = ByteBuffer.wrap(file, bytePos, 4).getInt();
			bytePos += 4;
			ymax = ByteBuffer.wrap(file, bytePos, 4).getInt();
			bytePos += 12;
			rectangles[i] = new Rectangle(new Vertex(xmin,ymin), (xmax-xmin), (ymax-ymin));
			//System.out.println(i+" "+this.rectangles[i].toString());
		}
		//bytePos += 2*t*32 - this.numRectangles*32;
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
	public void putRectangle(Rectangle r){
		if(this.numRectangles < 2*t){
			this.rectangles[this.numRectangles] =  r;
			numRectangles++;
		}
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
		ByteBuffer.wrap(file, bytePos, 8).putLong(this.pos);
		bytePos += 8;
		ByteBuffer.wrap(file, bytePos, 4).putInt(this.isLeaf);
		bytePos += 4;
		ByteBuffer.wrap(file, bytePos, 4).putInt(this.numRectangles);
		bytePos += 4;
		for (int i = 0; i < this.numRectangles; i++) {
			for(int j = 0; j < 4; j++){
				ByteBuffer.wrap(file, bytePos, 4).putInt(this.rectangles[i].v[j].xpos);
				bytePos += 4;
				ByteBuffer.wrap(file, bytePos, 4).putInt(this.rectangles[i].v[j].ypos);
				bytePos += 4;
			}
		}
		//bytePos += 2*t*32 - this.numRectangles*32;
		for (int i = 0; i < 2*t; i++) {
			ByteBuffer.wrap(file, bytePos, 8).putLong(this.sonsPos[i]);
			bytePos +=8;
		}
	}
	public static void main(String[] args) throws Exception{
		Node node = new Node(0,1);
		//node.nodeBufFill();
		System.out.println(node);
	}
}
