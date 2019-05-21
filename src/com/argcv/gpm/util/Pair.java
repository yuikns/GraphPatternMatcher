package com.argcv.gpm.util;

public class Pair<T, K> {
	private T key;
	private K value;

	public Pair() {
		super();
	}

	public Pair(T key, K value) {
		super();
		this.key = key;
		this.value = value;
	}

	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public K getValue() {
		return value;
	}

	public void setValue(K value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair))
			return false;
		else {
			return ((((Pair<?, ?>)o).getKey().equals(this.key)) &&
					(((Pair<?, ?>)o).getValue().equals(this.value)));
		}
	}
	
	public static void main(String []args){
		Pair<Integer,String> pair = new Pair<Integer,String>(1,"Good");
		System.out.println(pair.getKey() + "," + pair.getValue());
	}
}
