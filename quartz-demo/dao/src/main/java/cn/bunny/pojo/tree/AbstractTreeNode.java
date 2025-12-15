package cn.bunny.pojo.tree;

import java.util.List;

public interface AbstractTreeNode {
    Long getId();

    Long getParentId();

    void setChildren(List<? extends AbstractTreeNode> children);
}
