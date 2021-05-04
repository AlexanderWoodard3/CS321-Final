import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

public class BTree {
	private Node root;
	private int height;
	private int n;
	private int nodeCount = 1;
	private Entry[] children = new Entry[4];
}

public class Node {
	long fileIndex;
	int n = 0;
	boolean leaf = true;
	ArrayList<Key> keys = new ArrayList<Key>();
	ArrayList<Long> children = new ArrayList<Long>();
	}

	private void BTreeInsertNonfull(Node x, long k) {
		int i = x.n;
		if (x.leaf) {
			while (i >= 1 && k < x.key(i).val) {
				x.setKey(i + 1, x.key(i));
				i--;
			}
			x.setKey(i + 1, new Key(k));
			x.n++;

			x.writeToFile();
		} else {
			while (i >= 1 && k < x.key(i).val) {
				i--;
			}
			i++;

			if (x.child(i).n == 2 * t - 1) {
				BTreeSplitChild(x, i);
				if (k > x.key(i).val) {
					i++;
				}
			}
			BTreeInsertNonfull(x.child(i), k);
		}
	}

	// NEED TO DO
	public Long BTreeSearchFrequency(long key) {

	}

	private Long search(Node x, Long key, int height) {
		Entry[] children = x.children;

		// external nde
		if (height == 0) {
			for (int j = 0; j < x.m; j++) {
				if (key.equals(x.children[j].key)) {
					return (Long) children[j].val;
				}
			}
		} else {
			for (int j = 0; j < x.m; j++) {
				if (j + 1 == x.m || less(key, x.children[j + 1].key))
					return search(x.children[j].nect, key, height - 1);
			}
		}
		return null;
	}

	private Long searchFrequency(Node x, Long key, int height) {
		Entry[] children = x.children;
		if (height == 0) {
			for (int j = 0; j < x.m; j++) {
				if (key.equals(children[j].key)) {
					children[j].frequency++;
					return (Long) children[j].val;
				}
			}
		} else {
			for (int j = 0; j < x.m; j++) {
				if (j + 1 == x.m || less(key, children[j + 1].key)) {
					return searchFrequency(children[j].nect, key, height - 1);
				}
			}
			return null;
		}
	}

	public BtreeNode search(int k) {
		if (this.root == null)
			return null;
		else
			return this.root.search(k);
	}
	
	public Long BTreeSearch(Node x, Long k) {
		int i = 1; 
		
		while( i <= x.n && k > x.key(i)) {
			i = i+1; 
		}
		if(i <= x.n && k==x.key(i)) {
			return(x,k);
		}else if(x.leaf){
			return null; 
		}else {
			return BTreeSearch(x.child(i), k); 
		}
	}
	
	public void BTreeInsert(long k) {
		Node r = root;
		if (r.n == 2 * t - 1) {
			Node s = new Node();
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

	private void BTreeSplitChild(Node x, int i) {
		Node z = new Node();
		Node y = x.child(i);
		z.leaf = y.leaf;
		z.n = t - 1;

		for (int j = 1; j <= t - 1; j++) {
			z.setKey(j, y.key(j + t));
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
			x.setKey(j + 1, x.key(j));
		}

		x.setKey(i, y.key(t));
		x.n++;

		x.writeToFile();
		z.writeToFile();
		y.writeToFile();

	}

	public String toString() {
		return stringRepr(0);
	}

	String stringRepr(int level) {
		String x = "";
		for (int i = 0; i < level; i++) {
			x += "   ";
		}

		x += "Node [n = " + n + ", leaf = " + leaf + "]\n";

		for (int i = 1; i <= n; i++) {
			for (int z = 0; z < level; z++) {
				x += "   ";
			}
			x += "   " + key(i).val + " (" + new String(key(i).sequence());
		}
		for (int i = 1; i <= n + 1 && i <= children.size(); i++) {
			x += child(i).stringRepr(level + 1);
		}

		return x;
	}

	}

	// Node Stuff

	public Node() {
		fileIndex = nextAvailableFileIndex;
		nextAvailableFileIndex += nodeSizeOnDisk();
	}

	int nodeSizeOnDisk(){
    	return ( 2 * t) * 64 + ( 2 * t - 1) * ( 64 + 32 ); 
	}

	Node getNode(long idx){
    Node newNode = null; 
    LinkedList<Node> nodeList = cache.returnAllitems(); 
    for (Node n : nodeList){
      if(n.fileIndex == idx){
        new Node = n; 
        return newNode; 
      }
    }
    return newNode; 
  }

	Node child(int i) {
		long offset = children.get(i - 1);

		Node node = new Node();
		node.readFromFile(offset);
		return node;
	}

	Key key(int i) {
		return keys.get(i - 1);
	}

	void setKey(int i, Key key) {
		if (i - 1 == keys.size())
			keys.add(key);
		else
			keys.set(i - 1, key);

	}

	void setChild(int i, Node c) {
		if (i - 1 == children.size())
			children.add(c.fileIndex);
		else
			child.set(i - 1, c.fileIndex);
	}

	void writeToFile(){
      try{
        ByteBuffer buffer = ByteBuffer.allocate(nodeSizeOnDisk()); 
        file.seek(fileIndex); 
        
        int numChildren = n +1; 
        
        for( int i = 0; i < 2 * t -1; i++){
          if(i <n){
            Key k = key.get(i); 
            byte[] valb = ByteUtils.longToBytes(k.val); 
            buffer.put(valb); 
            buffer.put(ByteUtils.intToBytes(k.frequency)); 
          }else{
            buffer.put(ByteUtils.longToBytes(-1)); 
            buffer.put(ByteUtils.intToBytes(-1)); 
          }
        }
        
        for(int i = 0; i < 2 * t; i++){
          if(numchildren > i && !leaf){
            buffer.put(ByteUtils.longToBytes(children.get(1))); 
          }else{
            buffer.put(ByteUtils.longToBytes(-1)); 
          }
        }
        
        file.write(buffer.array()); 
        
        if(useCache){
          cache.addObject(this); 
        }
        
        file.getFD().sync(); 
        
      	}catch(IOException e) {
    	  System.out.println("An error in writeToFile methode");
      	}
      
      }

	void readFromFile(long idx) {
		try {
			fileIndex = idx;
			file.seek(fileIndex);
			Node newNode = null;

			if (useCache)
				newode = getNode(FileIndex);
			if (useCache && cache.getObject(newNode)) {
				this.fileIndex = newNode.fileIndex;
				this.n = newNode.n;
				this.leaf = newNode.leaf;
				this.keys = newNode.keys;
				this.children = newNode.children;
			} else {
				n = 0;

				for (int i = 0; i < 2 * t - 1; i++) {
					byte[] vb = new byte[8];
					file.read(vb);
					long value = ByteUtils.bytesToLong(vb);
					int frequency = file.readInt();

					if (value >= 0) {
						Key k = new Key(value);
						k.frequency = frequency;
						keys.add(k);
						n++;
					}
				}
				for (int i = 0; i < 2 * t; i++) {
					byte[] cub = new byte[8];
					file.read(cub);

					long childRed = ByteUtils.bytesToLong(cub);

					if (childRed >= 0) {
						children.add(childRed);
					}
				}
				leaf = children.isEmpty();

				if (useCache)
					cache.addObject(this);
			}

		} catch (IOException e) {
			System.out.println("An error in readFromFile method");
		}
	}


