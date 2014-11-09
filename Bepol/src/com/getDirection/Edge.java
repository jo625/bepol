package com.getDirection;

//Edge (Bigraph �ε�)
public class Edge  {
	  private final String id;    		// ���� �̸�
	  private final Vertex source;		// ���� ����
	  private final Vertex destination; // ���� ����
	  private final int weight; 		// ����ġ
	  
	  //Edge ����
	  public Edge(String id, Vertex source, Vertex destination, int weight) {
	    this.id = id;
	    this.source = source;
	    this.destination = destination;
	    this.weight = weight;
	  }
	  
	  public String getId() { //id ����
	    return id;
	  }
	  public Vertex getDestination() { //�������� ����
	    return destination; 
	  }

	  public Vertex getSource() { //�������� ����
	    return source;
	  }
	  public int getWeight() { //����ġ ����
	    return weight;
	  }
	  
	  @Override
	  public String toString() {
	    return source + " " + destination;
	  }
	  
	  
	} 