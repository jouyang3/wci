package wci.intermediate.icodeimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import wci.intermediate.ICodeFactory;
import wci.intermediate.ICodeKey;
import wci.intermediate.ICodeNode;
import wci.intermediate.ICodeNodeType;

public class ICodeNodeImpl extends HashMap<ICodeKey, Object> implements ICodeNode, Cloneable {
	
	private ICodeNode parentNode;
	private ICodeNodeType type;
	private List<ICodeNode> childNodes;
	
	public ICodeNodeImpl(ICodeNodeType type){
		this.parentNode = null;
		this.type = type;
		this.childNodes = new Vector<ICodeNode>();
	}
	
	public ICodeNodeImpl(ICodeNodeType type, ICodeNode parentNode){
		this.parentNode = parentNode;
		this.type = type;
		this.childNodes = new Vector<ICodeNode>();
	}

	@Override
	public ICodeNode getParent() {
		return parentNode;
	}

	@Override
	public ICodeNodeType getType() {
		return type;
	}

	@Override
	public void addChild(ICodeNode childNode) {
		childNodes.add(childNode);
		childNode.setParent(this);
	}
	
	@Override
	public void setParent(ICodeNode parentNode){
		this.parentNode = parentNode;
	}

	@Override
	public List<ICodeNode> getChildren() {
		return childNodes;
	}

	@Override
	public void setAttribute(ICodeKey key, Object value) {
		put(key, value);
	}

	@Override
	public Object getAttribute(ICodeKey key) {
		return get(key);
	}
	
	@Override
	public Object clone(){
		ICodeNodeImpl copy = (ICodeNodeImpl) ICodeFactory.createICodeNode(type,parentNode);
		Map<ICodeKey, Object> cpAttr = new HashMap<ICodeKey, Object>();
		Set<ICodeKey> keys = keySet();
		for(ICodeKey key:keys){
			cpAttr.put(key, get(key));
		}
		return copy;
	}

	@Override
	public ICodeNode copy(){
		return (ICodeNode)this.clone();
	}
	
	@Override
	public String toString(){
		return type.toString();
	}

}
