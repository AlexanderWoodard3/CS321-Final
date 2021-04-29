public class TreeObject{

private long key; 
private int sequenceData; 
private int duplicates; 

  public TreeObject(long key, int sequenceData){
    this.key = key; 
    this.sequenceData = sequenceData; 
    this.duplicates = 1;
  }
  
  //NEED TO DO
  public int increaseDuplicates(){
  }
  
  public void setDuplicates(){
  }
  
  public long  getKey(){
  }
  
  public void setKey(long key){
    
  }
  
  public int compareTo(TreeObject t){
    if(this.key > t.getKey()){
      return 1; //
    }else if(this.key < t.getKey()){
      return -1; 
    }else{
      return 0; 
  }



}
