package kesh.emd.marist.powertree.UserTree;

import java.util.ArrayList;

/**
 * Created by Keshine on 4/16/2016.
 */
public class Tree {
    private User root;
    private ArrayList<Tree> subs;
    public int treetier;
    public Tree(User superior,int tier){
        this.root = superior;
        this.subs = new ArrayList<>();
        this.treetier=tier;
    }

    /**
     * Adds one new user to list of subordinates
     * @param newsub new subordinate
     */
    public void addsub(User newsub){
        this.subs.add(new Tree(newsub,treetier+1) );

    }

    /**
     * Adds list of new subordinates to end of current list
     * @param subtrees
     */
    public void addsubtrees(ArrayList<Tree> subtrees){
        subs.addAll(subtrees);
    }
    public void addsubs(ArrayList<User> newsubs){
        for(User newsub : newsubs){
            subs.add(new Tree(newsub,treetier+1));
        }
    }
    public void addto(User newsub){
        subs.add(new Tree(newsub,treetier));
    }

    public void changeroot(User newsuperior){
        this.root = newsuperior;
    }
    public User getroot(){
        return this.root;
    }
    public Tree getsubtree(User subtreeroot){
        for(Tree subsearch : subs){
            if(subsearch.getroot().equals(subtreeroot)){
                return subsearch;
            }
        }
        return null;
    }
    public Boolean changesub(User newsub,User oldsub){
        for(Tree subsearch : subs){
            if(subsearch.getroot().equals(oldsub)){
                subsearch.changeroot(newsub);
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]){
        User ko = new User(1000);
        User ko11 = new User(1011);
        User ko12 = new User(1012);
        User ko21 = new User(1021);
        User ko22 = new User(1022);
        User ko23 = new User(1023);
        User ko24 = new User(1024);

        User ko45 = new User(1011);
        Tree tree1 = new Tree(ko,0);
        tree1.addsub(ko11);
        tree1.addsub(ko12);

        if(tree1.changesub(ko21,ko45)){
            System.out.println("subordinate changed");
        }
        else{
            System.out.println("subordinate not found");
        }
    }
}
