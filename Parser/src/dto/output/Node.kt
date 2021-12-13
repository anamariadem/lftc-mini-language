package dto.output

data class Node(val value: String, val child: Node? = null, val sibling: Node? = null)