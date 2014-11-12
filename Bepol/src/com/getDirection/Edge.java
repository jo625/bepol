package com.getDirection;

//Edge (Bigraph 양방향 그래프)
public class Edge  {
	  private final String id;    		// Id of Edge
	  private final Vertex source;		// Start Vertex
	  private final Vertex destination; // End Vertex
	  private final int weight; 		// Distance
	  
	  //Edge 생성자
	  public Edge(String id, Vertex source, Vertex destination, int weight) {
	    this.id = id;
	    this.source = source;
	    this.destination = destination;
	    this.weight = weight;
	  }
	  
	  public String getId() { 			//id 반환 메소드
	    return id;
	  }
	  public Vertex getDestination() {  //End Vertex 반환 메소드
	    return destination; 
	  }

	  public Vertex getSource() { 		//Start Vertex 반환 메소드
	    return source;
	  }
	  public int getWeight() { 			//Vertex 사이 거리 반환 메소드
	    return weight;
	  }
	  
	  @Override
	  public String toString() {
	    return source + " " + destination;
	  }
	  
	  
	} 