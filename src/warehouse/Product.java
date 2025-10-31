package warehouse;

class Product {
	  private int id;
	  private String name;
	  private int quantity;
	  private int reorderThreshold;

	  // Constructor
	  public Product(int id2, String name, int quantity, int reorderThreshold) {
	      this.id = id2;
	      this.name = name;
	      this.quantity = quantity;
	      this.reorderThreshold = reorderThreshold;
	  }

	  // Getters and Setters
	  public int getId() { 
		  return id; 
	  }
	  
	  public String getName() { 
		  return name; 
	  }
	  
	  public int getQuantity() { 
		  return quantity; 
	  }
	  
	  public int getReorderThreshold() { 
		  return reorderThreshold; 
	  }

	  public void setQuantity(int quantity) { 
		  this.quantity = quantity; 
	  }
	  
	  @Override
	    public String toString() {
	        return "ID: " + id + ", Name: " + name + 
	               ", Quantity: " + quantity + 
	               ", Reorder Level: " + reorderThreshold;
	    }
	}
