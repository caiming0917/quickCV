package com.caijuan.juc.forkjoin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TreeNode {

    int value;

    Set<TreeNode> children;

    TreeNode(int value, TreeNode... children) {
        this.value = value;
        this.children = new HashSet<>(Arrays.asList(children));
    }
}