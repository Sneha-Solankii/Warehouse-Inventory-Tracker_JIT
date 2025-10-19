package warehouse;

class Product {
	  private String id;
	  private String name;
	  private int quantity;
	  private int reorderThreshold;

	  // Constructor
	  public Product(String id, String name, int quantity, int reorderThreshold) {
	      this.id = id;
	      this.name = name;
	      this.quantity = quantity;
	      this.reorderThreshold = reorderThreshold;
	  }

	  // Getters and Setters
	  public String getId() { 
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
