package kesh.emd.marist.powertree.UserTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keshine on 4/29/2016.
 */
public class Node<User> {
    private List<Node<User>> children = new ArrayList<Node<User>>();
    private Node<User> parent = null;
    private User user = null;

    public Node(User user) {
        this.user = user;
    }

    public Node(User user, Node<User> parent) {
        this.user = user;
        this.parent = parent;
    }

    public List<Node<User>> getChildren() {
        return children;
    }

    public void setParent(Node<User> parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(User user) {
        Node<User> child = new Node<User>(user);
        if(child.parent!=this) {
            child.setParent(this);
            this.children.add(child);
        }
    }
    //bugged?
    public void addChild(Node<User> child) {
        if(child.parent!=this){
            child.setParent(this);
            this.children.add(child);
        }
    }

    public User getUser() {
        return this.user;
    }
}
