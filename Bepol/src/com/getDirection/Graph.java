package com.getDirection;
import java.util.List;

//Graph Ŭ����
public class Graph {
  /* ���� ���� ��ü (����Ʈ)*/
  private final List<Vertex> vertexes;
  private final List<Edge> edges;

  //����
  public Graph(List<Vertex> vertexes, List<Edge> edges) {
    this.vertexes = vertexes;
    this.edges = edges;
  }

  public List<Vertex> getVertexes() { //��� ����Ʈ ����
    return vertexes;
  }

  public List<Edge> getEdges() {	 //���� ����Ʈ ����
    return edges;
  }  
}