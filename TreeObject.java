public class TreeObject{

private long key; 
private int sequenceData; 
private int duplicates; 
private int frequency; 

  public TreeObject(long key, int sequenceData){
    this.key = key; 
    this.sequenceData = sequenceData; 
    this.duplicates = 1;
  }
  
  public int increaseDuplicates(){
    this.duplicates++; 
  }
  
  public void setDuplicates(int duplicates){
    this.duplicates = duplicates; 
  }
  
  public long  getKey(){
    return this.key; 
  }
  
  public void setKey(long key){
    this.key = key; 
    
  }
  
  public int getFrequency(){ 
    return this.frequency; 
  }
  
  public void increaseFrequency(){
    this.frequency++; 
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
