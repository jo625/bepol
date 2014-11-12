package com.getDirection;
import java.util.List;

//Graph 클래스
public class Graph {
  /* Graph Vertex, Edge List*/
  private final List<Vertex> vertexes;
  private final List<Edge> edges;

  //Constructor
  public Graph(List<Vertex> vertexes, List<Edge> edges) {
    this.vertexes = vertexes;
    this.edges = edges;
  }

  public List<Vertex> getVertexes() { //get List of Vertexes
    return vertexes;
  }

  public List<Edge> getEdges() {	 //get List of Edges
    return edges;
  }  
}