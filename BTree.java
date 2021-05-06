import java.io.File;


public class BTree{
	
	private BTreeNode root;
	private int height;
	private int n;
	private int t; 
	private Long val; 
	private int frequency; 
	private File myFile;
	//private int BTreeNodeCount = 1;
	
	public BTree(int degrees, File aFile) {
		this.root = new BTreeNode(degrees); 
		this.height = 0; 
		this.n = 0; 
		this.t = degrees;
		this.myFile = aFile; 
		
	}
	
	// NEED TO DO
	public Long BTreeSearchFrequency(long key) {
		if(this.root == null) {
			return -1L; 
		}
		return root.BTreeSearchFrequency(key); 
	}
	
	//Search
	public Long search(Long k) {
		if (this.root == null)
			return -1L;
		else
			return this.root.search(k);
	}
	
	
	//Insert
	public void BTreeInsert(long k) {
		BTreeNode r = root;
		if (r.n == 2 * t - 1) {
			BTreeNode s = new BTreeNode();
			root = s;
			s.leaf = false;
			s.n = 0;
			s.setChild(1, r);
			BTreeSplitChild(s, 1);
			BTreeInsertNonfull(s, k);
		} else {
			BTreeInsertNonfull(r, k);
		}
	}
	
	//Insert Nonfull
	private void BTreeInsertNonfull(BTreeNode x, long k) {
		int i = x.n;
		if (x.leaf) {
			beMyKey lookUpKey = x.keys.get(i);
			while (i > 0 && k < lookUpKey.keyValue) {
				x.setKey(i + 1, x.keys.get(i));
				i--;
			}
			x.setKey(i, new beMyKey(k));
			x.n++;

			x.writeToFile();
		} else {
			while (i > 0 && k < x.keys.get(i).keyValue) {
				i--;
			}
			i++;

			if (x.child(i).n == 2 * t - 1) {
				BTreeSplitChild(x, i);
				if (k > x.keys.get(i).keyValue) {
					i++;
				}
			}
			BTreeInsertNonfull(x.child(i), k);
		}
	}
	
	//BTreeSplitChild
	private void BTreeSplitChild(BTreeNode x, int i) {
		BTreeNode z = new BTreeNode();
		BTreeNode y = x.child(i);
		z.leaf = y.leaf;
		z.n = t - 1;

		for (int j = 1; j <= t - 1; j++) {
			z.setKey(j, y.keys.get(j + t));
		}

		if (!y.leaf) {
			for (int j = 1; j <= t; j++) {
				z.setChild(j, y.child(j + t));
			}
		}

		y.n = t - 1;

		for (int j = x.n = 1; j >= i + 1; j--) {
			x.setChild(j + 1, x.child(j));
		}
		x.setChild(i + 1, z);

		for (int j = x.n; j >= i; j--) {
			x.setKey(j + 1, x.keys.get(j));
		}

		x.setKey(i, y.keys.get(t));
		x.n++;

		x.writeToFile();
		z.writeToFile();
		y.writeToFile();

	}
	
	
	//String Stuff
	public String toString() {
		return "Value:" + val + " Frequency"+ frequency;
	}
	
	
	
}
