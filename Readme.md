<h1>CashReceipt</h1>
A Project that allows you to build products cash receipt.
<hr>
<h1 style="margin-top: -20px">Technology stack</h1>
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
1. Clone repository from github
<br>
2. Run project from CashReceiptApplication.java
<br>
3. Open any browser and in search bar type http://localhost:8080/check?item=1-2&item=4-6&card=1
<br>
or http://localhost:8080/check?item=1-2&item=4-6
<hr>
<h1 style="margin-top: -20px">v0.2</h1>
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
<h3>3. Added proxy pattern for repository</h3>

* Save - saves an object to database then saves it to cache.
  <br>
* Delete - deletes an object from database then deletes it from cache
  <br>
* Update - updated an object in database then updates it in cache
  <br>
* FindById - if an object is not present in cache then finds it in database, puts it into cache and returns it.
  <h3>4. Added regex for product name field</h3>
  *Name must start with the capital letter.
<br>
  *Name must not have any special characters except (")
<h3>Added new end points</h3>
<h5>DiscountCard: </h5>
1.http://localhost:8080/discountCard/update
<img src="images/discount_updatepng.png" wid>
2. http://localhost:8080/discountCard/delete/1
<img src="images/discount_delete.png">
3. http://localhost:8080/discountCard/add
<img src="images/add_discount_card.png">
4. http://localhost:8080/discountCard/155
<img src="images/find_discount.png">
<h5>Product: </h5>
5. http://localhost:8080/products/save
<img src="images/save_product.png">
6. http://localhost:8080/products/delete/152
<img src="images/delete_product.png">
7. http://localhost:8080/products/update
<img src="images/update_product.png">
8. http://localhost:8080/products/1
<img src="images/find_product.png">





