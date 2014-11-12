package com.getDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;*/

/* Dijkstra Test 클래스 
 * 지워도 상관없음
 */
public class TestDijkstraAlgorithm {

  //Nodes, Edges
  private List<Vertex> nodes;
  private List<Edge> edges;

  //Test
  public void testExcute() {
	//Vertex, Edge에 대한 ArrayList
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    
    
//    for (int i = 0; i < 12; i++) {
//      Vertex location = new Vertex("Node_" + i, "Node_" + i);
//      nodes.add(location);
//    }
    
    //�����߰�
    //�ܹ����̶� ������� ���� �� �� �߰��������
    addLane("Edge_0", 0, 1, 15);
    addLane("Edge_1", 0, 2, 5);
    addLane("Edge_2", 1, 4, 5);
    addLane("Edge_3", 2, 3, 2);
    addLane("Edge_4", 3, 6, 13);
    addLane("Edge_5", 4, 5, 2);
    addLane("Edge_6", 5, 7, 13);
    addLane("Edge_7", 6, 7, 10);
    addLane("Edge_8", 6, 8, 10);
    addLane("Edge_9", 7, 9, 10);
    addLane("Edge_10", 8, 10, 10);
    addLane("Edge_11", 9, 11, 10);
    addLane("Edge_12", 10, 11, 15);
    addLane("Edge_13", 1, 0, 15);
    addLane("Edge_14", 2, 0, 5);
    addLane("Edge_15", 4, 1, 5);
    addLane("Edge_16", 3, 2, 2);
    addLane("Edge_17", 6, 3, 13);
    addLane("Edge_18", 5, 4, 2);
    addLane("Edge_19", 7, 5, 13);
    addLane("Edge_20", 7, 6, 10);
    addLane("Edge_21", 8, 6, 10);
    addLane("Edge_22", 9, 7, 10);
    addLane("Edge_23", 10, 8, 10);
    addLane("Edge_24", 11, 9, 10);
    addLane("Edge_25", 11, 10, 15);

    // Lets check from location Loc_1 to Loc_10
    //���� ������ �׷����� �Ű����� ���� �� ����
    Graph graph = new Graph(nodes, edges);
    
    //���ͽ�Ʈ�� ��ü ����
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);

    
    //Execute(����� ����, ��ü ���� etc)
    dijkstra.execute(nodes.get(11));
    
    //getPath(������) ������ ��� ���Ḯ��Ʈ�� 
    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(0));

    /* assertNotNull(path);
    assertTrue(path.size() > 0);*/
    
    //��ΰ� �������� �ʴ� ��� �׽�Ʈ..
    //�ִ�ġ�� �ѱ�� ���(���� ���� ���� ��..)=> �̰� App �󿡼� �ϸ� �ɵ�..
    
    if(path==null){
    	System.out.println("��ΰ� �������� �ʽ��ϴ�.");
    }else{
	    for (Vertex vertex : path) {
	      System.out.println(vertex);
	    }
    }
    
  }

  //Edge �߰�
  private void addLane(String laneId, int sourceLocNo, int destLocNo,
      int duration) { //Edge �̸�, �����, ������, ����ġ
    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    edges.add(lane);
  }
} 