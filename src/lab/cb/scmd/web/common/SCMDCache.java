package lab.cb.scmd.web.common;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class SCMDCache<K,V> {
	protected LinkedHashMap<K,V> cache = new LinkedHashMap<K,V>();
	protected boolean autodel = false;
	protected int CACHE_SIZE = 0;
	/**
	 * @param max �ۑ������
	 * @param autodel TRUE�̏ꍇ��MAX�ʂ𒴂����ꍇ�Ɏg���Ă��Ȃ����̏��ɏ����Ă���
	 */
	public SCMDCache(int cachesize) {
		CACHE_SIZE = cachesize;
		if(CACHE_SIZE <=0) {
			CACHE_SIZE = 100;
		}
//		this.autodel = autodel;
	}

	public void put(K key,V value) {
		cache.put(key,value);
		//	�L���b�V���T�C�Y�������ς��̏ꍇ�͍Ō�ɃL���b�V�������f�[�^������
		if(cache.size() > CACHE_SIZE) {
			cache.remove(cache.keySet().iterator().next());
		}
	}

	/**
	 * �i�[�������̂����o��
	 * @param k
	 * @return
	 */
	public V get(K key) {
		V value = cache.get(key);

		if(value != null) {
			cache.remove(key);
			cache.put(key,value);
		}

		return cache.get(key);
	}
	
	public Iterator<K> getKeyIterator() {
		return cache.keySet().iterator();
	}
	
	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}
}


//	-------------------------
//	$log: $
//	-------------------------