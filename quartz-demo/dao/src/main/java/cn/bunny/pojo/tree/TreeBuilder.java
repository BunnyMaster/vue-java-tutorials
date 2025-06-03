package cn.bunny.pojo.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder<T extends AbstractTreeNode> {

    public List<T> buildTree(List<T> nodeList) {
        List<T> tree = new ArrayList<>();
        for (T node : nodeList) {
            if (node.getParentId() == 0) {
                node.setChildren(getChildren(node.getId(), nodeList));
                tree.add(node);
            }
        }
        return tree;
    }

    private List<T> getChildren(Long nodeId, List<T> nodeList) {
        List<T> children = new ArrayList<>();
        for (T node : nodeList) {
            if (node.getParentId().equals(nodeId)) {
                node.setChildren(getChildren(node.getId(), nodeList));
                children.add(node);
            }
        }
        return children;
    }
}
