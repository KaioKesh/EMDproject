package kesh.emd.marist.powertree.UserTree;

/**
 * Created by Speck on 4/3/2016.
 */
public class Company {
    private int company;
    private String[] positions;
    private Node companytree;

    public Company(User president){
        this.companytree=new Node(president);
    }

}
