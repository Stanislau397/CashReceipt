#CashReceipt
<hr>
A Project that allows you to build products cash receipt.
<hr>
<h1 style="margin-top: -20px">Technology stack</h1>
<hr>
1.Spring Boot
<br>
2.Spring data jpa
<br>
3.AspectJ
<br>
4.Lombok
<br>
5.Postgresql
<br>
6.Java 17
<hr>
<h1 style="margin-top: -20px">Start Instruction</h1>
<hr>
1. Clone repository from github
<br>
2. Run project from CashReceiptApplication.java
<br>
3. Open any browser and in search bar type http://localhost:8080/check?item=1-2&item=4-6&card=1
<br>
or http://localhost:8080/check?item=1-2&item=4-6
<hr>
<h1 style="margin-top: -20px">v0.2</h1>
<hr>
<h5>1.Added implementation of two caches.</h5>
<br>
* Least Recently Used. Consists of DoublyLinkedList 
that has head node and tail node. New node adds after head node.
When capacity of cache is reached a node before tail node gets removed.
<br>
<br>
* Least Frequently Used. Consists of valuesMap that stores all values.
KeyFrequencyMap that stores frequency of keys. FrequencyMap that
stores frequency and keys related to frequency in LinkedHashSet.
<br>
<h5>2.Added implementation of factory method for Cache. You can 
provide cache algorithm and capacity through application.yml
in resources folder.</h5>
<p>Example:</p>
<div style="background-color: lightgray;">
<p style="color: black">cache:</p>
<p style="color: black; margin-top: -20px; margin-left: 20px">capacity: 100</p>
<p style="color: black; margin-top: -20px; margin-left: 20px">algorithm: LRUCache</p>
</div>
<h5>3. Added proxy pattern for repository</h5>











