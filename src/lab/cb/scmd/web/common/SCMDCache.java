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
	 * @param max 保存する量
	 * @param autodel TRUEの場合はMAX量を超えた場合に使われていないもの順に消していく
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
		//	キャッシュサイズがいっぱいの場合は最後にキャッシュしたデータを消す
		if(cache.size() > CACHE_SIZE) {
			cache.remove(cache.keySet().iterator().next());
		}
	}

	/**
	 * 格納したものを取り出す
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