# CS321-F
CS321 final project (group based)

Group Members:
Alexander Woodard - GeneBankCreateBTree.java, READMe.md
Olivia Coca - BTree.java, BTreeNode.java, ByteUtils.java, beMyKey.java, TreeObject.java
Brian Wiggins - GeneBankSearch.java, Cache.java

Description of the B-Tree file:
The BTree.java class supplies the methods used in the classes GeneBankSearch and GeneBankCreateBTree.
It uses BTreeNode, beMyKey, ByteUtils, and TreeObject to work. As I write this BTree.java and BTreeNode.java
hace various bugs/errors and do not have some of the methods needed to make GeneBankCreateBTree.java work.

Cache Improvement:
As I write this our code does not compile, but just from knowing what a cache does and your
explaination in class the differnece between 100 and 500 in performance is almost nothing. The
real improvement happens once you have a size of 12 for your cache.