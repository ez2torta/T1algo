public class Node{
	Node sons[];
	Rectangle rectangles[];
	public Node(){
		sons = new Node[4];
		rectangles = new Rectangle[4];
	}
	public int numRectangles(){
		int i = 0;
		while(i < 4){
			if(this.rectangles[i] == null)
				break;
			i++;
		}
		return i;
	}
	public boolean isLeaf(){
		if(this.sons[0] == null)
			return true;
		return false;
	}
	public int nextSon(Rectangle r){
		/* Usar funciones de rectangle */
		return 0;
	}
}