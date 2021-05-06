import java.io.IOException;
import java.nio.ByteBuffer;
//import org.apache.commons.compress.utils.ByteUtils; 
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;

		long fileIndex;
		long nextAvailableFileIndex; 
		int n;
		int t;
		boolean leaf;
		//private File myFile;
		
		ArrayList<beMyKey> keys;
		ArrayList<Long> children;
		ArrayList<BTreeNode> nodeChildren; 
		
public class BTreeNode {
	

		//New BTreeNode
		public BTreeNode() { 
			n = 0;
			t = 0;
			leaf = true;
			
			keys = new ArrayList<beMyKey>();
			children = new ArrayList<Long>();
			nodeChildren = new ArrayList<BTreeNode>(); 
			
			fileIndex = nextAvailableFileIndex;
			nextAvailableFileIndex += BTreeNodeSizeOnDisk();
		}
		
		public BTreeNode(int degree) { 
			n = 0;
			this.t = degree;
			leaf = true;
			
			keys = new ArrayList<beMyKey>();
			children = new ArrayList<Long>();
			nodeChildren = new ArrayList<BTreeNode>(); 
			
			fileIndex = nextAvailableFileIndex;
			nextAvailableFileIndex += BTreeNodeSizeOnDisk();
		}
	
		//Disk
		int BTreeNodeSizeOnDisk(){
	    	return ( 2 * t) * 64 + ( 2 * t - 1) * ( 64 + 32 ); 
		}
		
		//ADD A Search 
		public Long search(Long akey) {
			//BTreeNode x = this; 
			if (leaf) {
				for (int j = 0; j < this.n; j++) {
					beMyKey lookUpKey = this.keys.get(j);
					if (akey.equals(lookUpKey.keyValue)) {
						return lookUpKey.keyValue;
					}
				}
			} else { //A Tree - is the top of the Tree 
				for (int j = 0; j < this.n; j++) {
					beMyKey lookUpKey = this.keys.get(j); 
					if (akey.equals(lookUpKey.keyValue)) {
						lookUpKey.frequency++;
						return lookUpKey.keyValue;
					}
					if (j + 1 == this.n || (akey < lookUpKey.keyValue)) {
						return nodeChildren.get(j).search(akey); //This is the spot to modify to read from Disk
					}
				}
			} 
			return -1L;
			
		}
		
		public Long BTreeSearchFrequency(Long akey) {
			//BTreeNode x = this; 
			if (leaf) {
				for (int j = 0; j < this.n; j++) {
					beMyKey lookUpKey = this.keys.get(j);
					if (akey.equals(lookUpKey.keyValue)) {
						return lookUpKey.frequency;
					}
				}
			} else { //A Tree - is the top of the Tree 
				for (int j = 0; j < this.n; j++) {
					beMyKey lookUpKey = this.keys.get(j); 
					if (akey.equals(lookUpKey.keyValue)) {
						lookUpKey.frequency++;
						return lookUpKey.frequency;
					}
					if (j + 1 == this.n || (akey < lookUpKey.keyValue)) {
						return nodeChildren.get(j).BTreeSearchFrequency(akey); //This is the spot to modify to read from Disk
					}
				}
			} 
			return -1L;
			
		}
		
		//GetBTreeNode
		BTreeNode getBTreeNode(long idx){
		    BTreeNode newBTreeNode = null; 
		   /* LinkedList<BTreeNode> BTreeNodeList = cache.returnAllitems(); 
		    for (BTreeNode n : BTreeNodeList){
		      if(n.fileIndex == idx){
		        newBTreeNode = n; 
		        return newBTreeNode; 
		      }
		    }*/
		    return newBTreeNode; 
		  }
		
		//Child
		BTreeNode child(int i) {
			long offset = children.get(i - 1);

			BTreeNode BTreeNode = new BTreeNode();
			BTreeNode.readFromFile(offset);
			return BTreeNode;
		}
		
		void setChild(int i, BTreeNode c) {
			if (i - 1 == children.size()) {
				children.add(c.fileIndex);
				nodeChildren.add(c); 
			}else {
				children.set(i - 1, c.fileIndex);
				nodeChildren.set(i-1, c); 
			}
		}


		//KeyStuff
		public void setKey(int i, beMyKey key) {
			if (i - 1 == keys.size())
				keys.add(key);
			else
				keys.set(i - 1, key);
		}

		
		
		
		//TOString Stuff
		public String toString() {
			return stringRepr(0);
		}
		String stringRepr(int level) {
			String x = "";
			for (int i = 0; i < level; i++) {
				x += "   ";
			}

			x += "BTreeNode [n = " + n + ", leaf = " + leaf + "]\n";

			for (int i = 1; i <= n; i++) {
				for (int z = 0; z < level; z++) {
					x += "   ";
				}
				x += "   " + keys.get(i).keyValue + " (" + new String(keys.get(i).sequence());
			}
			for (int i = 1; i <= n + 1 && i <= children.size(); i++) {
				x += child(i).stringRepr(level + 1);
			}

			return x;
		}

	

		
		//Write-to-file and Read-from-file
		void writeToFile(){
		      try{
		        ByteBuffer buffer = ByteBuffer.allocate(BTreeNodeSizeOnDisk()); 
		        file.seek(fileIndex); 
		        
		        int numChildren = n +1; 
		        
		        for( int i = 0; i < 2 * t -1; i++){
		          if(i <n){
		            beMyKey k = keys.get(i); 
		            byte[] valb = ByteUtils.longToBytes(k.keyValue); 
		            buffer.put(valb); 
		            buffer.put(ByteUtils.intToBytes(k.frequency)); 
		          }else{
		            buffer.put(ByteUtils.longToBytes(-1)); 
		            buffer.put(ByteUtils.intToBytes(-1L)); 
		          }
		        }
		        
		        for(int i = 0; i < 2 * t; i++){
		          if(numChildren > i && !leaf){
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
				BTreeNode newBTreeNode = null;

				if (useCache)
					newBTreeNode = getBTreeNode(fileIndex);
				if (useCache && cache.getObject(newBTreeNode)) {
					this.fileIndex = newBTreeNode.fileIndex;
					this.n = newBTreeNode.n;
					this.leaf = newBTreeNode.leaf;
					this.keys = newBTreeNode.keys;
					this.children = newBTreeNode.children;
				} else {
					n = 0;

					for (int i = 0; i < 2 * t - 1; i++) {
						byte[] vb = new byte[8];
						myFile.read(vb);
						long value = ByteUtils.bytesToLong(vb);
						Long frequency = file.readInt();

						if (value >= 0) {
							beMyKey k = new beMyKey(value);
							k.frequency = (Long) frequency;
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
		
}
