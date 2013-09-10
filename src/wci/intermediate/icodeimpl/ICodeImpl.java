package wci.intermediate.icodeimpl;

import wci.intermediate.ICode;
import wci.intermediate.ICodeNode;

public class ICodeImpl implements ICode {
	
	private ICodeNode rootNode;
	
	public ICodeImpl(){
		
	}
	
	public ICodeImpl(ICodeNode node){
		this.rootNode = node;
	}

	@Override
	public ICodeNode setRoot(ICodeNode rootNode) {
		return this.rootNode = rootNode;
	}

	@Override
	public ICodeNode getRoot() {
		return rootNode;
	}

}
