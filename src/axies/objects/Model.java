package axies.objects;

public class Model {
    Cube[] cubes;

    Point[] vertexes;

    int[][] edges;

    public Model(Cube[] cubes, Point[] vertexes, int[][] edges){
        this.cubes = cubes;
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public Model(Cube[] cubes, Cube[] visableCubes){
        this.cubes = cubes;
        vertexes = new Point[Cube.vertexes.length*visableCubes.length];
        int vertexPointer = 0;
        edges = new int[Cube.edges.length*visableCubes.length][2];
        int edgePointer = 0;
        int cubePointer = 0;
        for (Cube cube : visableCubes) {
            
            for (int i = 0; i < Cube.vertexes.length; i++) {
                vertexes[vertexPointer] = new Point();
                for (int j = 0; j < Cube.vertexes[0].length; j++) {
                    vertexes[vertexPointer].setAxis(j,cube.getPositionAxis(j)+cube.getSizeAxis(j)*Cube.vertexes[i][j]);
                }
                vertexPointer++;
            }
            for (int i = 0; i < Cube.edges.length; i++) {
                edges[edgePointer][0] = Cube.edges[edgePointer%Cube.edges.length][0]+cubePointer;
                edges[edgePointer][1] = Cube.edges[edgePointer%Cube.edges.length][1]+cubePointer;
                edgePointer++;
            }

            cubePointer = vertexPointer;
        }
    }

    public Cube[] getCubes(){
        return cubes;
    }

    public int[][] getEdges() {
        return edges;
    }

    public Point[] getVertexes() {
        return vertexes;
    }
}
