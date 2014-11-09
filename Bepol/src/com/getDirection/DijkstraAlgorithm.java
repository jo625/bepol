package com.getDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

  /* 노드 , 엣지 리스트 객체 */
  private final List<Vertex> nodes;
  private final List<Edge> edges;
  
  /* 방문, 미방문 노드 집합 객체 */
  private Set<Vertex> settledNodes;
  private Set<Vertex> unSettledNodes;
  
  /* Map 객체(거리, 전 노드) */
  private Map<Vertex, Vertex> predecessors; //1번째 vertex까지의 경로 중 바로 전 Vertex 를 알려주는 Map 객체
  private Map<Vertex, Integer> distance;	//출발지 부터 해당 Vertex 까지의 거리를 알려주는 객체

  //생성자
  public DijkstraAlgorithm(Graph graph) {
    // create a copy of the array so that we can operate on this array
    this.nodes = new ArrayList<Vertex>(graph.getVertexes());
    this.edges = new ArrayList<Edge>(graph.getEdges());
  }

  public void execute(Vertex source) {
    
	//생성
	settledNodes = new HashSet<Vertex>();
    unSettledNodes = new HashSet<Vertex>();
    distance = new HashMap<Vertex, Integer>();
    predecessors = new HashMap<Vertex, Vertex>();
    
    //Source 추가
    distance.put(source, 0); 		//Source로부터 source 거리 0
    unSettledNodes.add(source);     //미방문 노드에 추가
    
    //unSettledNodes Size가 0 이상일 때 까지
    while (unSettledNodes.size() > 0) {
      Vertex node = getMinimum(unSettledNodes); //제일 작은 거 채택
      settledNodes.add(node); 		//방문노드 집합에 추가
      unSettledNodes.remove(node);  //미방문노드 집합에서 제거
      findMinimalDistances(node);   //최소 거리 찾기(Distance, precedessor 갱신 및 추가)
    }
    
  }

  //최소 거리 찾기 
  private void findMinimalDistances(Vertex node) {
	//node에 대해서 인접 노트 리스트 객체 생성
    List<Vertex> adjacentNodes = getNeighbors(node);
    //모든 인접 노드에 대해서
    for (Vertex target : adjacentNodes) {    	
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) { //현재 타겟까지의 거리보다 node를 거친 거리가 더 짧으면
        distance.put(target, getShortestDistance(node) //타겟과 거리 갱신
            + getDistance(node, target));
        predecessors.put(target, node); //이전 노드에 추가
        unSettledNodes.add(target);     //타겟 미방문 노드 추가
      }
    }
  }

  private int getDistance(Vertex node, Vertex target) {
	//모든 엣지에 대해서
    for (Edge edge : edges) {
      if (edge.getSource().equals(node) //하나가 node고 하나가 target이면 
          && edge.getDestination().equals(target)) {
        return edge.getWeight(); //가중치 리턴
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<Vertex> getNeighbors(Vertex node) {
	//Vertex에 대해 리스트 객체 생성
    List<Vertex> neighbors = new ArrayList<Vertex>();
    //모든 Edge에 대해서 부모가 node와 같고 방문한것이 아니면
    for (Edge edge : edges) {
      if (edge.getSource().equals(node)
          && !isSettled(edge.getDestination())) {
        neighbors.add(edge.getDestination()); //neighbor 리스트에 추가
      }
    }
    return neighbors; //return
  }

  //최소 거리 인거 선택
  private Vertex getMinimum(Set<Vertex> vertexes) {
    Vertex minimum = null;
    for (Vertex vertex : vertexes) { //노드 집합 중에서
      if (minimum == null) { 		 //minimum 값이 null이면
        minimum = vertex;			 //vertex를 minimum으로 설정
      } else { 
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) { //minimum 보다 더 작은 거리가 있으면
          minimum = vertex;												  //그것을 Minimun Vertex로
        }
      }
    }
    return minimum; //제일 짧은 거리값 가진 노드 리턴
  }
  
  //방문 여부 테스트
  private boolean isSettled(Vertex vertex) {
    return settledNodes.contains(vertex); //방문에 존재하면 true 아니면 false
  }

  //getShortDistance
  private int getShortestDistance(Vertex destination) {
    Integer d = distance.get(destination); //Source로부터 destination거리
    if (d == null) { //거리가 null 이면 Infinity
      return Integer.MAX_VALUE;
    } else { 		 //있으면 거리 리턴
      return d;
    }
  }

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  //경로 리턴 (소스로부터 선택된 타겟까지의 경로를 리턴) 없으면 null을 리턴
  public LinkedList<Vertex> getPath(Vertex target) {
    LinkedList<Vertex> path = new LinkedList<Vertex>(); //vertex 연결 리스트 객체 생성
    
    Vertex step = target; //타겟
    // check if a path exists
    if (predecessors.get(step) == null) { //target 까지 경로에 해당하는 노드가 없으면 null(경로 없음)
      return null;
    }
    
    path.add(step); //Path에 step 추가
    
    while (predecessors.get(step) != null) { //predecessors 빌때까지
      step = predecessors.get(step); 		 //객체 받고
      path.add(step); 					     //Path에 추가
    }
    // Put it into the correct order
    Collections.reverse(path);				 //Path 경로 뒤집기
    return path;						     //path 리턴
  }

} 