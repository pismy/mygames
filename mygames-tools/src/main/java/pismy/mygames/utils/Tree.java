package pismy.mygames.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * JavaScript native implementation of a tree structure.
 * <p>
 * A tree is basically a set of <strong>nodes</strong> with hierarchical links.
 * Each <strong>nodes</strong> has:
 * <ul>
 * <li>none or several children nodes,
 * <li>a stored value (leaf).
 * </ul>
 * <p>
 * Nodes are navigable with relative <strong>paths<strong> (array of string).
 * 
 * @param <V>
 *            the type of leaf values
 */
public class Tree<V> {

	protected V value;
	protected Map<String, Tree<V>> children;
	/**
	 * Returns the subtree structure located at the given path.
	 * 
	 * @param path
	 *            the subtree path
	 * @param create
	 *            determines whether the subtree structure should be created if
	 *            it does not exist
	 * @return the subtree (<code>null</code> if doesn't exist)
	 */
	public Tree<V> getSubTree(String[] path, boolean create) {
		Tree<V> tree = this;
		// recurse path
		for (int i = 0; i < path.length && tree != null; i++)
			tree = tree.getChildTree(path[i], create);
		return tree;
	}

	/**
	 * Returns direct child tree structure with the given name.
	 * 
	 * @param name
	 *            the child subtree name
	 * @param create
	 *            determines whether the subtree structure should be created if
	 *            it does not exist
	 * @return the subtree (<code>null</code> if doesn't exist)
	 */
	public Tree<V> getChildTree(String name, boolean create) {
		if(children == null) {
			if(!create)
				return null;
			// create
			children = new HashMap<String, Tree<V>>();
		} else {
			Tree<V> child = children.get(name);
			if(child != null)
				return child;
			if(!create)
				return null;
		}
		// we have to create
		Tree<V> child = new Tree<V>();
		children.put(name, child);
		return child;
	}

	/**
	 * Returns the value stored in this tree node
	 */
	public V get() {
		return value;
	}
	
	/**
	 * Sets the value stored in this tree node
	 */
	public void set(V value) {
		this.value = value;
	}

	/**
	 * Deletes the value stored in this tree node
	 */
	public V delete() {
		V v = value;
		value = null;
		return v;
	}

	/**
	 * Determines whether this node has a stored value
	 */
	public boolean hasValue() {
		return get() != null;
	}

	/**
	 * Returns the number of (direct) children tree nodes.
	 * 
	 * @return the number of (direct) children tree nodes.
	 */
	public int getChildrenCount() {
		return children == null ? 0 : children.size();
	}

	/**
	 * Returns the list of (direct) children tree nodes names.
	 * 
	 * @return the list of (direct) children tree nodes names.
	 */
	public Set<String> getChildrenNames() {
		return children == null ? Collections.EMPTY_SET : children.keySet();
	}

	/**
	 * Empties this tree node (both children nodes and value)
	 */
	public void clear() {
		delete();
		children = null;
	}

	/**
	 * Returns the value located at the given path in the tree.
	 * 
	 * @param path
	 *            the path
	 * @return the value (<code>null</code> if no value is associated to the
	 *         path)
	 */
	public V get(String[] path) {
		Tree<V> sub = getSubTree(path, false);
		return sub == null ? null : sub.get();
	}

	/**
	 * Puts the specified value at the specified path in this tree.
	 * If the tree previously contained a value at this location, the old value
	 * is replaced by the specified value.
	 * 
	 * @param path
	 *            the location where the value shall be placed in the tree.
	 * @param value
	 *            the value.
	 */
	public void put(String[] path, V value) {
		Tree<V> sub = getSubTree(path, true);
		sub.set(value);
	}

	/**
	 * Removes the value located at the specified path location if it is
	 * present.
	 * <p>
	 * Returns the value that was removed at the given location, or
	 * <tt>null</tt> if no value was found.
	 * 
	 * @param path
	 *            path whose mapping is to be removed from the map.
	 * @return previous value associated with specified path, or <tt>null</tt>
	 *         if there was no mapping for path.
	 */
	public V remove(String[] path) {
		Tree<V> sub = getSubTree(path, false);
		// TODO: also remove empty nodes ?
		return sub == null ? null : sub.delete();
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("+-- root");
		return toString(buffer, 0).toString();
	}
	protected StringBuffer toString(StringBuffer buffer, int depth) {
		buffer.append(": "+get()+"\n");
		if(children != null) {
			depth++;
			for(Entry<String, Tree<V>> entry : children.entrySet()) {
				for(int i=0; i<depth; i++)
					buffer.append("  ");
				buffer.append("+-- ");
				buffer.append(entry.getKey());
				entry.getValue().toString(buffer, depth);
			}
		}
		return buffer;
	}
}
