package DBProject;

public class DataPoint {
    public int ind;
    public float val;
    public DataPoint nextDataPoint;
    public static int SIZE = 16;

    public DataPoint(int ind, DataPoint nxtDP) {
        this.ind = ind;
        this.nextDataPoint = nxtDP;
    }
    public void swapIndex() {
        Object[] tempArray = new Object[]{this.ind,this.nextDataPoint.ind};
        this.ind = (int)tempArray[1];
        this.nextDataPoint.ind = (int)tempArray[0];
    }
    public void swapIndex(DataPoint node) {
        Object[] tempArray = new Object[]{this.ind,node.ind};
        this.ind = (int)tempArray[1];
        node.ind = (int)tempArray[0];
    }
    public void swapValue() {
        Object[] tempArray = new Object[]{this.val,this.nextDataPoint.val};
        this.val = (float)tempArray[1];
        this.nextDataPoint.val = (float) tempArray[0];
    }
    public void swapValue(DataPoint node) {
        Object[] tempArray = new Object[]{this.val,node.val};
        this.val = (float)tempArray[1];
        node.val = (float)tempArray[0];
    }
}
