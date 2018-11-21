import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MST {
	private int parent[]; // 루트의 정보들을 가지고 있는 배열 선언.
	private int size; // 배열의 size.
	private int minCost = 0; // 최단 경로를 구했을때 경로들의 합을 구하기 위한 변수.
	private Edge k_array[]; // 출발->도착 정점의 정보와 가중치를 가지고 있는 간선 객체들의 배열.
	private boolean v_check[]; // 정점에 연결되어있는 간선이 존재하는지 체크하는 boolean 배열.

	// 초기 생성자, n 값을입력받음.
	public MST(int n) { // MST 초기화.
		this.size = n;
		this.parent = new int[size];
		this.v_check = new boolean[size];

		Arrays.fill(parent, -1);
		try {
			BufferedReader br = new BufferedReader(new FileReader("c:\\users\\KimDoYeon\\Desktop\\input.txt"));
			String line = br.readLine();
			String[] split_line = line.split(" ");
			// 버퍼 리더와 파일 리더를 통해 input.txt 파일을 읽어온다.
			int k_arraySize = Integer.parseInt(split_line[1]);
			k_array = new Edge[k_arraySize];
			System.out.println("k_array's size : " + k_array.length + "\n"); // 읽어온 파일에서 배열의 size를 추출한다.

			int count = 0;
			while (count < k_arraySize) {
				line = br.readLine(); // 한줄씩 읽어나가고.
				split_line = line.split(" "); // 띄어쓰기 단위로 잘라서.
				k_array[count] = new Edge(Integer.parseInt(split_line[0]), Integer.parseInt(split_line[1]),
						Integer.parseInt(split_line[2]), false); // k_array 배열을 매 줄을 읽을 때마다 만들어서 배열에 넣는다.
				count++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void weightedUnion(int i, int j) { // i가 속한 집합과 j 가 속한 집합을
		// 가중치 병합 한다.
		int temp = (-1) * (objectSize(i) + objectSize(j)); // 두 집합의 size를 더한 값에 -1을 곱하여 저장한다.

		if (objectSize(i) >= objectSize(j)) { // i가 속한 집합의 크기가 j가 속한 집합의 크기보다 크거나 같다면.
			parent[collapsingFind(j)] = i; // j의 TopRoot는 를 i로 바꾼다.
			parent[i] = temp; // 두 집합의 크기를 합한 값은 i에 들어간다.
		} else { // i가 속한 집합의 크기가 j가 속한 집합의 크기보다 작다면.
			parent[collapsingFind(i)] = j; // i의 TopRoot 를 j로 바꾼다.
			parent[j] = temp; // 두 집합의 크기를 합한 값은 j에 들어간다.
		}
	}
	

	public int collapsingFind(int a) { // a가 속한 집합의 TopRoot를 찾는다.
		if (parent[a] < 0) {
			return a; // collapsingFind가 실행되었을 때 첫 parent[a]가 음수라면
						// TopRoot 이므로 a를 반환한다.
		}
		if (parent[a] >= 0) { // parent[a] 가 양수라면 자식이므로.
			if (parent[parent[a]] < 0) { // 다음 element가 root 라면.
				return parent[a]; // parent[a]를 return한다.
			}
			return collapsingFind(parent[parent[a]]); // TopRoot가
			// 나올 때 까지 재귀함수를 돌린다.
		} else
			return a;

	}

	
	public int objectSize(int a) { // a가 속한 집합의 크기를 구한다.
		return (-1) * parent[collapsingFind(a)]; // TopRoot의 값은 음수이므로 양수로 변환한다.
	}

	
	public void kruskal() { // kruskal 알고리즘을 통해 최소비용 신장 트리를 만든다.
		Edge[] edges = k_array;
		sort(edges); // 간선 객체들의 배열을 오름차순 순으로 sorting 한다.

		for (int i = 0; i < edges.length; i++) { //
			// v와 w에 연결되어있는 간선이 있는지 확인. 둘다 true라면 사이클이 형성되어버리므로 제외하기 위함이다.
			if (!(v_check[edges[i].v] == true && v_check[edges[i].w] == true)) {
				weightedUnion(edges[i].v, edges[i].w); // 조건을 만족한다면 weightedUnion을 실행.
				k_array[i].selected = true; // 해당 간선이 사용되었음을 보여주기 위해 selected를 true로 세팅.

				v_check[edges[i].v] = true; // 해당 정점에 연결된 간선이 있음을 체크.
				v_check[edges[i].w] = true; // 해당 정점에 연결된 간선이 있음을 체크.

				if (edges[i].selected == true) { // 해당 간선이 사용되었다면.
					minCost = minCost + edges[i].weight; // 그 간선 객체의 weight들을 더해준다.
				}
			}
		}
		System.out.println("최소 신장 트리에 포함된 간선");
		for (int j = 0; j < k_array.length; j++) {
			if (k_array[j].selected == true) { // 사용되어진 간선의 정보를 출력한다.
				System.out.print("( " + k_array[j].v + " , " + k_array[j].w + " ) ");
			}
		}
		System.out.println("");
		System.out.println("Min Cost : " + minCost); // 최단 경로에 있는 모든 간선들의 가중치를 출력한다.
	}
	
	
	public Edge[] sort(Edge[] e) { // 객체 배열에 들어있는 Edge 객체들의 weight를 바탕으로 오름차순으로 정렬한다.
		Edge tempEdge = new Edge();
		for (int i = 0; i < e.length; i++) {
			for (int j = i + 1; j < e.length; j++) {
				if (e[i].weight > e[j].weight) {
					tempEdge = e[i];
					e[i] = e[j];
					e[j] = tempEdge;
				}
			}
		}
		return e;
	}

}
class Edge { // Edge 클래스 선언.
	int v; // start Vertex index
	int w; // end Vertex index
	int weight; // 가중치
	boolean selected; // 간선으로 선택되었는지 여부

	public Edge() { // 기본 생성자.
	};

	public Edge(int v, int w, int weight, boolean selected) { // 위의 정보들을 담고있는 Edge 객체 생성자 선언.
		this.v = v;
		this.w = w;
		this.weight = weight;
		this.selected = selected;
	}
}
