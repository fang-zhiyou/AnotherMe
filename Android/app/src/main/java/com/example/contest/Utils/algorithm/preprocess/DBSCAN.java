package com.example.contest.Utils.algorithm.preprocess;

import com.example.contest.Utils.algorithm.geography.Calculations;
import com.example.contest.Utils.algorithm.geography.Point;

import java.util.ArrayList;

public class DBSCAN {
    public ArrayList<FlagPoint> D;
    public ArrayList<Cluster> C;
    public ArrayList<Point> noise;
    double Eps;
    int MinPts;

    //eps 是半径  MinPts是最小邻居数
    public DBSCAN(ArrayList<Point> points, double eps, int minPts) {
        D = new ArrayList<>();
        C=new ArrayList<>();
        noise=new ArrayList<>();

        for(int i=0;i<points.size();i++){
            D.add(new FlagPoint(points.get(i),i));
        }

        for (;D.size()!=0;) {
            FlagPoint p=D.get(0);
            p.visited = true;
            D.removeIf(flagPoint -> flagPoint.visited);

            ArrayList<FlagPoint> N = getNeighbors(p, eps);
            if (N.size() < minPts) {
                noise.add(p.point);
            }else{
                Cluster cluster=new Cluster();
                ExpandCluster(p,N,cluster,eps,minPts);
                C.add(cluster);
            }
        }
    }

    private void ExpandCluster(FlagPoint p, ArrayList<FlagPoint> N, Cluster cluster, double eps, int minPts) {
        cluster.add(p);
        p.clustered=true;

        for(;N.size()!=0;){
            FlagPoint p_hat=N.get(0);
            p_hat.visited=true;
            N.removeIf(flagPoint -> flagPoint.visited);
            ArrayList<FlagPoint> N_hat=getNeighbors(p_hat,Eps);
            if(N_hat.size()>=MinPts){
                N.addAll(N_hat);
            }
            if(!p_hat.clustered){
                cluster.add(p_hat);
                p_hat.clustered=true;
            }
        }
    }

    private ArrayList<FlagPoint> getNeighbors(FlagPoint p, double Eps) {
        ArrayList<FlagPoint> neighbors=new ArrayList<>();
        for(int i=0;i<D.size();i++){
            FlagPoint p2=D.get(i);
            if(Calculations.getDistFromGeo(p.point,p2.point)<Eps){
                neighbors.add(p2);
            }
        }
        return neighbors;
    }
}

class Cluster{
    ArrayList<Point> members;

    public void add(FlagPoint p){
        members.add(p.point);
    }

    public Cluster() {
        members=new ArrayList<>();
    }
}

class FlagPoint {
    boolean visited;
    boolean clustered;
    Integer i;
    Point point;

    public FlagPoint(Point point,int i) {
        this.visited = false;
        this.clustered=false;
        this.point = point;
        this.i=i;
    }
}


