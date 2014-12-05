
public class ColumnVals {
	
	public float[] vals;
	
	public ColumnVals(int size){
		
	this.vals = new float[size];	
		
	}
	
	public void addElement(float ele, int index) {
		 
		this.vals[index] = ele;
		
	}

}
