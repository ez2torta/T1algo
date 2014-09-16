import java.nio.ByteBuffer;

public class Vertex{
	int xpos;
	int ypos;
	public Vertex(int x, int y){
		this.xpos = x;
		this.ypos = y;
	}
	public void vertexBufFill(int pos, byte[] file){
		ByteBuffer.wrap(file, pos, 4).putInt(this.xpos);
		pos += 4;
		ByteBuffer.wrap(file, pos, 4).putInt(this.ypos);
		pos += 4;
	}
}