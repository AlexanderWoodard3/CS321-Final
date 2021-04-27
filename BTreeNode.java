
public class BTreeNode {

  
  
  private void BTreeInsertNonfull(Node x, long x){
    int i = x.n; 
    if(x.leaf){
      while (i >= 1 && k < x.key(i).val){
        x.setKey(i + 1, x.key(i)); 
        i--; 
      }
      x.setKey(i+1, new Key(k)); 
      x.n++; 
      
      x.writeToFile(); 
    }else{
      while(i >= && k < x.key(i).val){
        i--; 
      }
      i++; 
      
      if(x.child(i).n == 2*t -1){
        BTreeSplitChild(x,i); 
        if(k >x.key(i).val){
          i++; 
        }
      }
      BTreeInsertNonfull(x.child(i), k); 
  }
  
  //NEED TO DO  
  publlic int BTreeSearchFrequency(long key){
    
  }
    
  public void BTreeInsert(long k){
   Node r = root; 
   if(r.n == 2*t-1){ 
     Node s = new Node(); 
     root = s; 
     s.leaf = false; 
     s.n = 0; 
     s.setChild(1, r); 
     BTreeSplitChild(s,1); 
     BTreeInsertNonfull(s, k); 
   }else{
     BTreeInsertNonfull(r,k); 
  } 
    
    
  private void BTreeSplitChild(Node x, int i){
    Node z = new Node(); 
    Node y = x.child(i); 
    z.leaf = y.leaf; 
     z.n = t-1; 
    
    for(int j = 1; j <=t-1; j++){
      z.setKey(j, y.key(j+t)); 
    }
    
    if(!y.leaf){
      for(int j = 1; j<=t; j++){
        z.setChild(j,y.child(j+T)); 
      }
    }
    
    y.n = t-1; 
    
    for(int j = x.n=1; j >= i+1; j--){
      x.setChild(j+1, x.child(j)); 
    }
    x.setChild(i+1, z); 
    
    for(int j = x.n; j >= i; j--){
      x.setKey(j +1, x.key(j)); 
    }
    
    x.setKey(i, y.key(t)); 
    x.n++; 
    
    x.writeToFile(); 
    z.writeToFile(); 
    y.writeToFile(); 
 
  }
    
  public String toString(){ 
    return stringRepr(0); 
  }
    
  String stringRepr(int level){
    string x = ""; 
    for(int i=0; i < level; i++){ 
      x += "   "; 
    } 
    
    x += "Node [n = " + n + ", leaf = " + leaf + "]\n"; 
    
    for(int i =1; i <= n; i++){
      for(int z = 0; z <level; z++){
        x += "   "; 
      }
      x += "   " + key(i).val + " (" + new String(key(i).sequence())
    }
    for(int i = 1; i <= n+1 && i <= childre.size(); i++){
      x += child(i).stringRepr(level+1); 
    }
    
    return x; 
   }
  } 
    
    
}
