public class Node2 {
	int pos;
	int isLeaf;
	double nothing1;
	double nothing2;
	double nothing3;
	Rectangle rectangles[];
	public Node2(int pos){
		this.pos = pos;
		this.isLeaf = 1;
		this.nothing1 = 0;
		this.nothing2 = 0;
		this.nothing3 = 0;
		rectangles = new Rectangle[15];
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
	public int getFather(){
		if(pos == 0)
			return -1;
		else if(pos%4 == 0)
			return (pos/4)-1;
		else
			return pos/4;
	}
	/* Obtiene el nodo hijo en la posición indicada entre 0 y 14 */
	public int getSon(int sonPos){
		return (4*this.pos)+sonPos+1;
	}
}
